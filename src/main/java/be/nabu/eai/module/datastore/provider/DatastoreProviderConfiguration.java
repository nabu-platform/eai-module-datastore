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

package be.nabu.eai.module.datastore.provider;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;
import be.nabu.libs.types.api.DefinedType;

@XmlRootElement(name = "datastoreProvider")
@XmlType(propOrder = { "scheme", "retrieveService", "propertiesService", "storeService", "deleteService", "configurationType" })
public class DatastoreProviderConfiguration {

	private String scheme;
	private DefinedService retrieveService;
	private DefinedService propertiesService;
	private DefinedService storeService;
	private DefinedService deleteService;
	private DefinedType configurationType;
	
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.Datastore.getProperties")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getPropertiesService() {
		return propertiesService;
	}
	public void setPropertiesService(DefinedService propertiesService) {
		this.propertiesService = propertiesService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.Datastore.retrieve")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getRetrieveService() {
		return retrieveService;
	}
	public void setRetrieveService(DefinedService retrieveService) {
		this.retrieveService = retrieveService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.WritableDatastore.store")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getStoreService() {
		return storeService;
	}
	public void setStoreService(DefinedService storeService) {
		this.storeService = storeService;
	}
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedType getConfigurationType() {
		return configurationType;
	}
	public void setConfigurationType(DefinedType configurationType) {
		this.configurationType = configurationType;
	}
	public String getScheme() {
		return scheme;
	}
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	public DefinedService getDeleteService() {
		return deleteService;
	}
	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.DeletableDatastore.delete")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public void setDeleteService(DefinedService deleteService) {
		this.deleteService = deleteService;
	}
}
