/**
 * T4.1 The bean lifecycle: five small, runnable examples. Each one shows a problem first, then the fix.
 * <ul>
 *   <li>{@code nullfield}: an injected field is still null in the constructor; {@code @PostConstruct} runs after injection</li>
 *   <li>{@code whencreated}: singletons are created at startup, once; {@code @Lazy} waits for the first request</li>
 *   <li>{@code callbacks}: the three init and destroy mechanisms and their order; a public {@code close()} is found on its own</li>
 *   <li>{@code cleanup}: {@code @PreDestroy} only runs when the container is closed</li>
 *   <li>{@code order}: dependencies are created first; {@code @DependsOn} for a hidden dependency; shutdown runs in reverse</li>
 * </ul>
 */
package info.mnafshin.springuniverse.container.beanlife;
