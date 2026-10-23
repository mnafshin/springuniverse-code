# Module 1: Spring Boot BOM without the parent (T1.2)

Some companies give every project their own parent POM, so you can't use `spring-boot-starter-parent`. You still get Spring Boot's dependency management by **importing the BOM**, `spring-boot-dependencies`, in `<dependencyManagement>` with `<type>pom</type>` and `<scope>import</scope>`.

| Video | What it shows |
|---|---|
| T1.2 Starters, dependency management and the Boot BOM (tutorial), chapter "No parent? Import the BOM" | the import typed live; what you keep and what you lose |
| Short: Override one Spring Boot dependency version, the right way | properties with the parent; an entry above the import without it |

Videos: https://www.youtube.com/@springuniverse

## What you keep, what you lose
- Keep: every managed version. The dependencies in `pom.xml` still have no versions.
- Lose: the parent's build defaults. This `pom.xml` sets them by hand: Java release and UTF-8 source encoding, `-parameters` for the compiler (Spring MVC uses it to read `@RequestParam` names), plugin versions and the `repackage` goal of `spring-boot-maven-plugin`.
- Overriding a version: properties like `tomcat.version` do **not** work here. Add that dependency with its version to `<dependencyManagement>` **above** the BOM import.

This module deliberately does not inherit from the repo's aggregator POM (which extends the starter parent); it is only listed as a module so CI builds it.

## What's inside
| File | Role |
|---|---|
| `BomImportApplication` | `@SpringBootApplication` main class |
| `HelloController` | `GET /hello?name=Ada` → `{"message":"Hello, Ada!"}` |
| `BomImportApplicationTests` | context loads, and the Spring Boot version on the classpath is the imported BOM's (4.1.2) |
| `HelloControllerTest` | `@WebMvcTest`: the `name` parameter is read (proves `-parameters` is on), default `World` |

## Test and run
```bash
./mvnw -pl module-01-bom-import verify
./mvnw -pl module-01-bom-import spring-boot:run
curl 'localhost:8080/hello?name=Ada'
```

Docs: [Using Spring Boot without the parent POM](https://docs.spring.io/spring-boot/maven-plugin/using.html) · [Maven: importing dependencies](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html) · [Build systems and starters](https://docs.spring.io/spring-boot/reference/using/build-systems.html)
