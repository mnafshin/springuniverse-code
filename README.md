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
