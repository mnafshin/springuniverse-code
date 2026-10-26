package info.mnafshin.springuniverse.container.postproc.wrapper;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** The bean you get back is not the object the @Bean method created. Ask for the interface. */
public class WrapperApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(WrapperConfig.class)) {
            System.out.println("-- asking for the class");
            try {
                context.getBean(SimplePriceService.class);
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            }
            System.out.println("-- asking for the interface");
            PriceService service = context.getBean(PriceService.class);
            System.out.println("same object as created? " + (service instanceof SimplePriceService));
            System.out.println("coffee costs " + service.price("coffee"));
        }
    }
}
