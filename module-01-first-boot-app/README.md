# Module 1: First Spring Boot app (Boot 4)

Your first Spring Boot 4 app: generated on [start.spring.io](https://start.spring.io) (Maven, Java 25, Spring Boot 4.1.1, dependency **Spring Web**), plus one `@RestController` that returns a record as JSON.

| Video | What it shows |
|---|---|
| T1.1 Spring Boot 4 from zero (tutorial) | start.spring.io, project tour, `@SpringBootApplication`, run, first `@RestController`, executable jar |
| Short: Your first Spring Boot 4 app in 30 seconds | the whole flow, fast |
| Short: What `@SpringBootApplication` actually does | 3 annotations in 1; why a controller in a sibling package gives 404 |
| Short: Spring Boot 4 needs which Java? | minimum 17, up to 26; use 25 (LTS) |
| Short: Every file in a new Spring Boot project | the generated layout |

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

The `pom.xml` is the generated one, except that its parent is this repo's aggregator (which itself extends `spring-boot-starter-parent` 4.1.1).

## Run
```bash
./mvnw -pl module-01-first-boot-app spring-boot:run
curl localhost:8080/hello              # {"message":"Hello, World!"}
curl 'localhost:8080/hello?name=Ada'   # {"message":"Hello, Ada!"}
```

## Test and package
```bash
./mvnw -pl module-01-first-boot-app verify
java -jar module-01-first-boot-app/target/module-01-first-boot-app-0.1.0-SNAPSHOT.jar
```

Docs: [Developing your first Spring Boot application](https://docs.spring.io/spring-boot/tutorial/first-application/index.html) · [System requirements](https://docs.spring.io/spring-boot/system-requirements.html) · [Using @SpringBootApplication](https://docs.spring.io/spring-boot/reference/using/using-the-springbootapplication-annotation.html) · [Structuring your code](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html)
