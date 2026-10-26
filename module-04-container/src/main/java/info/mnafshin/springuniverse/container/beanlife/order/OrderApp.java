package info.mnafshin.springuniverse.container.beanlife.order;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String[] args) {
        System.out.println("-- without @DependsOn");
        Database.reset();
        try (var context = new AnnotationConfigApplicationContext(HiddenDependencyConfig.class)) {
            System.out.println("closing");
        }

        System.out.println("-- with @DependsOn(\"migrations\")");
        Database.reset();
        try (var context = new AnnotationConfigApplicationContext(DependsOnConfig.class)) {
            System.out.println("closing");
        }
    }
}
