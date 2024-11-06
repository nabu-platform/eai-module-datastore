/*
* Copyright (C) 2015 Alexander Verbruggen
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package be.nabu.eai.module.datastore.route;

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
		super(id, directory, repository, "datastore-route.xml", DatastoreRouteConfiguration.class);
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
		nabu.frameworks.datastore.Services.refresh();
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
