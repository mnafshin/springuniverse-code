/**
 * T4.2 Bean definitions and post-processors: six small, runnable examples. Each one shows a problem first, then the fix.
 * <ul>
 *   <li>{@code plan}: registering classes creates bean definitions (the plan); refresh() creates the beans</li>
 *   <li>{@code lazyplan}: a BeanFactoryPostProcessor makes a library bean lazy without touching its class</li>
 *   <li>{@code staticrule}: a non-static @Bean method for a post-processor breaks @Value in its configuration class</li>
 *   <li>{@code tracing}: a BeanPostProcessor sees every bean, before and after its init callbacks</li>
 *   <li>{@code wrapper}: a BeanPostProcessor can hand back a different object; depend on the interface</li>
 *   <li>{@code builtins}: @Autowired and @PostConstruct only work because Spring registers its own post-processors</li>
 * </ul>
 */
package info.mnafshin.springuniverse.container.postproc;
