# Datastore

This package adds the datastore framework to the nabu suite.

Datastore is concerned with managing data at rest, a few concepts to be aware of:

- **Context**: a context simply provides datastore with a string that identifies for example the project. It follows a dot-based notation and the most precise match "wins". This allows for very finegrained control over where your data will end up
- **URN**: a globally unique identifier for data, it is decoupled from its physical location
- **URL**: a locally unique identifier that points to the physical location of the data
- **URN Manager**: a urn manager can create new urns for a URL and resolve an existing URN back into its URL
- **Datastore Provider**: a datastore provider has the capacity to store and retrieve data. Upon retrieval it is the scheme of the URL that determines which provider is used to resolve it.
- **Datastore Route**: a route dictates which context is bound to which provider (with optionally additional parameters) and which URN manager

## Custom Datastore Providers

Datastore ships with a default provider that uses a virtual file system to store files using any protocol that is supported.

However in some cases you want custom datastore providers, you need to implement these interfaces to be able to create a datastore provider artifact:

- **be.nabu.libs.datastore.api.Datastore.retrieve**: This retrieves data given a URL
- **be.nabu.libs.datastore.api.Datastore.getProperties**: This retrieves metadata about the URL
- **be.nabu.libs.datastore.api.WritableDatastore.store**: This service is called to store new data

## Custom URN Providers

Even though we strongly suggest that you use URNs instead of URLs to reference your data, this package does not come with a default URN provider. This is mostly because the requirements are too usecase-specific to generalize.

To implement your own urn provider, you need to implement these interfaces:

- **be.nabu.libs.datastore.api.ContextualURNManager.map**: This is a context-aware URN generator. Note that the urn manager is called _after_ the data has been stored successfully, as such you can use the properties service to get even more metadata if required
- **be.nabu.libs.datastore.api.URNManager.resolve**: This allows the code to resolve a URN back into the original URL