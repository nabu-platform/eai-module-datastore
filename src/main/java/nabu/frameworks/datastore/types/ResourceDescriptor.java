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