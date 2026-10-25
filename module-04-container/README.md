# Module 4: Bean lifecycle and container internals

What the Spring container does between "here are my classes" and "the app is ready": bean definitions,
the two kinds of post-processor, init and destroy callbacks, creation order, the proxies behind `@Transactional`,
and why Spring Boot refuses circular dependencies.

| Video | What it shows |
|---|---|
| T4.1 Inside the container (tutorial) | `BeanDefinition`s and a `BeanFactoryPostProcessor` that changes one before any bean exists, a `BeanPostProcessor` around the init callbacks, `@PostConstruct` / `afterPropertiesSet()` / `initMethod` and their destroy twins, dependency order and `@DependsOn`, CGLIB vs JDK proxies, Spring Framework 7's `@Proxyable`, the `@Transactional` self-call trap and its fix, and the circular-dependency startup failure |
| Short 025 | `@PostConstruct` vs the constructor: what runs when |
| Short 026 | Why your `@Transactional` self-call does nothing |
| Short 027 | Circular dependency? Spring Boot refuses, and that's good |
| Short 028 | `@Proxyable`: choose your proxy type (Spring Framework 7) |

Watch first: T2.2, `@Configuration`, `@Bean` and the ApplicationContext (`module-02-configuration`).
Videos: https://www.youtube.com/@springuniverse

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
`LifecycleOrderTest` (definitions before instances, the init order, lazy on first use, the destroy order),
`ProxyTest` (CGLIB by default, `@Proxyable` gives a JDK proxy, the self-call has no transaction, the fix has one),
`CircularDependencyTest` (constructor cycle fails even with the escape hatch, field cycle fails in Boot by default,
starts with `spring.main.allow-circular-references=true` and in plain Spring Framework, the third-bean fix starts).

## Sources
- Spring Framework: [Customizing the nature of a bean](https://docs.spring.io/spring-framework/reference/core/beans/factory-nature.html),
  [Container extension points](https://docs.spring.io/spring-framework/reference/core/beans/factory-extension.html),
  [Proxying mechanisms](https://docs.spring.io/spring-framework/reference/core/aop/proxying.html),
  [Dependency injection: circular dependencies](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html)
- Spring Boot: [AOP](https://docs.spring.io/spring-boot/reference/features/aop.html),
  [application properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)
  (`spring.main.allow-circular-references`, `spring.aop.proxy-target-class`)
