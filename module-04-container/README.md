# Module 4: Bean lifecycle and container internals

What the Spring container does between "here are my classes" and "the app is ready": bean definitions,
the two kinds of post-processor, init and destroy callbacks, creation order, the proxies behind `@Transactional`,
and why Spring Boot refuses circular dependencies.

| Video | What it shows |
|---|---|
| T4.1 The Spring bean lifecycle (tutorial) | package `container.shop`: one small shop app built around its price service, in six runnable examples (the last one runs the whole fixed shop from start to stop), each problem first, then the fix: a `null` field in the constructor (`@PostConstruct`, constructor injection), when singletons are created and `@Lazy`, the order of the three init and destroy callbacks, cleanup that never runs until the container is closed (try-with-resources, the Spring Boot shutdown hook), and `@DependsOn` for a hidden dependency. The first render used package `container.beanlife` (kept) |
| Bean post-processors (video dropped 2026-10-26, code kept) | package `container.postproc`: six runnable examples: the plan (bean definitions) comes before the beans, a `BeanFactoryPostProcessor` that makes a library bean lazy, the `static @Bean` rule (`Menu for null` vs `Menu for Corner Shop`), a `BeanPostProcessor` that sees every bean, a post-processor that returns a wrapper (ask for the interface, not the class), and the built-in annotation post-processors behind `@Autowired` and `@PostConstruct` |
| T4.2 Proxies and the self-call trap (tutorial) | package `container.checkout`: one small shop checkout app (orders and order lines in an in-memory H2 database), in seven runnable examples, each problem first, then the fix: a `@Transactional` bean is not your class (`OrderService$$SpringCGLIB$$0`), Boot's CGLIB default vs `@Proxyable(INTERFACES)` (ask for the interface, not the class), a `final` method that runs on the proxy (`NullPointerException`), the self-call that leaves a half-saved order (`transaction active = false`, `orders 1, order lines 1`), the fix with a second bean (rolled back: `orders 0, order lines 0`), the same trap with `@Async` (`main` vs `task-1`), and the whole checkout going through three proxies. The first render used package `container.proxies` (kept) |
| T4.3 Circular dependencies (tutorial) | package `container.cycles`: six runnable examples: a constructor cycle and Spring Boot's cycle report, field injection is still refused and `spring.main.allow-circular-references=true` lets it start (early reference) but can't break a constructor cycle, a half-built bean used in `@PostConstruct` (`NullPointerException`), the fix by extracting `CustomerDirectory`, the fix with an application event, and `@Lazy` on an injection point as a last resort (`CustomerService$$SpringCGLIB$$0`) |
| Earlier draft (not a tutorial) | circular dependencies: packages `container.cycle`, `fieldcycle`, `cyclefix` (kept for the unpublished T4.1 v1) |
| Short 025 | Why is my field null in the constructor? (`@PostConstruct` vs the constructor) |
| Short 026 | Why your `@Transactional` self-call does nothing |
| Short 027 | Circular dependency? Spring Boot refuses to start |
| Short 028 | `@Proxyable`: choose the proxy type per bean (Spring Framework 7) |

Watch first: T2.2, `@Configuration`, `@Bean` and the ApplicationContext (`module-02-configuration`).
Videos: https://www.youtube.com/@springuniverse

## Package `container.shop` (T4.1, one shop app; Spring Framework only except `cleanup.BootCleanupApp`)
| Package | Example | Run |
|---|---|---|
| `nullfield` | the shop's `BrokenPriceCache` uses an `@Autowired` `PriceSource` field in its constructor: `source = null`, startup fails. Fix 1 `FixedPriceCache` (`@PostConstruct`), fix 2 `ConstructorPriceCache` (constructor injection) | `NullFieldApp` |
| `whencreated` | `PriceCache` and the slow, rarely used `SalesReport` are created at startup, once (same object twice); `LazySalesReport` (`@Lazy`) is created on first use | `WhenCreatedApp` |
| `callbacks` | `PriceCache` with `@PostConstruct` / `afterPropertiesSet()` / `initMethod` and `@PreDestroy` / `destroy()` / `destroyMethod`; `PriceStore` only has `close()`, which a `@Bean` method infers as its destroy method | `CallbacksApp` |
| `cleanup` | `PriceExport` writes the nightly price export (`prices.csv`) into a real temp folder in `@PostConstruct` and deletes the folder in `@PreDestroy`. `NoCloseApp` never closes the context (no delete), `CloseApp` uses try-with-resources (deleted), `BootCleanupApp` relies on Boot's shutdown hook (Ctrl+C or `kill PID`: deleted; `kill -9 PID`: not deleted) | `NoCloseApp`, `CloseApp`, `BootCleanupApp` |
| `order` | `HiddenDependencyConfig`: `PriceSource` reads a table that `Migrations` creates but doesn't depend on it ("table prices is missing!"); `DependsOnConfig` adds `@DependsOn("migrations")`; shutdown runs in reverse | `OrderApp` |
| `app` | the whole shop app with every fix: `ShopConfig` (`@DependsOn("migrations")`, `@Lazy` `SalesReport`), `PriceCache` (constructor injection + `@PostConstruct`), `PriceExport` (`@PreDestroy`), `ShopApp` closes the context with try-with-resources | `ShopApp` |

