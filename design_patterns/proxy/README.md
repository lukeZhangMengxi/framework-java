## Proxy pattern
The Proxy pattern allows us to create an intermediary that acts as an interface to another resource, while also hiding the underlying complexity of the component.

Consider a heavy Java object (like a JDBC connection or a SessionFactory) that requires some initial configuration.

We only want such objects to be initialized on demand, and once they are, we'd want to reuse them for all calls.
