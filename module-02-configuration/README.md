# Module 2: `@Configuration`, `@Bean` and the `ApplicationContext`

The signup service from T2.1, now with a Spring container you create yourself: Java configuration classes,
`@Bean` methods, component scanning, two configuration classes joined with `@Import`, and the same thing in Spring Boot.

| Video | What it shows |
|---|---|
| T2.2 `@Configuration`, `@Bean` and the ApplicationContext (tutorial) | `new AnnotationConfigApplicationContext(AppConfig.class)`, a `@Bean` for a class you can't annotate, bean names, why `@Configuration` methods run once (CGLIB) and the lite-mode trap, the component-scan base package, `getBean` and friends, `@Import`, and where all of it hides in `@SpringBootApplication` |
| Short 012 | `@Component` vs `@Bean`: when to use which |
| Short 013 | This `@Bean` method runs once, even if you call it twice |
| Short 014 | Bean not found? Check the package |

Watch first: T2.1, IoC and DI from first principles (`module-02-di`).
Videos: https://www.youtube.com/@springuniverse

## Package `config.app` (Spring Framework only, no Boot)
| File | Role |
|---|---|
| `AppConfig` | `@Configuration @ComponentScan @Import(PersistenceConfig.class)`, plus `@Bean Clock clock()` |
| `PersistenceConfig` | a second `@Configuration`: `@Bean auditLog()` and `@Bean userRepository()`, which calls `auditLog()` |
| `EmailNotifier`, `SignupService` | `@Component` / `@Service`, found by component scanning |
| `AuditLog` | prints `AuditLog created` in its constructor, so you can count instances |
| `InMemoryUserRepository`, `UserRepository`, `Notifier` | plain classes and interfaces, no annotations |
| `ContainerApp` | creates the container, calls `register("Ada")`, prints every bean name and type |

## Package `config.lite` (the trap)
| File | Role |
|---|---|
| `LiteConfig` | the same `@Bean` methods with `@Configuration(proxyBeanMethods = false)`: `auditLog()` is now a plain call, so a second `AuditLog` appears |
| `LiteFixedConfig` | the fix for every mode: `userRepository(AuditLog auditLog)`, the dependency as a method parameter |
| `LiteApp` | prints `AuditLog created` twice |

## Package `config.outside` and `config.boot`
| File | Role |
|---|---|
| `outside.SmsGateway` | a `@Component` that is never a bean: no configuration scans `config.outside` |
| `boot.BootApp` | `@SpringBootApplication @Import(AppConfig.class)`; `SpringApplication.run` returns the `ApplicationContext` |

## Run
```bash
./mvnw -pl module-02-configuration -am -q install -DskipTests
cd module-02-configuration
../mvnw -q dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.config.app.ContainerApp
# AuditLog created
# EMAIL to Ada: Welcome, Ada! (signed up 2026-10-24T13:47:32.704591267Z)
# bean: appConfig -> AppConfig$$SpringCGLIB$$0
# bean: emailNotifier -> EmailNotifier
# bean: persistenceConfig -> PersistenceConfig$$SpringCGLIB$$0
# bean: signupService -> SignupService
# bean: auditLog -> AuditLog
# bean: userRepository -> InMemoryUserRepository
# bean: clock -> SystemClock

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.config.lite.LiteApp
# AuditLog created
# AuditLog created
# beans of type AuditLog: 1

../mvnw -q spring-boot:run -Dspring-boot.run.main-class=info.mnafshin.springuniverse.config.boot.BootApp
```

## Test
```bash
./mvnw -pl module-02-configuration -am verify
```
| Test | Checks |
|---|---|
| `AppConfigTest` | the context wires scanned and `@Bean` beans; bean name = method name; `AppConfig` is a CGLIB subclass; one `AuditLog` even though `auditLog()` is called twice; one config class alone only knows its own beans |
| `LiteConfigTest` | lite mode gives a second, unmanaged `AuditLog`; the method-parameter version doesn't |
| `ComponentScanTest` | `SmsGateway` → `NoSuchBeanDefinitionException`; `register` + `scan("...outside")` + `refresh` finds it |
| `BootAppTest` | `@SpringBootApplication` is a `@Configuration`, imports `AppConfig`, and doesn't scan `config.outside` |
| `SignupServiceTest` | plain JUnit with `Clock.fixed`, no container |

Docs: [Basic concepts: @Bean and @Configuration](https://docs.spring.io/spring-framework/reference/core/beans/java/basic-concepts.html) · [AnnotationConfigApplicationContext](https://docs.spring.io/spring-framework/reference/core/beans/java/instantiating-container.html) · [Using @Bean](https://docs.spring.io/spring-framework/reference/core/beans/java/bean-annotation.html) · [Using @Configuration](https://docs.spring.io/spring-framework/reference/core/beans/java/configuration-annotation.html) · [Composing configuration classes](https://docs.spring.io/spring-framework/reference/core/beans/java/composing-configuration-classes.html) · [Classpath scanning](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html) · [Using @SpringBootApplication](https://docs.spring.io/spring-boot/reference/using/using-the-springbootapplication-annotation.html)
