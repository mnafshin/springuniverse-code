package info.mnafshin.springuniverse.container.lifecycle;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Starts a plain Spring container, uses one bean, then closes the container. */
public class LifecycleApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(LifecycleConfig.class)) {
            Events.log("context ready, coffee costs " + context.getBean(PriceCache.class).price("coffee"));
            context.getBean(ReportJob.class);
            Events.log("closing");
        }
    }
}
