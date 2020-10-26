### What is dependency
Collaboration among objects/beans to achieve a goal.

### Why it is called Inverse of Control
This process is fundamentally the inverse (hence the name, Inversion of Control) of the bean itself controlling the instantiation or location of its dependencies on its own by using direct construction of classes or the Service Locator pattern.

### Constructor-based Dependency Injection
- accomplished by the container invoking a constructor with a number of arguments, each representing a dependency

### Setter-based Dependency Injection
- accomplished by the container calling setter methods on your beans after invoking a no-argument constructor or a no-argument static factory method to instantiate your bean
- it makes objects of that class amenable to reconfiguration or re-injection later

### Dependency Resolution Process
- The **ApplicationContext** is created and initialized with configuration metadata that describes all the beans.
- For each **bean**, its dependencies are expressed in the form of properties/constructor arguments/arguments to the static-factory method. These dependencies are provided to the bean, when the bean is actually created.
- Each **property or constructor argument** is an actual definition of the value to set, or a reference to another bean in the container.

