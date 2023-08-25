package nabu.frameworks.datastore.types;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "descriptor")
public class ResourceDescriptors {
	private List<ResourceDescriptor> resources = new ArrayList<ResourceDescriptor>();

	public List<ResourceDescriptor> getResources() {
		return resources;
	}
	public void setResources(List<ResourceDescriptor> resources) {
		this.resources = resources;
	}
}