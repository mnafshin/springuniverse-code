# Module 2: IoC and dependency injection

The same small signup service, wired twice: first by hand in plain Java, then by the Spring container.

| Video | What it shows |
|---|---|
| T2.1 IoC and DI from first principles: plain Java, then Spring (tutorial) | a hard-wired service, constructor injection, a fake in a plain unit test, the composition root, then the same classes with `@Service`/`@Component` and the two startup errors you will meet |
| Short 009 | Constructor injection wins. Here's why |
| Short 010 | "Required a bean that could not be found", decoded |
| Short 011 | Test it without Spring: the real payoff of dependency injection |

Videos: https://www.youtube.com/@springuniverse

## Package `di.plain` (no Spring at all)
| File | Role |
|---|---|
| `HardWiredSignupService` | Step 1, the problem: builds its own `InMemoryUserRepository` and `EmailNotifier` with `new` |
| `SignupService` | Step 2: asks for a `UserRepository` and a `Notifier` in its constructor |
| `PlainJavaApp` | Step 3, the composition root: `new` everything once, pass it in, call `register("Ada")` |
| `Notifier`, `EmailNotifier`, `UserRepository`, `InMemoryUserRepository` | the collaborators (the "email" only prints) |
| `SignupServiceTest` (test) | plain JUnit with a `FakeNotifier`: no email, no Spring |

## Package `di.spring` (the same classes, plus annotations)
| File | Role |
|---|---|
| `DiApplication` | `@SpringBootApplication` |
| `SignupService` | `@Service`, the same single constructor, no `@Autowired` needed |
| `EmailNotifier`, `InMemoryUserRepository` | `@Component` |
| `SignupController` | `POST /signups?name=Ada` → `{"user":"Ada","message":"Welcome, Ada!"}` |
| `SpringWiringTest` (test) | the container created and wired the beans; one shared instance per bean by default; no `Notifier` bean → startup fails (`NoSuchBeanDefinitionException`); two → `NoUniqueBeanDefinitionException` |
| `SignupServiceUnitTest` (test) | the `@Service` is still a plain class: `new` it in a test |
| `SignupControllerTest` (test) | the whole app on a random port |

## Run
```bash
./mvnw -pl module-02-di -am -q install -DskipTests
java -cp module-02-di/target/classes info.mnafshin.springuniverse.di.plain.PlainJavaApp
# EMAIL to Ada: Welcome, Ada!

./mvnw -pl module-02-di spring-boot:run
curl -X POST 'localhost:8080/signups?name=Ada'   # {"user":"Ada","message":"Welcome, Ada!"}
# app log: EMAIL to Ada: Welcome, Ada!
```

Try it: remove `@Component` from `EmailNotifier` and start the app. It stops with
"Parameter 1 of constructor in ...SignupService required a bean of type '...Notifier' that could not be found."

## Test
```bash
./mvnw -pl module-02-di verify
```

Docs: [The IoC container](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html) · [Dependency injection](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html) · [Using @Autowired](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/autowired.html) · [Spring Beans and dependency injection (Boot)](https://docs.spring.io/spring-boot/reference/using/spring-beans-and-dependency-injection.html)
