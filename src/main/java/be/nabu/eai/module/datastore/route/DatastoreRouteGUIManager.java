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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.module.datastore.provider.DatastoreProviderArtifact;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;
import be.nabu.libs.types.TypeUtils;
import be.nabu.libs.types.api.ComplexType;
import be.nabu.libs.types.api.DefinedType;
import be.nabu.libs.types.api.Element;

public class DatastoreRouteGUIManager extends BaseJAXBGUIManager<DatastoreRouteConfiguration, DatastoreRouteArtifact> {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public DatastoreRouteGUIManager() {
		super("Datastore Route", DatastoreRouteArtifact.class, new DatastoreRouteManager(), DatastoreRouteConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected DatastoreRouteArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new DatastoreRouteArtifact(entry.getId(), entry.getContainer(), entry.getRepository());
	}

	/**
	 * If we set a new value for the datastore provider, we wipe the current map and set empty values for all existing fields
	 */
	@Override
	public <V> void setValue(DatastoreRouteArtifact instance, Property<V> property, V value) {
		if ("datastoreProvider".equals(property.getName())) {
			// for the default provider you can only set an URI property (it uses the resource datastore in the background)
			Map<String, String> properties = getConfiguration(instance).getProperties();
			if (properties == null) {
				properties = new LinkedHashMap<String, String>();
			}
			else {
				properties.clear();
			}
			if (value == null) {
				properties.put("url", null);
			}
			else {
				DatastoreProviderArtifact provider = (DatastoreProviderArtifact) value;
				try {
					DefinedType configurationType = provider.getConfiguration().getConfigurationType();
					if (configurationType instanceof ComplexType) {
						Map<String, String> currentProperties = getConfiguration(instance).getProperties();
						for (Element<?> element : TypeUtils.getAllChildren((ComplexType) configurationType)) {
							properties.put(element.getName(), currentProperties == null ? null : currentProperties.get(element.getName()));
						}
					}
				}
				catch (IOException e) {
					logger.error("Could not load properties for: " + provider.getId(), e);
				}
			}
			getConfiguration(instance).setProperties(properties);
		}
		// we don't want to update the properties as the map has to stay the same (reference-wise)
		if (!"properties".equals(property.getName())) {
			super.setValue(instance, property, value);
		}
		// we can however merge it
		else if (value instanceof Map) {
			getConfiguration(instance).getProperties().putAll(((Map<? extends String, ? extends String>) value));
		}
	}

	@Override
	public String getCategory() {
		return "Datastore";
	}
}
