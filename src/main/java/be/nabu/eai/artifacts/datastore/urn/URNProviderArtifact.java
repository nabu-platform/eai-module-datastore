package be.nabu.eai.artifacts.datastore.urn;

import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class URNProviderArtifact extends JAXBArtifact<URNProviderConfiguration> {

	public URNProviderArtifact(String id, ResourceContainer<?> directory) {
		super(id, directory, "urn-provider.xml", URNProviderConfiguration.class);
	}

}
