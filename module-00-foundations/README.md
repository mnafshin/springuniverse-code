# Module 0: Foundations

Plain Java, no Spring yet. These are the ideas Spring is built on.

| Package | Idea | Run | Video |
|---|---|---|---|
| `ioc` | **Library vs framework**: you call a library (`LibraryExample`); a framework calls you (`MiniFramework`, a tiny stand-in for Spring MVC's dispatcher) | `MiniFramework.main` | F01 "Library or framework?" |
| `di` | **Dependency injection in plain words**: `GreetingService` receives its `MessageSource` through the constructor; `ManualWiring` is a hand-made "container" | `ManualWiring.main` | F07 |
| `http` | **HTTP + JSON**: JDK `HttpServer`, `GET /hello?name=Ada` returns `{"message":"Hello, Ada!","length":11}`, other methods return `405` | `HelloHttpServer.main`, then `curl 'http://localhost:8080/hello?name=Ada'` | F03, F05 |

## Test
```bash
./mvnw -pl module-00-foundations verify
```

Further reading: [Inversion of Control (Martin Fowler)](https://martinfowler.com/bliki/InversionOfControl.html) · [MDN: HTTP overview](https://developer.mozilla.org/en-US/docs/Web/HTTP/Guides/Overview) · [MDN: JSON](https://developer.mozilla.org/en-US/docs/Learn_web_development/Core/Scripting/JSON) · [Spring IoC container](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html)
