# SpringUniverse code

[![ci](https://github.com/mnafshin/springuniverse-code/actions/workflows/ci.yml/badge.svg)](https://github.com/mnafshin/springuniverse-code/actions/workflows/ci.yml)


Companion code for **SpringUniverse**: Java and Spring, explained.

- YouTube: https://www.youtube.com/@springuniverse
- Blog: https://mnafshin.info

One folder per curriculum module. Every example is small, runnable and covered by tests.

| Module | What's inside |
|---|---|
| [module-00-foundations](module-00-foundations) | Library vs framework (Inversion of Control), dependency injection in plain Java, a minimal HTTP + JSON server |

## Requirements
- Java 25 (e.g. Eclipse Temurin)
- No Maven install needed: use the Maven wrapper (`./mvnw`)

Stack: Spring Boot 4.1.1 BOM (manages JUnit 6, AssertJ, Jackson 3).

## Build and test
```bash
./mvnw verify
```

## License
[Apache License 2.0](LICENSE)
