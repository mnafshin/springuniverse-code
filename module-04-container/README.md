# Module 4: Bean lifecycle and container internals

What the Spring container does between "here are my classes" and "the app is ready": bean definitions,
the two kinds of post-processor, init and destroy callbacks, creation order, the proxies behind `@Transactional`,
and why Spring Boot refuses circular dependencies.

| Video | What it shows |
|---|---|
| T4.1 The Spring bean lifecycle (tutorial) | package `container.beanlife`: five runnable examples, each problem first, then the fix: a `null` field in the constructor (`@PostConstruct`, constructor injection), when singletons are created and `@Lazy`, the order of the three init and destroy callbacks, cleanup that never runs until the container is closed (try-with-resources, the Spring Boot shutdown hook), and `@DependsOn` for a hidden dependency |
| T4.2 Bean post-processors (tutorial) | package `container.postproc`: six runnable examples: the plan (bean definitions) comes before the beans, a `BeanFactoryPostProcessor` that makes a library bean lazy, the `static @Bean` rule (`Menu for null` vs `Menu for Corner Shop`), a `BeanPostProcessor` that sees every bean, a post-processor that returns a wrapper (ask for the interface, not the class), and the built-in annotation post-processors behind `@Autowired` and `@PostConstruct` |
| T4.3, T4.4 (planned) | proxies and the self-call trap, circular dependencies: packages `container.proxy`, `container.cycle`, `fieldcycle`, `cyclefix` |
| Short 025 | Why is my field null in the constructor? (`@PostConstruct` vs the constructor) |
| Short 026 | Why your `@Transactional` self-call does nothing |
| Short 027 | Circular dependency? Spring Boot refuses to start |
| Short 028 | `@Proxyable`: choose the proxy type per bean (Spring Framework 7) |

Watch first: T2.2, `@Configuration`, `@Bean` and the ApplicationContext (`module-02-configuration`).
Videos: https://www.youtube.com/@springuniverse

## Package `container.beanlife` (T4.1, Spring Framework only except `cleanup.BootCleanupApp`)
| Package | Example | Run |
|---|---|---|
| `nullfield` | `BrokenPriceCache` uses an `@Autowired` field in its constructor: `source = null`, startup fails. Fix 1 `FixedPriceCache` (`@PostConstruct`), fix 2 `ConstructorPriceCache` (constructor injection) | `NullFieldApp` |
| `whencreated` | `Menu` and `ReportJob` are created at startup, once (same object twice); `LazyReportJob` (`@Lazy`) is created on first use | `WhenCreatedApp` |
| `callbacks` | `PriceCache` with `@PostConstruct` / `afterPropertiesSet()` / `initMethod` and `@PreDestroy` / `destroy()` / `destroyMethod`; `PriceStore` only has `close()`, which a `@Bean` method infers as its destroy method | `CallbacksApp` |
| `cleanup` | `TempFiles` creates a real temp folder in `@PostConstruct` and deletes it in `@PreDestroy`. `NoCloseApp` never closes the context (no delete), `CloseApp` uses try-with-resources (deleted), `BootCleanupApp` relies on Boot's shutdown hook (stop it with Ctrl+C or `kill PID`: deleted; `kill -9 PID`: not deleted) | `NoCloseApp`, `CloseApp`, `BootCleanupApp` |
| `order` | `HiddenDependencyConfig`: `PriceSource` reads a table that `Migrations` creates but doesn't depend on it ("table prices is missing!"); `DependsOnConfig` adds `@DependsOn("migrations")`; shutdown runs in reverse | `OrderApp` |

## Package `container.postproc` (T4.2, Spring Framework only, no Boot)
| Package | Example | Run |
|---|---|---|
| `plan` | after `register()` the container only holds bean definitions (class, scope, lazy); `Menu` is created at `refresh()`, the `@Lazy` `ReportJob` stays a recipe | `PlanApp` |
| `lazyplan` | `ReportJob` (a "library" class, no `@Lazy`) is slow and created at startup (`ShopConfig`); `LazyShopConfig` adds a `static @Bean MakeReportJobLazy` (`BeanFactoryPostProcessor`) that sets `lazyInit` on its definition | `LazyPlanApp` |
| `staticrule` | `BrokenConfig` declares a `BeanFactoryPostProcessor` with a non-static `@Bean` method: Spring logs an INFO from `ConfigurationClassEnhancer` and `@Value` is never injected (`Menu for null`); `FixedConfig` makes the method `static` (`Menu for Corner Shop`) | `StaticRuleApp` |
| `tracing` | `TracingPostProcessor` (`BeanPostProcessor`) prints before and after init for every bean; `@PostConstruct` runs between the two calls | `TracingApp` |
| `wrapper` | `LoggingPostProcessor` returns a JDK proxy around every `PriceService`; `getBean(SimplePriceService.class)` fails with `NoSuchBeanDefinitionException`, `getBean(PriceService.class)` works and every call goes through the wrapper | `WrapperApp` |
| `builtins` | a bare `GenericApplicationContext`: `@Autowired` and `@PostConstruct` are ignored (`source = null`) until `AnnotationConfigUtils.registerAnnotationConfigProcessors(context)` adds Spring's own post-processors | `BuiltinsApp` |

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
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.beanlife.nullfield.NullFieldApp
java -cp target/classes:$(cat cp.txt) info.mnafshin.springuniverse.container.postproc.staticrule.StaticRuleApp
# -- non-static @Bean method
# INFO ... @Bean method BrokenConfig.noOpPostProcessor is non-static and returns an object assignable to Spring's BeanFactoryPostProcessor interface. ...
# Menu for null
# -- static @Bean method
# Menu for Corner Shop

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
`BeanLifecycleTest` (T4.1: the null field and both fixes, eager vs lazy creation, the callback order, close() inference, cleanup only on close, `@DependsOn` order and reverse shutdown),
`PostProcessorsTest` (T4.2: the exact output of all six apps, the definition really becomes lazy, the bean really is a JDK proxy),
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
  [Dependency injection: circular dependencies](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html)
- Spring Boot: [AOP](https://docs.spring.io/spring-boot/reference/features/aop.html),
  [SpringApplication, application exit](https://docs.spring.io/spring-boot/reference/features/spring-application.html),
  [application properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)
  (`spring.main.allow-circular-references`, `spring.aop.proxy-target-class`)
