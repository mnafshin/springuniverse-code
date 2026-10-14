# Module 0: Foundations

Plain Java, no Spring yet. These are the ideas Spring is built on.

| Video | Package | Idea | Article |
|---|---|---|---|
| F01 Library or framework? | `ioc` | You call a library (`LibraryExample`); a framework calls you (`MiniFramework`) | §1 |
| F02 How the web works | `clientserver` | `HttpClient` (client) fetches JSON from a local `HelloHttpServer` (server) | §2 |
| F03 HTTP request as a letter | `http` | JDK `HttpServer`: method, path, status codes, JSON body | §3 |
| F04 REST is rules | `rest` | REST-style CRUD on in-memory books (`/books`, `/books/{id}`) | §4 |
| F05 JSON | `json` | Jackson: objects ↔ JSON; single quotes are invalid | §5 |
| F06 Maven & Gradle | `maven` | What Maven does for this module (`./mvnw verify`) | §6 |
| F07 DI in plain words | `di` | Constructor injection + a hand-made "container" | §7 |
| F08 Java vs the JVM | `jvm` | Bytecode magic `CA FE BA BE`, JVM name, ClassLoader | §8 |

## Test
```bash
./mvnw -pl module-00-foundations verify
```

Further reading: [Inversion of Control (Martin Fowler)](https://martinfowler.com/bliki/InversionOfControl.html) · [MDN: How the web works](https://developer.mozilla.org/en-US/docs/Learn_web_development/Getting_started/Web_standards/How_the_web_works) · [MDN: HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview) · [MDN: REST](https://developer.mozilla.org/en-US/docs/Glossary/REST) · [MDN: JSON](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON) · [What is Maven?](https://maven.apache.org/what-is-maven.html) · [Spring IoC container](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html) · [dev.java: The JVM](https://dev.java/learn/jvm/)
