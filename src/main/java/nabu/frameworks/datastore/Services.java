package nabu.frameworks.datastore;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.module.datastore.provider.DatastoreProviderArtifact;
import be.nabu.eai.module.datastore.route.DatastoreRouteArtifact;
import be.nabu.eai.module.datastore.urn.URNProviderArtifact;
import be.nabu.eai.repository.EAIResourceRepository;
import be.nabu.eai.repository.util.SystemPrincipal;
import be.nabu.libs.datastore.DatastoreOutputStream;
import be.nabu.libs.datastore.api.ContextualURNManager;
import be.nabu.libs.datastore.api.ContextualWritableDatastore;
import be.nabu.libs.datastore.api.DataProperties;
import be.nabu.libs.datastore.api.WritableDatastore;
import be.nabu.libs.datastore.resources.DataRouter;
import be.nabu.libs.datastore.resources.ResourceDatastore;
import be.nabu.libs.datastore.resources.context.StringContextBaseRouter;
import be.nabu.libs.resources.URIUtils;
import be.nabu.libs.services.ServiceRuntime;
import be.nabu.libs.services.ServiceUtils;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.services.api.ExecutionContext;
import be.nabu.libs.services.api.ServiceException;
import be.nabu.libs.services.pojo.POJOUtils;
import be.nabu.libs.types.TypeUtils;
import be.nabu.libs.types.api.ComplexContent;
import be.nabu.libs.types.api.ComplexType;
import be.nabu.libs.types.api.Element;
import be.nabu.utils.io.IOUtils;

@WebService
public class Services {

	private static Map<String, DatastoreRouteArtifact> routes = new HashMap<String, DatastoreRouteArtifact>();
	private static Map<URNProviderArtifact, ContextualURNManager> urnProviders = new HashMap<URNProviderArtifact, ContextualURNManager>();
	private static Map<DatastoreProviderArtifact, WritableDatastore> datastoreProviders = new HashMap<DatastoreProviderArtifact, WritableDatastore>();
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ServiceRuntime runtime;
	
	public static void refresh() {
		routes.clear();
		urnProviders.clear();
		datastoreProviders.clear();
	}
	
