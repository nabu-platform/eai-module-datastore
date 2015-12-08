package be.nabu.eai.module.datastore.urn;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class URNProviderArtifact extends JAXBArtifact<URNProviderConfiguration> {

	public URNProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "urn-provider.xml", URNProviderConfiguration.class);
	}

}
