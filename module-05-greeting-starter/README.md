# Module 5: a starter of our own (T5.2)

A library jar that auto-configures a `Greeter`, written the way Spring Boot writes its own modules. The app that uses it is `module-05-greeting-app`.

| File | Role |
|---|---|
| `greeting/Greeter`, `greeting/DefaultGreeter` | what the starter offers, and its default ("Hello, Ada!") |
| `greeting/GreetingAutoConfiguration` | `@AutoConfiguration`, on unless `greeting.enabled=false`; its `@Bean` is `@ConditionalOnMissingBean`, so a `Greeter` of the app's own wins |
| `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` | one line: the class name. Without it, the app fails to start: `required a bean of type 'info.mnafshin.springuniverse.greeting.Greeter' that could not be found` (delete the line and run `GreetingApp` to see it) |
| `GreetingAutoConfigurationTest` (test) | `ApplicationContextRunner` with `withConfiguration(AutoConfigurations.of(...))`: the default, backing off, the property switch (3 tests) |
| `GreetingWrongSetupTest` (test, tag `trap`) | fails on purpose: the auto-configuration registered with `withUserConfiguration(...)` before the user's config, so `@ConditionalOnMissingBean` checks too early and finds `["greeter", "myGreeter"]` |

```bash
./mvnw -pl module-05-greeting-starter test            # Tests run: 3, Failures: 0
./mvnw -pl module-05-greeting-starter test -Ptrap     # Tests run: 1, Failures: 1 (on purpose)
```

Docs: [Creating your own auto-configuration](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html)
