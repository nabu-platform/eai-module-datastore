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

package be.nabu.eai.module.datastore.urn;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import be.nabu.eai.api.InterfaceFilter;
import be.nabu.eai.repository.jaxb.ArtifactXMLAdapter;
import be.nabu.libs.services.api.DefinedService;

@XmlRootElement(name = "urnProvider")
@XmlType(propOrder = { "urnCreator", "urnResolver" })
public class URNProviderConfiguration {
	
	private DefinedService urnCreator, urnResolver;

	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.URNManager.map")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getUrnCreator() {
		return urnCreator;
	}
	public void setUrnCreator(DefinedService urnCreator) {
		this.urnCreator = urnCreator;
	}

	@InterfaceFilter(implement = "be.nabu.libs.datastore.api.URNManager.resolve")
	@XmlJavaTypeAdapter(value = ArtifactXMLAdapter.class)
	public DefinedService getUrnResolver() {
		return urnResolver;
	}
	public void setUrnResolver(DefinedService urnResolver) {
		this.urnResolver = urnResolver;
	}
}
