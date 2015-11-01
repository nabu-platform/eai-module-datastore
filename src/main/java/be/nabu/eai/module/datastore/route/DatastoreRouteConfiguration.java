package be.nabu.eai.module.datastore.route;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.module.datastore.provider.DatastoreProviderArtifact;
import be.nabu.eai.module.datastore.urn.URNProviderArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.eai.repository.util.KeyValueMapAdapter;

@XmlRootElement(name = "datastoreRoute")
@XmlType(propOrder = { "context", "urnProvider", "datastoreProvider", "properties" })
public class DatastoreRouteConfiguration {
	
	private String context;
	private DatastoreProviderArtifact datastoreProvider;
	private URNProviderArtifact urnProvider;
	private Map<String, String> properties;
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DatastoreProviderArtifact getDatastoreProvider() {
		return datastoreProvider;
	}
	public void setDatastoreProvider(DatastoreProviderArtifact datastoreProvider) {
		this.datastoreProvider = datastoreProvider;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public URNProviderArtifact getUrnProvider() {
		return urnProvider;
	}
	public void setUrnProvider(URNProviderArtifact urnProvider) {
		this.urnProvider = urnProvider;
	}
	@XmlJavaTypeAdapter(value = KeyValueMapAdapter.class)
	public Map<String, String> getProperties() {
		// always has to have a value because it is then passed by reference to the maincontroller and the updates to it can be seen
		if (properties == null) {
			properties = new LinkedHashMap<String, String>();
			properties.put("url", null);
		}
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
