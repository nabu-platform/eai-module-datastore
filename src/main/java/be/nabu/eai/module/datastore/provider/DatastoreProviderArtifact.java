package be.nabu.eai.module.datastore.provider;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class DatastoreProviderArtifact extends JAXBArtifact<DatastoreProviderConfiguration> {

	public DatastoreProviderArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, repository, "datastore-provider.xml", DatastoreProviderConfiguration.class);
	}

}
