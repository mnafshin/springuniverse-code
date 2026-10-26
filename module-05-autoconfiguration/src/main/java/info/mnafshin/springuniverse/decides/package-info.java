/**
 * T5.1 How auto-configuration decides: six small runnable apps. Each one has its own
 * {@code @SpringBootApplication} in its own package, so component scanning only sees that
 * example. All of them also get the module's own {@code NotifierAutoConfiguration}
 * (listed in {@code AutoConfiguration.imports}).
 */
package info.mnafshin.springuniverse.decides;