## Package `container.beanlife` (first T4.1 render, kept; Spring Framework only except `cleanup.BootCleanupApp`)
| Package | Example | Run |
|---|---|---|
| `nullfield` | `BrokenPriceCache` uses an `@Autowired` field in its constructor: `source = null`, startup fails. Fix 1 `FixedPriceCache` (`@PostConstruct`), fix 2 `ConstructorPriceCache` (constructor injection) | `NullFieldApp` |
| `whencreated` | `Menu` and `ReportJob` are created at startup, once (same object twice); `LazyReportJob` (`@Lazy`) is created on first use | `WhenCreatedApp` |
| `callbacks` | `PriceCache` with `@PostConstruct` / `afterPropertiesSet()` / `initMethod` and `@PreDestroy` / `destroy()` / `destroyMethod`; `PriceStore` only has `close()`, which a `@Bean` method infers as its destroy method | `CallbacksApp` |
| `cleanup` | `TempFiles` creates a real temp folder in `@PostConstruct` and deletes it in `@PreDestroy`. `NoCloseApp` never closes the context (no delete), `CloseApp` uses try-with-resources (deleted), `BootCleanupApp` relies on Boot's shutdown hook (stop it with Ctrl+C or `kill PID`: deleted; `kill -9 PID`: not deleted) | `NoCloseApp`, `CloseApp`, `BootCleanupApp` |
| `order` | `HiddenDependencyConfig`: `PriceSource` reads a table that `Migrations` creates but doesn't depend on it ("table prices is missing!"); `DependsOnConfig` adds `@DependsOn("migrations")`; shutdown runs in reverse | `OrderApp` |

## Package `container.postproc` (post-processors video, dropped; Spring Framework only, no Boot)
| Package | Example | Run |
|---|---|---|
| `plan` | after `register()` the container only holds bean definitions (class, scope, lazy); `Menu` is created at `refresh()`, the `@Lazy` `ReportJob` stays a recipe | `PlanApp` |
| `lazyplan` | `ReportJob` (a "library" class, no `@Lazy`) is slow and created at startup (`ShopConfig`); `LazyShopConfig` adds a `static @Bean MakeReportJobLazy` (`BeanFactoryPostProcessor`) that sets `lazyInit` on its definition | `LazyPlanApp` |
| `staticrule` | `BrokenConfig` declares a `BeanFactoryPostProcessor` with a non-static `@Bean` method: Spring logs an INFO from `ConfigurationClassEnhancer` and `@Value` is never injected (`Menu for null`); `FixedConfig` makes the method `static` (`Menu for Corner Shop`) | `StaticRuleApp` |
| `tracing` | `TracingPostProcessor` (`BeanPostProcessor`) prints before and after init for every bean; `@PostConstruct` runs between the two calls | `TracingApp` |
| `wrapper` | `LoggingPostProcessor` returns a JDK proxy around every `PriceService`; `getBean(SimplePriceService.class)` fails with `NoSuchBeanDefinitionException`, `getBean(PriceService.class)` works and every call goes through the wrapper | `WrapperApp` |
| `builtins` | a bare `GenericApplicationContext`: `@Autowired` and `@PostConstruct` are ignored (`source = null`) until `AnnotationConfigUtils.registerAnnotationConfigProcessors(context)` adds Spring's own post-processors | `BuiltinsApp` |

## Package `container.checkout` (T4.2, one shop checkout app; Spring Boot, JDBC + in-memory H2)
`OrderTables` (imported with `@Import(OrderTables.class)`) creates the tables `orders` and `order_lines` and prints what is really in the database. `PriceTable` knows coffee (3) and cake (4); a muffin has no price, so ordering one fails halfway through saving.

