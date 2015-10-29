package be.nabu.eai.artifacts.datastore.urn;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "urnProvider")
@XmlType(propOrder = { "urnCreator", "urnResolver" })
public class URNProviderConfiguration {
	
	private DefinedService urnCreator, urnResolver;

	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.URNManager.map")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getUrnCreator() {
		return urnCreator;
	}
	public void setUrnCreator(DefinedService urnCreator) {
		this.urnCreator = urnCreator;
	}

	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.URNManager.resolve")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getUrnResolver() {
		return urnResolver;
	}
	public void setUrnResolver(DefinedService urnResolver) {
		this.urnResolver = urnResolver;
	}
}
