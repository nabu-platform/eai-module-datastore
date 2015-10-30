package be.nabu.eai.artifacts.datastore.route;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBException;

import be.nabu.eai.repository.api.Repository;
import be.nabu.eai.repository.artifacts.jaxb.JAXBArtifact;
import be.nabu.libs.artifacts.api.StartableArtifact;
import be.nabu.libs.artifacts.api.StoppableArtifact;
import be.nabu.libs.resources.api.ResourceContainer;

public class DatastoreRouteArtifact extends JAXBArtifact<DatastoreRouteConfiguration> implements StartableArtifact, StoppableArtifact {

	private boolean enabled;
	
	public DatastoreRouteArtifact(String id, ResourceContainer<?> directory, Repository repository) {
		super(id, directory, "datastore-route.xml", DatastoreRouteConfiguration.class);
	}

	@Override
	public void start() throws IOException {
		enabled = true;
		reloadConfiguration();
	}
	
	@Override
	public void stop() throws IOException {
		enabled = false;
		reloadConfiguration();		
	}

	private void reloadConfiguration() {
		nabu.services.Datastore.refresh();
	}

	@Override
	public void marshal(DatastoreRouteConfiguration configuration, OutputStream output) throws JAXBException {
		super.marshal(configuration, output);
	}

	@Override
	public boolean isStarted() {
		return enabled;
	}
	
}