| Package | Example | Run |
|---|---|---|
| `whatis` | `ProductCatalog` (plain) is its own class; `OrderService` has a `@Transactional` method, so the bean is `OrderService$$SpringCGLIB$$0`, a subclass in front of the real object (`AopUtils.isAopProxy`, `AopUtils.getTargetClass`) | `WhatIsApp` |
| `kinds` | `ShippingService` gets Boot's default CGLIB subclass (still a `ShippingService`); `PaymentService` has `@Proxyable(INTERFACES)` (Spring Framework 7) and gets a JDK proxy that only implements `Payments`: `getBean(PaymentService.class)` fails with `NoSuchBeanDefinitionException`, `getBean(Payments.class)` works | `KindsApp` |
| `finalmethod` | `BrokenOrderService.total()` is `public final`: CGLIB can't override it, so the call runs on the proxy object itself, whose fields are `null` (`NullPointerException`); `FixedOrderService` drops `final` (`total: 7`). With `--logging.level.org.springframework.aop=info` you also see Spring's WARN from `CglibAopProxy` | `FinalMethodApp` |
| `selfcall` | `OrderService.checkout()` calls its own `@Transactional save()` through `this`: `transaction active = false`, and the failing muffin leaves a half-saved order (`orders 1, order lines 1`); called from outside, the same `save()` rolls back (`orders 0, order lines 0`) | `SelfCallApp` |
| `fix` | `OrderService.checkout()` calls `OrderWriter.save()` through the injected proxy: `true`, the failed order rolls back, a good order saves `orders 1, order lines 2` | `FixApp` |
| `async` | `OrderService.checkout()` calls its own `@Async sendConfirmation()`: it runs on `main`; `FixedOrderService` calls `ConfirmationMailer.send()` in another bean: it runs on `task-1` | `AsyncApp` |
| `mail` | T4.2a cold open: `OrderService.checkout()` calls its own `@Async sendConfirmation()` and a 3-second `MailServer`: `confirmation A-1 sent on: main`, `customer waited: 3.0 s`; the bean is `OrderService$$SpringCGLIB$$0`; `FixedOrderService` calls `ConfirmationMailer.send()` in another bean: `customer waited: 0.0 s`, sent on `task-1` | `MailApp` |
| `finalmethod` (T4.2b) | `WhyNullApp`: the proxy's `prices` field is `null`, the real object's is set: the proxy is made without our constructor | `WhyNullApp` |
| `plain` (T4.2b) | the same `ShippingService` in plain Spring Framework (no Boot, `@EnableTransactionManagement`): a JDK proxy `jdk.proxy2.$Proxy19`, `is a ShippingService? false` | `PlainSpringApp` |
| `app` | the whole checkout: `CheckoutService` calls `OrderWriter.save()` (CGLIB, `@Transactional`), `Payments.charge()` (JDK proxy, `@Proxyable(INTERFACES)`) and `ConfirmationMailer.send()` (`@Async`), each through its proxy; a failed order leaves nothing behind | `CheckoutApp` |

Tests: `CheckoutProxiesTest` (7 tests) checks every line the video shows.

## Package `container.proxies` (first T4.2 render, kept; Spring Boot, JDBC + in-memory H2 for a transaction manager)
| Package | Example | Run |
|---|---|---|
| `whatis` | `MenuService` (plain) is its own class; `OrderService` has a `@Transactional` method, so the bean is `OrderService$$SpringCGLIB$$0`, a subclass in front of the real object (`AopUtils.isAopProxy`, `AopProxyUtils.ultimateTargetClass`) | `WhatIsApp` |
| `kinds` | `RefundService` gets Boot's default CGLIB subclass (still a `RefundService`); `PaymentService` has `@Proxyable(INTERFACES)` (Spring Framework 7) and gets a JDK proxy that only implements `Payments`: `getBean(PaymentService.class)` fails with `NoSuchBeanDefinitionException`, `getBean(Payments.class)` works | `KindsApp` |
| `finalmethod` | `BrokenPriceService.price()` is `public final`: CGLIB can't override it, so the call runs on the proxy object itself, whose fields are `null` (`NullPointerException`); `FixedPriceService` drops `final` (`coffee costs 3`). With `--logging.level.org.springframework.aop=info` you also see Spring's WARN from `CglibAopProxy` | `FinalMethodApp` |
| `selfcall` | `OrderService.checkout()` calls its own `@Transactional save()` through `this`: `transaction active = false`; `FixedOrderService` calls `OrderWriter.save()` through the injected proxy: `true` | `SelfCallApp` |
| `async` | `ReportService.finish()` calls its own `@Async send()`: it runs on `main`; `FixedReportService` calls `Mailer.send()` in another bean: it runs on `task-1` | `AsyncApp` |

