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

package nabu.frameworks.datastore.types;

import java.net.URI;
import java.util.Date;

import be.nabu.libs.datastore.api.DataProperties;

public class ResourceDescriptor implements DataProperties {
	private URI uri;
	private Long size;
	private String name, contentType, entry;
	private Date lastModified;
	
	public static ResourceDescriptor create(String entry, URI uri, DataProperties properties) {
		ResourceDescriptor descriptor = new ResourceDescriptor();
		descriptor.setEntry(entry);
		descriptor.setUri(uri);
		descriptor.setContentType(properties.getContentType());
		descriptor.setLastModified(properties.getLastModified());
		descriptor.setName(properties.getName());
		descriptor.setSize(properties.getSize());
		return descriptor;
	}
	@Override
	public Long getSize() {
		return size;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public Date getLastModified() {
		return lastModified;
	}

	public URI getUri() {
		return uri;
	}

	public void setUri(URI uri) {
		this.uri = uri;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	
}