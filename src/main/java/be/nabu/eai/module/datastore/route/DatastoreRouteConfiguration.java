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

import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.EnvironmentSpecific;
import be.nabu.eai.module.datastore.provider.DatastoreProviderArtifact;
import be.nabu.eai.module.datastore.urn.URNProviderArtifact;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.eai.repository.util.KeyValueMapAdapter;

@XmlRootElement(name = "datastoreRoute")
@XmlType(propOrder = { "context", "urnProvider", "datastoreProvider", "properties" })
public class DatastoreRouteConfiguration {
	
	private String context;
	private DatastoreProviderArtifact datastoreProvider;
	private URNProviderArtifact urnProvider;
	private Map<String, String> properties;
	
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	@EnvironmentSpecific
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DatastoreProviderArtifact getDatastoreProvider() {
		return datastoreProvider;
	}
	public void setDatastoreProvider(DatastoreProviderArtifact datastoreProvider) {
		this.datastoreProvider = datastoreProvider;
	}
	@EnvironmentSpecific
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public URNProviderArtifact getUrnProvider() {
		return urnProvider;
	}
	public void setUrnProvider(URNProviderArtifact urnProvider) {
		this.urnProvider = urnProvider;
	}
	@EnvironmentSpecific
	@XmlJavaTypeAdapter(value = KeyValueMapAdapter.class)
	public Map<String, String> getProperties() {
		// always has to have a value because it is then passed by reference to the maincontroller and the updates to it can be seen
		if (properties == null) {
			properties = new LinkedHashMap<String, String>();
			properties.put("url", null);
		}
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
}
