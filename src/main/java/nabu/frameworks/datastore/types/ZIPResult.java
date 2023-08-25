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
