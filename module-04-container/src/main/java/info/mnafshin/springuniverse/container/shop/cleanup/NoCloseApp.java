package info.mnafshin.springuniverse.container.shop.cleanup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** The problem: nobody closes the container, so @PreDestroy never runs and the export folder stays. */
public class NoCloseApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(PriceExport.class);
        System.out.println("working with " + context.getBean(PriceExport.class).getClass().getSimpleName());
        System.out.println("main() ends");
    }
}
