package info.mnafshin.springuniverse.container.beanlife.callbacks;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CallbacksApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(CallbacksConfig.class)) {
            System.out.println("container ready");
            System.out.println("closing the container");
        }
    }
}
