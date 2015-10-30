package be.nabu.eai.module.datastore.urn;

import java.io.IOException;
import java.util.List;

import be.nabu.eai.developer.MainController;
import be.nabu.eai.developer.managers.base.BaseJAXBGUIManager;
import be.nabu.eai.repository.resources.RepositoryEntry;
import be.nabu.libs.property.api.Property;
import be.nabu.libs.property.api.Value;

public class URNProviderGUIManager extends BaseJAXBGUIManager<URNProviderConfiguration, URNProviderArtifact> {

	public URNProviderGUIManager() {
		super("URN Provider", URNProviderArtifact.class, new URNProviderManager(), URNProviderConfiguration.class);
	}

	@Override
	protected List<Property<?>> getCreateProperties() {
		return null;
	}

	@Override
	protected URNProviderArtifact newInstance(MainController controller, RepositoryEntry entry, Value<?>... values) throws IOException {
		return new URNProviderArtifact(entry.getId(), entry.getContainer());
	}

}
