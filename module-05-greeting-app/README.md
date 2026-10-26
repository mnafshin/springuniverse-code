# Module 5: an app that uses our starter (T5.2)

`GreetingApp` depends on `module-05-greeting-starter` and nothing else of ours. It never defines a `Greeter`, and its package (`greetingapp`) is not the starter's (`greeting`), so component scanning never sees the starter's classes. The greeter comes from the starter's `AutoConfiguration.imports` file.

```bash
./mvnw -q -pl module-05-greeting-app -am package -DskipTests
java -jar module-05-greeting-app/target/module-05-greeting-app-0.1.0-SNAPSHOT.jar
# Hello, Ada!
java -jar module-05-greeting-app/target/module-05-greeting-app-0.1.0-SNAPSHOT.jar --greeting.enabled=false
# APPLICATION FAILED TO START ... required a bean of type 'info.mnafshin.springuniverse.greeting.Greeter' that could not be found.
```
