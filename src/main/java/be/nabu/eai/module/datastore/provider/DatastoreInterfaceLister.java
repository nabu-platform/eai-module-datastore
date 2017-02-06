package be.nabu.eai.module.datastore.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.nabu.eai.developer.api.InterfaceLister;
import be.nabu.eai.developer.util.InterfaceDescriptionImpl;

public class DatastoreInterfaceLister implements InterfaceLister {
	
	private static Collection<InterfaceDescription> descriptions = null;
	
	@Override
	public Collection<InterfaceDescription> getInterfaces() {
		if (descriptions == null) {
			synchronized(DatastoreInterfaceLister.class) {
				if (descriptions == null) {
					List<InterfaceDescription> descriptions = new ArrayList<InterfaceDescription>();
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "Properties Provider", "be.nabu.libs.datastore.api.Datastore.getProperties"));
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "Retrieve Provider", "be.nabu.libs.datastore.api.Datastore.retrieve"));
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "Store Provider", "be.nabu.libs.datastore.api.WritableDatastore.store"));
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "Delete Provider", "be.nabu.libs.datastore.api.DeletableDatastore.delete"));
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "URN Creator", "be.nabu.libs.datastore.api.URNManager.map"));
					descriptions.add(new InterfaceDescriptionImpl("Datastore", "URN Resolver", "be.nabu.libs.datastore.api.URNManager.resolve"));
					DatastoreInterfaceLister.descriptions = descriptions;
				}
			}
		}
		return descriptions;
	}

}