	@WebResult(name = "uri")
	public URI zip(@WebParam(name = "context") String context, @WebParam(name = "name") String name, @WebParam(name = "uris") List<URI> uris) throws URISyntaxException, IOException, ServiceException {
		if (context == null) {
			context = ServiceUtils.getServiceContext(runtime);
		}
		if (uris == null || uris.isEmpty()) {
			return null;
		}
		DatastoreOutputStream streamable = streamable(runtime, context, name, "application/zip");
		ByteArrayOutputStream output = null;
		ZipOutputStream zip;
		if (streamable != null) {
			zip = new ZipOutputStream(streamable);
		}
		else {
			output = new ByteArrayOutputStream();
			zip = new ZipOutputStream(output);
		}
		try {
			for (URI uri : uris) {
				if (uri == null) {
					continue;
				}
				DataProperties properties = properties(uri);
				InputStream input = retrieve(uri);
				if (properties == null || input == null) {
					throw new IllegalArgumentException("Can not resolve uri: " + uri);
				}
				try {
					input = new BufferedInputStream(input);
					ZipEntry entry = new ZipEntry(properties.getName());
					zip.putNextEntry(entry);
					IOUtils.copyBytes(IOUtils.wrap(input), IOUtils.wrap(zip));
				}
				finally {
					input.close();
				}
			}
		}
		finally {
			zip.close();
		}
		URI uri = streamable == null ? null : streamable.getURI();
		if (uri == null && output != null) {
			uri = store(context, name, "application/zip", new ByteArrayInputStream(output.toByteArray()));
		}
		return uri;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@WebResult(name = "uri")
	public URI store(@WebParam(name = "context") String context, @WebParam(name = "name") String name, @WebParam(name = "contentType") String contentType, @WebParam(name = "stream") InputStream stream) throws IOException, URISyntaxException, ServiceException {
		if (context == null) {
			context = ServiceUtils.getServiceContext(runtime);
		}
		logger.debug("Storing data in context: " + context);
		DatastoreRouteArtifact route = getRoute(context);
		logger.trace("Chosen route: {}", route);
		
		ContextualURNManager urnManager = null;
		if (route != null) {
			logger.trace("Available parameters in route: {}", route.getConfiguration().getProperties());
			urnManager = getURNManager(route.getConfiguration().getUrnProvider());
			logger.trace("Chosen urn manager: {}", urnManager);
		}
		
		URI result;
		// use the default provider
		if (route == null || route.getConfiguration().getDatastoreProvider() == null) {
			String url = route == null ? null : route.getConfiguration().getProperties().get("url");
			logger.trace("Default datastore with url: " + url);
			ResourceDatastore datastore = newResourceDatastore(url);
			result = datastore.store(route == null ? context : route.getConfiguration().getContext(), stream, name, contentType);
		}
		else {
			DefinedService storeService = route.getConfiguration().getDatastoreProvider().getConfiguration().getStoreService();
			if (storeService == null) {
				throw new IllegalArgumentException("The datastore provider has no store service configured: " + route.getConfiguration().getDatastoreProvider().getId());
			}
			ComplexContent input = storeService.getServiceInterface().getInputDefinition().newInstance();
			input.set("stream", stream);
			input.set("name", name);
			input.set("contentType", contentType);
			setCustomProperties(route, input);
			ServiceRuntime runtime = new ServiceRuntime(storeService, this.runtime.getExecutionContext());
			ComplexContent run = runtime.run(input);
			result = (URI) run.get("uri");
			// make sure it has the correct scheme
			if (result.getScheme() == null) {
				result = new URI(URIUtils.encodeURI(route.getConfiguration().getDatastoreProvider().getConfiguration().getScheme() + "://" + result.toString()));
			}
			else if (!route.getConfiguration().getDatastoreProvider().getConfiguration().getScheme().equals(result.getScheme())) {
				result = new URI(URIUtils.encodeURI(result.toString().replaceFirst("^[^:]://", route.getConfiguration().getDatastoreProvider().getConfiguration().getScheme() + "://")));
			}
		}
		if (result == null) {
			throw new IllegalArgumentException("No 'uri' returned by the provider");
		}
		logger.debug("Stored URI: {}", result);
		return urnManager == null ? result : urnManager.map(context, result);
	}
	
	public void delete(@WebParam(name = "uri") URI uri) throws IOException, URISyntaxException, ServiceException {
		if (uri != null) {
			URI url = resolveURL(uri);
			if (url.getScheme() == null) {
				throw new IllegalArgumentException("Can not resolve a URL without scheme: " + url);
			}
			boolean found = false;
			for (DatastoreProviderArtifact provider : EAIResourceRepository.getInstance().getArtifacts(DatastoreProviderArtifact.class)) {
				if (url.getScheme().equals(provider.getConfiguration().getScheme())) {
					DefinedService deleteService = provider.getConfiguration().getDeleteService();
					if (deleteService == null) {
						throw new IllegalArgumentException("The datastore provider has no delete service configured: " + provider.getId());
					}
					ComplexContent input = deleteService.getServiceInterface().getInputDefinition().newInstance();
					input.set("uri", url);
					ServiceRuntime runtime = new ServiceRuntime(deleteService, this.runtime.getExecutionContext());
					runtime.run(input);
					found = true;
					break;
				}
			}
			// if no provider was found that can handle the scheme, try with the resource datastore
			if (!found) {
				newResourceDatastore(null).delete(url);
			}
		}
	}

	@WebResult(name = "properties")
	public DataProperties properties(@WebParam(name = "uri") URI uri) throws IOException, URISyntaxException, ServiceException {
		if (uri == null) {
			return null;
		}
		URI url = resolveURL(uri);
		if (url.getScheme() == null) {
			throw new IllegalArgumentException("Can not resolve a URL without scheme: " + url);
		}
		for (DatastoreProviderArtifact provider : EAIResourceRepository.getInstance().getArtifacts(DatastoreProviderArtifact.class)) {
			if (url.getScheme().equals(provider.getConfiguration().getScheme())) {
				DefinedService propertiesService = provider.getConfiguration().getPropertiesService();
				if (propertiesService == null) {
					throw new IllegalArgumentException("The datastore provider has no properties service configured: " + provider.getId());
				}
				ComplexContent input = propertiesService.getServiceInterface().getInputDefinition().newInstance();
				input.set("uri", url);
				ServiceRuntime runtime = new ServiceRuntime(propertiesService, this.runtime.getExecutionContext());
				ComplexContent run = runtime.run(input);
				return (DataProperties) run.get("properties");
			}
		}
		// if we get here, no provider was found that can handle the scheme, try with the resource datastore
		return newResourceDatastore(null).getProperties(url);
	}
	
	@WebResult(name = "stream")
	public InputStream retrieve(@WebParam(name="uri") URI uri) throws IOException, ServiceException, URISyntaxException {
		if (uri == null) {
			return null;
		}
		URI url = resolveURL(uri);
		if (url.getScheme() == null) {
			throw new IllegalArgumentException("Can not resolve a URL without scheme: " + url);
		}
		for (DatastoreProviderArtifact provider : EAIResourceRepository.getInstance().getArtifacts(DatastoreProviderArtifact.class)) {
			if (url.getScheme().equals(provider.getConfiguration().getScheme())) {
				DefinedService retrieveService = provider.getConfiguration().getRetrieveService();
				if (retrieveService == null) {
					throw new IllegalArgumentException("The datastore provider has no retrieve service configured: " + provider.getId());
				}
				ComplexContent input = retrieveService.getServiceInterface().getInputDefinition().newInstance();
				input.set("uri", url);
				ServiceRuntime runtime = new ServiceRuntime(retrieveService, this.runtime.getExecutionContext());
				ComplexContent run = runtime.run(input);
				return (InputStream) run.get("stream");
			}
		}
		// if we get here, no provider was found that can handle the scheme, try with the resource datastore
		return new BufferedInputStream(newResourceDatastore(null).retrieve(url));
	}

	private URI resolveURL(URI uri) throws IOException {
		URI url = null;
		if ("urn".equals(uri.getScheme())) {
			for (URNProviderArtifact provider : EAIResourceRepository.getInstance().getArtifacts(URNProviderArtifact.class)) {
				url = getURNManager(provider).resolve(uri);
				if (url != null) {
					break;
				}
			}
		}
		else {
			url = uri;
		}
		if (url == null) {
			throw new IllegalArgumentException("Could not resolve the uri: " + uri);
		}
		return url;
	}

	private void setCustomProperties(DatastoreRouteArtifact route, ComplexContent input) throws IOException {
		for (Element<?> child : TypeUtils.getAllChildren(input.getType())) {
			if (child.getType().equals(route.getConfiguration().getDatastoreProvider().getConfiguration().getConfigurationType())) {
				ComplexContent properties = ((ComplexType) child.getType()).newInstance();
				for (String key : route.getConfiguration().getProperties().keySet()) {
					properties.set(key, route.getConfiguration().getProperties().get(key));
				}
				input.set(child.getName(), properties);
				break;
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ResourceDatastore newResourceDatastore(String url) throws URISyntaxException {
		DataRouter router = url == null ? new StringContextBaseRouter() : new StringContextBaseRouter(new URI(URIUtils.encodeURI(url)));
		ResourceDatastore datastore = new ResourceDatastore(router);
		datastore.setPrincipal(runtime.getExecutionContext().getSecurityContext().getToken());
		return datastore;
	}
	
	@SuppressWarnings("unused")
	private WritableDatastore getDatastore(DatastoreProviderArtifact provider, boolean forWriting) throws IOException {
		if (!datastoreProviders.containsKey(provider)) {
			synchronized(datastoreProviders) {
				if (!datastoreProviders.containsKey(provider)) {
					if (provider.getConfiguration().getPropertiesService() == null) {
						throw new IllegalArgumentException("Datastore provider missing properties service");
					}
					if (provider.getConfiguration().getRetrieveService() == null) {
						throw new IllegalArgumentException("Datastore provider missing retrieve service");
					}
					if (forWriting && provider.getConfiguration().getStoreService() == null) {
						throw new IllegalArgumentException("Datastore provider missing store service");
					}
					WritableDatastore datastore = POJOUtils.newProxy(WritableDatastore.class,  
						EAIResourceRepository.getInstance(), 
						SystemPrincipal.ROOT, 
						provider.getConfiguration().getPropertiesService(), 
						provider.getConfiguration().getRetrieveService(),
						provider.getConfiguration().getStoreService()
					);
					datastoreProviders.put(provider, datastore);
				}
			}
		}
		return datastoreProviders.get(provider);
	}
	
	private ContextualURNManager getURNManager(URNProviderArtifact provider) throws IOException {
		if (provider == null) {
			return null;
		}
		else if (!urnProviders.containsKey(provider)) {
			synchronized(urnProviders) {
				if (!urnProviders.containsKey(provider)) {
					if (provider.getConfiguration().getUrnCreator() == null) {
						throw new IllegalArgumentException("URN provider missing urn creator service");
					}
					if (provider.getConfiguration().getUrnResolver() == null) {
						throw new IllegalArgumentException("URN provider missing urn resolver service");
					}
					ContextualURNManager manager = POJOUtils.newProxy(
						ContextualURNManager.class, 
						EAIResourceRepository.getInstance(), 
						SystemPrincipal.ROOT, 
						provider.getConfiguration().getUrnCreator(), 
						provider.getConfiguration().getUrnResolver()
					);
					urnProviders.put(provider, manager);
				}
			}
		}
		return urnProviders.get(provider);
	}
	
	private DatastoreRouteArtifact getRoute(String context) {
		if (!routes.containsKey(context)) {
			synchronized(routes) {
				if (!routes.containsKey(context)) {
					DatastoreRouteArtifact closest = null;
					for (DatastoreRouteArtifact route : EAIResourceRepository.getInstance().getArtifacts(DatastoreRouteArtifact.class)) {
						try {
							if (!route.isStarted()) {
								continue;
							}
							// a route without context matches all contexts
							if (route.getConfiguration().getContext() == null) {
								if (closest == null) {
									closest = route;
								}
							}
							// contexts are dot based, the most specific one wins
							else if (context != null && (context.equals(route.getConfiguration().getContext()) || context.startsWith(route.getConfiguration().getContext() + "."))) {
								// if the context is more specific (in other words longer), take that one
								if (closest == null || closest.getConfiguration().getContext() == null || closest.getConfiguration().getContext().length() < route.getConfiguration().getContext().length()) {
									closest = route;
								}
							}
						}
						catch (Exception e) {
							logger.error("Could not load: " + route.getId(), e);
						} 
					}
					routes.put(context, closest);
				}
			}
		}
		return routes.get(context);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static DatastoreOutputStream streamable(ServiceRuntime runtime, String context, String name, String contentType) throws URISyntaxException, IOException {
		if (context == null) {
			context = ServiceUtils.getServiceContext(runtime);
		}
		Services services = new Services();
		services.runtime = runtime;
		DatastoreRouteArtifact route = services.getRoute(context);
		// the default provider supports streaming
		if (route == null || route.getConfiguration().getDatastoreProvider() == null) {
			String url = route == null ? null : route.getConfiguration().getProperties().get("url");
			ResourceDatastore datastore = services.newResourceDatastore(url);
			return datastore.stream(context, name, contentType);
		}
		return null;
	}
	
	public static ContextualWritableDatastore<String> getAsDatastore(final ExecutionContext context) {
		final DefinedService store = context.getServiceContext().getResolver(DefinedService.class).resolve("nabu.frameworks.datastore.Services.store");
		final DefinedService retrieve = context.getServiceContext().getResolver(DefinedService.class).resolve("nabu.frameworks.datastore.Services.retrieve");
		final DefinedService properties = context.getServiceContext().getResolver(DefinedService.class).resolve("nabu.frameworks.datastore.Services.properties");
		return new ContextualWritableDatastore<String>() {
			@Override
			public URI store(InputStream input, String name, String contentType) throws IOException {
				return store(null, input, name, contentType);
			}
			@Override
			public InputStream retrieve(URI uri) throws IOException {
				if (retrieve == null) {
					throw new IllegalArgumentException("Could not find retrieve service");
				}
				ComplexContent input = retrieve.getServiceInterface().getInputDefinition().newInstance();
				input.set("uri", uri);
				ServiceRuntime runtime = new ServiceRuntime(retrieve, context);
				try {
					ComplexContent run = runtime.run(input);
					return (InputStream) run.get("stream");
				}
				catch (ServiceException e) {
					throw new RuntimeException(e);
				}
			}
			@Override
			public DataProperties getProperties(URI uri) throws IOException {
				if (properties == null) {
					throw new IllegalArgumentException("Could not find properties service");
				}
				ComplexContent input = properties.getServiceInterface().getInputDefinition().newInstance();
				input.set("uri", uri);
				try {
					ServiceRuntime runtime = new ServiceRuntime(properties, context);
					ComplexContent run = runtime.run(input);
					return (DataProperties) run.get("properties");
				}
				catch (ServiceException e) {
					throw new RuntimeException(e);
				}
			}
			@Override
			public URI store(String storeContext, InputStream stream, String name, String contentType) throws IOException {
				if (store == null) {
					throw new IllegalArgumentException("Could not find store service");
				}
				ComplexContent input = store.getServiceInterface().getInputDefinition().newInstance();
				input.set("context", storeContext);
				input.set("stream", stream);
				input.set("name", name);
				input.set("contentType", contentType);
				try {
					ServiceRuntime runtime = new ServiceRuntime(store, context);
					ComplexContent run = runtime.run(input);
					return (URI) run.get("uri");
				}
				catch (ServiceException e) {
					throw new RuntimeException(e);
				}
			}
			@Override
			public Class<String> getContextClass() {
				return String.class;
			}
		};
	}
}
