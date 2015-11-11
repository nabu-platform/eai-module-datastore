package be.nabu.eai.module.datastore.provider;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class DatastoreProviderGUIManager extends BaseJAXBGUIManager<DatastoreProviderConfiguration, DatastoreProviderArtifact> {

	public DatastoreProviderGUIManager() {
		super("Datastore Provider", DatastoreProviderArtifact.class, new DatastoreProviderManager(), DatastoreProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected DatastoreProviderArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new DatastoreProviderArtifact(entry.getId(), entry.getContainer());
	}

	@Override
	public String getCategory() {
		return "Datastore";
	}
}
