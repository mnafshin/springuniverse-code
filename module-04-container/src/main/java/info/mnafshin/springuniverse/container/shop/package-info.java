/**
 * T4.1 The bean lifecycle (v2, ADR-0019): one small shop app built around its price service, in six runnable examples.
 * Each one shows a problem first, then the fix.
 * <ul>
 *   <li>{@code nullfield}: the price cache's injected price source is still null in its constructor; {@code @PostConstruct} runs after injection</li>
 *   <li>{@code whencreated}: the price cache and the sales report are created at startup, once; {@code @Lazy} makes the sales report wait for the first request</li>
 *   <li>{@code callbacks}: the three init and destroy mechanisms on the price cache and their order; the price store's public {@code close()} is found on its own</li>
 *   <li>{@code cleanup}: the price export only deletes its folder in {@code @PreDestroy} when the container is closed</li>
 *   <li>{@code order}: dependencies are created first; {@code @DependsOn} for the hidden migrations dependency; shutdown runs in reverse</li>
 *   <li>{@code app}: the whole shop app with every fix, run from start to stop</li>
 * </ul>
 */
package info.mnafshin.springuniverse.container.shop;
