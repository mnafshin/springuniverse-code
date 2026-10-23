# Module 1: First Spring Boot app (Boot 4)

Your first Spring Boot 4 app: generated on [start.spring.io](https://start.spring.io) (Maven, Java 25, Spring Boot 4.1.1 at the time, dependency **Spring Web**; now on 4.1.2), plus one `@RestController` that returns a record as JSON.

| Video | What it shows |
|---|---|
| T1.1 Spring Boot 4 from zero (tutorial) | start.spring.io, project tour, `@SpringBootApplication`, run, first `@RestController`, executable jar |
| Short: Your first Spring Boot 4 app in 30 seconds | the whole flow, fast |
| Short: What `@SpringBootApplication` actually does | 3 annotations in 1; why a controller in a sibling package gives 404 |
| Short: Spring Boot 4 needs which Java? | minimum 17, up to 26; use 25 (LTS) |
| Short: Every file in a new Spring Boot project | the generated layout |
| T1.2 Starters, dependency management and the Boot BOM (tutorial) | why the `pom.xml` has no versions, starters, the BOM, overriding one version, upgrading 4.1.1 to 4.1.2, virtual threads |
| Shorts 005-008 | no versions; what a starter is; override one version; virtual threads with one property |

Videos: https://www.youtube.com/@springuniverse

## What's inside
| File | Role |
|---|---|
| `HelloApplication` | `@SpringBootApplication` main class (as generated) |
| `HelloController` | `GET /hello?name=Ada` → `{"message":"Hello, Ada!"}` (`name` defaults to `World`) |
| `Greeting` | the response record, written as JSON by Jackson 3 |
| `HelloApplicationTests` | the generated `@SpringBootTest` context test |
| `HelloControllerTest` | `@WebMvcTest` + `MockMvcTester`: 200 + JSON, default name, 405 for POST, 404 for unknown paths |
| `HelloEndToEndTest` | real embedded Tomcat on a random port, called with `RestTestClient` |
| `ThreadController` (T1.2) | `GET /thread` → which thread served the request, e.g. `{"thread":"VirtualThread[#46,tomcat-handler-0]/runnable@ForkJoinPool-1-worker-1","virtual":true}` |
| `ManagedVersionsTest` (T1.2) | Spring Framework, Tomcat and Jackson on the classpath are exactly the versions the BOM properties name |
| `VirtualThreadsTest` / `PlatformThreadsTest` (T1.2) | with `spring.threads.virtual.enabled=true` requests run on a virtual thread; with it set to `false` they run on a Tomcat `http-nio-…-exec-…` platform thread |

The `pom.xml` is the generated one, except that its parent is this repo's aggregator (which itself extends `spring-boot-starter-parent`, now 4.1.2) and a surefire setting that hands three BOM version properties to `ManagedVersionsTest`.

## T1.2: where the versions come from
- No dependency in `pom.xml` has a version. `spring-boot-starter-parent` inherits from `spring-boot-dependencies`, the Spring Boot BOM, which manages the versions.
- See the whole tree: `./mvnw -pl module-01-first-boot-app dependency:tree`
- Override one managed version with its property, e.g. `-Dtomcat.version=11.0.27` or a `<tomcat.version>` entry in `<properties>`. Prefer upgrading Spring Boot itself (4.1.1 → 4.1.2 changes 55 managed versions in one go).
- Virtual threads: `spring.threads.virtual.enabled=true` in `application.properties` (Java 21+; the docs strongly recommend Java 24+). Thread-pool properties then have no effect.
- No parent POM allowed? See [module-01-bom-import](../module-01-bom-import).

## Run
```bash
./mvnw -pl module-01-first-boot-app spring-boot:run
curl localhost:8080/hello              # {"message":"Hello, World!"}
curl 'localhost:8080/hello?name=Ada'   # {"message":"Hello, Ada!"}
curl localhost:8080/thread             # {"thread":"VirtualThread[...]","virtual":true}
```

## Test and package
```bash
./mvnw -pl module-01-first-boot-app verify
java -jar module-01-first-boot-app/target/module-01-first-boot-app-0.1.0-SNAPSHOT.jar
```

Docs: [Developing your first Spring Boot application](https://docs.spring.io/spring-boot/tutorial/first-application/index.html) · [System requirements](https://docs.spring.io/spring-boot/system-requirements.html) · [Using @SpringBootApplication](https://docs.spring.io/spring-boot/reference/using/using-the-springbootapplication-annotation.html) · [Structuring your code](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html) · [Build systems and starters](https://docs.spring.io/spring-boot/reference/using/build-systems.html) · [Dependency versions](https://docs.spring.io/spring-boot/appendix/dependency-versions/properties.html) · [Virtual threads](https://docs.spring.io/spring-boot/reference/features/spring-application.html)
