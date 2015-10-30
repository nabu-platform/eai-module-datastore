package be.nabu.eai.modules.datastore.route;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.managers.base.JAXBArtifactManager;
import be.nabu.libs.resources.api.ResourceContainer;

public class DatastoreRouteManager extends JAXBArtifactManager<DatastoreRouteConfiguration, DatastoreRouteArtifact> {

	public DatastoreRouteManager() {
		super(DatastoreRouteArtifact.class);
	}

	@Override
	protected DatastoreRouteArtifact newInstance(String id, ResourceContainer<?> container, Repository repository) {
		return new DatastoreRouteArtifact(id, container, repository);
	}

}
