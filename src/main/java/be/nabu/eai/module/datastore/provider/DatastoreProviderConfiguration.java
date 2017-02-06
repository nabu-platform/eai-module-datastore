package be.nabu.eai.module.datastore.provider;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.types.api.DefinedType;

@XmlRootElement(name = "datastoreProvider")
@XmlType(propOrder = { "scheme", "retrieveService", "propertiesService", "storeService", "deleteService", "configurationType" })
public class DatastoreProviderConfiguration {

	private String scheme;
	private DefinedService retrieveService;
	private DefinedService propertiesService;
	private DefinedService storeService;
	private DefinedService deleteService;
	private DefinedType configurationType;
	
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.Datastore.getProperties")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getPropertiesService() {
		return propertiesService;
	}
	public void setPropertiesService(DefinedService propertiesService) {
		this.propertiesService = propertiesService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.Datastore.retrieve")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getRetrieveService() {
		return retrieveService;
	}
	public void setRetrieveService(DefinedService retrieveService) {
		this.retrieveService = retrieveService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.WritableDatastore.store")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getStoreService() {
		return storeService;
	}
	public void setStoreService(DefinedService storeService) {
		this.storeService = storeService;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedType getConfigurationType() {
		return configurationType;
	}
	public void setConfigurationType(DefinedType configurationType) {
		this.configurationType = configurationType;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public DefinedService getDeleteService() {
		return deleteService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.DeletableDatastore.delete")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public void setDeleteService(DefinedService deleteService) {
		this.deleteService = deleteService;
	}
}
