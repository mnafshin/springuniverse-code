package info.mnafshin.springuniverse.container.postproc.staticrule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Declare post-processors with static @Bean methods, or their configuration class misses @Value and @Autowired. */
public class StaticRuleApp {

    public static void main(String[] args) {
        System.out.println("-- non-static @Bean method");
        new AnnotationConfigApplicationContext(BrokenConfig.class).close();
        System.out.println("-- static @Bean method");
        new AnnotationConfigApplicationContext(FixedConfig.class).close();
    }
}