## Package `container.cycles` (T4.3, Spring Boot)
| Package | Example | Run |
|---|---|---|
| `ctor` | `CustomerService` and `InvoiceService` need each other through their constructors: `APPLICATION FAILED TO START`, "form a cycle". With `--spring.main.allow-circular-references=true` it still fails ("could not be broken") | `CtorCycleApp` |
| `field` | the same cycle with `@Autowired` fields: still refused by default (the report names the fields); with `--spring.main.allow-circular-references=true` it starts and each service holds the other | `FieldCycleApp` |
| `halfbuilt` | run with `--spring.main.allow-circular-references=true`: `InvoiceService`'s `@PostConstruct` calls the early, half-built `CustomerService`, whose `invoices` field is still `null` (`NullPointerException`) | `HalfBuiltApp` |
| `extract` | the fix: names move into `CustomerDirectory`; the arrows point one way (`welcome Ada, invoice for Ada`) | `ExtractApp` |
| `events` | the fix: `InvoiceService` publishes `InvoicePaid` instead of calling back; `CustomerService` has an `@EventListener` | `EventsApp` |
| `lazy` | last resort: `@Lazy` on `InvoiceService`'s constructor parameter injects a proxy (`CustomerService$$SpringCGLIB$$0`); the cycle is only hidden | `LazyApp` |

## Package `container.lifecycle` (Spring Framework only, no Boot)
| File | Role |
|---|---|
| `LifecycleConfig` | `@Bean(initMethod = "warmUp", destroyMethod = "shutdown") priceCache(PriceSource)`, `@DependsOn("migrations") priceSource()`, `migrations()`, `reportJob()`, and both post-processors as `static @Bean` methods |
| `PriceCache` | every init and destroy mechanism on one bean: constructor, `@PostConstruct`, `InitializingBean`, `initMethod`, `@PreDestroy`, `DisposableBean`, `destroyMethod` (in real code, pick `@PostConstruct` / `@PreDestroy`) |
| `MakeReportJobLazy` | a `BeanFactoryPostProcessor`: prints the bean definitions while no bean exists yet, then marks `reportJob` lazy |
| `TracingPostProcessor` | a `BeanPostProcessor`: prints before and after the init callbacks of `priceCache` |
| `PriceSource`, `Migrations`, `ReportJob` | a dependency, a hidden dependency (`@DependsOn`) and a bean made lazy |
| `LifecycleApp` | starts the container, uses one bean, closes the container |

## Package `container.proxy` (Spring Boot, JDBC + in-memory H2 for a transaction manager)
| File | Role |
|---|---|
| `OrderService` | `checkout()` calls its own `@Transactional save()`: the call skips the proxy, so no transaction |
| `OrderWriter`, `FixedOrderService` | the fix: `save()` moves to another bean and is called through the injected proxy |
| `PaymentService` | `@Proxyable(INTERFACES)` (new in Spring Framework 7): a JDK dynamic proxy that implements `Payments`, instead of Boot's default CGLIB subclass |
| `ProxyApp` | prints the proxy classes and whether a transaction is active |

## Packages `container.cycle`, `fieldcycle`, `cyclefix`
| File | Role |
|---|---|
| `cycle.InvoiceService`, `cycle.CustomerService`, `CycleApp` | two services that need each other through their constructors: the app fails to start |
| `fieldcycle.*` | the same cycle with field injection: plain Spring Framework resolves it, Spring Boot refuses it unless `spring.main.allow-circular-references=true` |
| `cyclefix.*` | the fix: the shared part moves into `CustomerDirectory`, and both services depend on it |

