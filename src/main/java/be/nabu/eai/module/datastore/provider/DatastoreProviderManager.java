package be.nabu.eai.module.datastore.provider;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class DatastoreProviderManager extends JAXBArtifactManager<DatastoreProviderConfiguration, DatastoreProviderArtifact> {

	public DatastoreProviderManager() {
		super(DatastoreProviderArtifact.class);
	}

	@Override
	protected DatastoreProviderArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new DatastoreProviderArtifact(id, container, repository);
	}

}
