package info.mnafshin.springuniverse.container.postproc.tracing;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** One BeanPostProcessor prints a line for every bean, and no bean prints anything itself (except @PostConstruct). */
public class TracingApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(TracingConfig.class)) {
            System.out.println("container ready");
        }
    }
}
