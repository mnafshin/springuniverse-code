/**
 * T4.2 Proxies and the self-call trap, built around one small shop's checkout (ADR-0019 R2).
 * Ten runnable Spring Boot examples, one sub-package each (each has its own {@code @SpringBootApplication}, except {@code finalclass}, which starts two tiny {@code @EnableAutoConfiguration} apps,
 * so each scans only its own package): {@code whatis}, {@code kinds}, {@code finalmethod}, {@code selfcall},
 * {@code fix}, {@code async}, {@code cache}, {@code retry}, {@code finalclass}, {@code app} (the whole checkout with every fix), {@code mail} (T4.2a cold open: @Async self-call, the customer waits 3 s)
 * and {@code plain} (T4.2b: plain Spring Framework without Boot makes a JDK proxy; its config is a plain AnnotationConfigApplicationContext).
 * Each app gets its own in-memory H2 database from Spring Boot, so {@code @Transactional} has a real transaction manager.
 */
package info.mnafshin.springuniverse.container.checkout;
