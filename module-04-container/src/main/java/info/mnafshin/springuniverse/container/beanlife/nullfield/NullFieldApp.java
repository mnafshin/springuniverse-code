package info.mnafshin.springuniverse.container.beanlife.nullfield;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Runs the broken cache, then both fixes. */
public class NullFieldApp {

    public static void main(String[] args) {
        System.out.println("-- field injection, used in the constructor");
        try (var context = new AnnotationConfigApplicationContext(PriceSource.class, BrokenPriceCache.class)) {
            System.out.println("started");
        } catch (BeanCreationException e) {
            Throwable cause = e.getMostSpecificCause();
            System.out.println("startup failed: " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
        }

        System.out.println("-- fix 1: @PostConstruct");
        try (var context = new AnnotationConfigApplicationContext(PriceSource.class, FixedPriceCache.class)) {
            System.out.println("coffee costs " + context.getBean(FixedPriceCache.class).price("coffee"));
        }

        System.out.println("-- fix 2: constructor injection");
        try (var context = new AnnotationConfigApplicationContext(PriceSource.class, ConstructorPriceCache.class)) {
            System.out.println("coffee costs " + context.getBean(ConstructorPriceCache.class).price("coffee"));
        }
    }
}
