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
import java.util.List;

public class ZIPResult {
	private URI uri;
	private List<ResourceDescriptor> descriptors;
	
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public List<ResourceDescriptor> getDescriptors() {
		return descriptors;
	}
	public void setDescriptors(List<ResourceDescriptor> descriptors) {
		this.descriptors = descriptors;
	}
	
}
