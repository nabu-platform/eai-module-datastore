package be.nabu.eai.module.datastore.urn;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class URNProviderManager extends JAXBArtifactManager<URNProviderConfiguration, URNProviderArtifact> {

	public URNProviderManager() {
		super(URNProviderArtifact.class);
	}

	@Override
	protected URNProviderArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new URNProviderArtifact(id, container, repository);
	}

}
