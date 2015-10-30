package be.nabu.eai.module.datastore.provider;

import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class DatastoreProviderArtifact extends JAXBArtifact<DatastoreProviderConfiguration> {

	public DatastoreProviderArtifact(String id, ResourceContainer<?> directory) {
		super(id, directory, "datastore-provider.xml", DatastoreProviderConfiguration.class);
	}

}
