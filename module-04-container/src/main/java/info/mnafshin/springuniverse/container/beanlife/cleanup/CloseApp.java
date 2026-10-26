package info.mnafshin.springuniverse.container.beanlife.cleanup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** The fix: close the container. try-with-resources calls close() for us. */
public class CloseApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(TempFiles.class)) {
            System.out.println("working with " + context.getBean(TempFiles.class).getClass().getSimpleName());
        }
        System.out.println("main() ends");
    }
}
