# Module 5: a shared library, before it is a starter (T5.3 cold open)

The same `Greeter` and `DefaultGreeter` as `module-05-greeting-starter`, but registered by a plain `@Configuration` class, `GreetingConfig`.
An app whose package is `greetingapp` never scans `greeting`, so the bean is never created:

```bash
./mvnw -q -pl module-05-greeting-app -am package -DskipTests -Plib
# run GreetingApp with this jar instead of the starter:
# APPLICATION FAILED TO START ... required a bean of type 'info.mnafshin.springuniverse.greeting.Greeter' that could not be found.
```

The fix is `module-05-greeting-starter`: `@AutoConfiguration` plus one line in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
