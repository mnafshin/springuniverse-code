# SpringUniverse code

[![ci](https://github.com/mnafshin/springuniverse-code/actions/workflows/ci.yml/badge.svg)](https://github.com/mnafshin/springuniverse-code/actions/workflows/ci.yml)


Companion code for **SpringUniverse**: Java and Spring, explained.

- YouTube: https://www.youtube.com/@springuniverse
- Blog: https://mnafshin.info

One folder per curriculum module. Every example is small, runnable and covered by tests.

| Module | What's inside |
|---|---|
| [module-00-foundations](module-00-foundations) | F01–F08 companions: IoC, client/server, HTTP, REST CRUD, JSON/Jackson, Maven note, DI, JVM/bytecode |
| [module-01-first-boot-app](module-01-first-boot-app) | T1.1: first Spring Boot 4 app from start.spring.io, `@RestController` returning JSON, `@WebMvcTest` + end-to-end tests. T1.2: no versions in the pom (the Boot BOM), BOM-version tests, virtual threads with one property |
| [module-01-bom-import](module-01-bom-import) | T1.2: the same kind of app without `spring-boot-starter-parent`, importing `spring-boot-dependencies` (scope `import`) |
| [module-02-di](module-02-di) | T2.1: IoC and dependency injection from first principles. A signup service wired by hand in plain Java (hard-wired, then constructor injection, a fake in a plain unit test, the composition root), then by the Spring container (`@Service`/`@Component`, no `@Autowired`), plus the "no bean" and "two beans" startup errors as tests |
| [module-02-configuration](module-02-configuration) | T2.2: `@Configuration`, `@Bean` and the `ApplicationContext`. A container created with `AnnotationConfigApplicationContext`, a `@Bean` for `java.time.Clock`, bean names, the CGLIB proxy that keeps `@Bean` methods singletons (and the lite-mode trap as a test), the component-scan base package, `@Import` of a second config class, and the same in `@SpringBootApplication` |
| [module-03-configuration](module-03-configuration) | T3.1: external configuration. `application.yml`, property sources and precedence (file, environment variable, command line), a validated `@ConfigurationProperties` record with a default, and `@Value` for comparison. T3.2: profiles for dev/test/prod (`application-{profile}.yml` merged key by key, `spring.profiles.active`, the `default` profile, last profile wins, `@Profile("!prod")` bean swap, `@ActiveProfiles` in tests, multi-document YAML with `on-profile`), the prod mail password only from the environment, and a little SpEL (`#{${...} * 8 / 10}`) |
| [module-05-autoconfiguration](module-05-autoconfiguration) | T5.1: how auto-configuration decides. Our own `NotifierAutoConfiguration` registered in `AutoConfiguration.imports`, `@ConditionalOnBooleanProperty` and `@ConditionalOnMissingBean`, the `--debug` conditions report (also read in a test), replacing a default with one bean (our `Notifier`, our `JsonMapper`), `spring.autoconfigure.exclude`, `ApplicationContextRunner` tests, and the candidate list of a modular Boot 4 app (no JDBC module, no JDBC candidates) |

## Requirements
- Java 25 (e.g. Eclipse Temurin)
- No Maven install needed: use the Maven wrapper (`./mvnw`)

Stack: Spring Boot 4.1.2 BOM (manages JUnit 6, AssertJ, Jackson 3).

## Build and test
```bash
./mvnw verify
```

## License
[Apache License 2.0](LICENSE)
