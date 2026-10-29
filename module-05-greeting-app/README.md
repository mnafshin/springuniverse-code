# Module 5: an app that uses our starter (T5.3)

`GreetingApp` depends on `module-05-greeting-starter` and nothing else of ours. It never defines a `Greeter`, and its package (`greetingapp`) is not the starter's (`greeting`), so component scanning never sees the starter's classes. The greeter comes from the starter's `AutoConfiguration.imports` file.

```bash
./mvnw -q -pl module-05-greeting-app -am package -DskipTests
java -jar module-05-greeting-app/target/module-05-greeting-app-0.1.0-SNAPSHOT.jar
# Hello, Ada!
java -jar module-05-greeting-app/target/module-05-greeting-app-0.1.0-SNAPSHOT.jar --greeting.enabled=false
# APPLICATION FAILED TO START ... required a bean of type 'info.mnafshin.springuniverse.greeting.Greeter' that could not be found.
```

## Two more runs from T5.3

- **The library before it was an auto-configuration:** build with `-Plib` (the app then depends on `module-05-greeting-lib`, a plain `@Configuration`), and the app fails with the same `required a bean of type ... Greeter that could not be found`.
- **An app with its own greeter:** `ownapp.GreetingApp` defines `@Bean Greeter myGreeter()` ("Hi Ada"). The starter's `greeter()` backs off; with `--debug` the report says `GreetingAutoConfiguration#greeter: Did not match: ... found beans of type '...Greeter' myGreeter`. The jar's main class is `greetingapp.GreetingApp`, so run this one from the classpath:

```bash
./mvnw -q dependency:build-classpath -pl module-05-greeting-app -Dmdep.outputFile=/tmp/cp.txt
java -cp module-05-greeting-app/target/classes:$(cat /tmp/cp.txt) info.mnafshin.springuniverse.ownapp.GreetingApp
# Hi Ada
```

`OwnGreeterTest` checks the same thing (a single `Greeter`, named `myGreeter`).