## Run
```bash
./mvnw -pl module-04-container -am -q install -DskipTests
cd module-04-container
../mvnw -q dependency:build-classpath -Dmdep.outputFile=cp.txt
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.shop.nullfield.NullFieldApp
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.shop.app.ShopApp
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.postproc.staticrule.StaticRuleApp
# -- non-static @Bean method
# INFO ... @Bean method BrokenConfig.noOpPostProcessor is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. ...
# Menu for null
# -- static @Bean method
# Menu for Corner Shop

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.proxies.selfcall.SelfCallApp
# -- save() from outside
# save(A-1): transaction active = true
# -- checkout() calls save()
# save(A-2): transaction active = false
# -- fix: save() in another bean
# save(A-3): transaction active = true

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.cycles.field.FieldCycleApp --spring.main.allow-circular-references=true
# started
# customers.invoices is the invoice service? true
# invoices.customers is the customer service? true

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.cycles.lazy.LazyApp
# started
# injected: CustomerService$$SpringCGLIB$$0
# invoice for Ada

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.lifecycle.LifecycleApp
# definition: priceCache scope=singleton lazy=false
# definition: priceSource scope=singleton lazy=false
# definition: migrations scope=singleton lazy=false
# definition: reportJob scope=singleton lazy=false
# instances so far: 0
# reportJob is now lazy
# Migrations ran
# PriceSource created
# 1 constructor (dependencies injected)
# 2 postProcessBeforeInitialization(priceCache)
# 3 @PostConstruct load()
# 4 afterPropertiesSet()
# 5 initMethod warmUp()
# 6 postProcessAfterInitialization(priceCache)
# context ready, coffee costs 3
# ReportJob created
# closing
# @PreDestroy flush()
# DisposableBean destroy()
# destroyMethod shutdown()
# PriceSource closed

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.proxy.ProxyApp
# orderService   -> info.mnafshin.springuniverse.container.proxy.OrderService$$SpringCGLIB$$0
# paymentService -> jdk.proxy2.$Proxy49
# save() from outside:     transaction active = true
# checkout() calls save(): transaction active = false
# fixed checkout():        transaction active = true
# charge() via JDK proxy:  transaction active = true

java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.cycle.CycleApp
# APPLICATION FAILED TO START
# The dependencies of some of the beans in the application context form a cycle:
# ┌─────┐
# |  customerService defined in file [...CustomerService.class]
# ↑     ↓
# |  invoiceService defined in file [...InvoiceService.class]
# └─────┘
# Action:
# Relying upon circular references is discouraged and they are prohibited by default. ...
```

## Tests
`ShopLifecycleTest` (T4.1, one shop app: the null field and both fixes, eager vs lazy creation, the callback order, close() inference, cleanup only on close, `@DependsOn` order and reverse shutdown, the whole shop app start to stop),
`BeanLifecycleTest` (first T4.1 render: the null field and both fixes, eager vs lazy creation, the callback order, close() inference, cleanup only on close, `@DependsOn` order and reverse shutdown),
`PostProcessorsTest` (dropped post-processors video: the exact output of all six apps, the definition really becomes lazy, the bean really is a JDK proxy),
`ProxiesTest` (T4.2: the exact output of all five apps: CGLIB class name, JDK proxy and the failed class lookup, the final-method NPE, false then true, `main` then `task-1`),
`CyclesTest` (T4.3: the cycle report, the flag can't fix a constructor cycle, the field cycle refused then started with the flag, the half-built bean NPE, both fixes start without the flag, `@Lazy` injects a CGLIB proxy),
`LifecycleOrderTest` (definitions before instances, the init order, lazy on first use, the destroy order),
`ProxyTest` (CGLIB by default, `@Proxyable` gives a JDK proxy, the self-call has no transaction, the fix has one),
`CircularDependencyTest` (constructor cycle fails even with the escape hatch, field cycle fails in Boot by default,
starts with `spring.main.allow-circular-references=true` and in plain Spring Framework, the third-bean fix starts).

## Sources
- Spring Framework: [Customizing the nature of a bean](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html),
  [Lazy-initialized beans](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-lazy-init.html),
  [Using depends-on](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-dependson.html),
  [Container extension points](https://docs.spring.io/spring-framework/reference/core/beans/factory-extension.html),
  [Annotation-based container configuration](https://docs.spring.io/spring-framework/reference/core/beans/annotation-config.html),
  [Bean overview (bean definitions)](https://docs.spring.io/spring-framework/reference/core/beans/definition.html),
  [Proxying mechanisms](https://docs.spring.io/spring-framework/reference/core/aop/proxying.html),
  [Using @Transactional (proxy mode, self-invocation)](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html),
  [Declarative caching annotations](https://docs.spring.io/spring-framework/reference/integration/cache/annotations.html),
  [Task execution and scheduling (@Async)](https://docs.spring.io/spring-framework/reference/integration/scheduling.html),
  [Dependency injection: circular dependencies](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html)
- Spring Boot: [AOP](https://docs.spring.io/spring-boot/reference/features/aop.html),
  [SpringApplication, application exit](https://docs.spring.io/spring-boot/reference/features/spring-application.html),
  [application properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)
  (`spring.main.allow-circular-references`, `spring.aop.proxy-target-class`)
