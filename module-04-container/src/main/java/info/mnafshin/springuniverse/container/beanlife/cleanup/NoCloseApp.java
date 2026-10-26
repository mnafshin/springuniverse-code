package info.mnafshin.springuniverse.container.beanlife.cleanup;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** The problem: nobody closes the container, so @PreDestroy never runs and the temp folder stays. */
public class NoCloseApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(TempFiles.class);
        System.out.println("working with " + context.getBean(TempFiles.class).getClass().getSimpleName());
        System.out.println("main() ends");
    }
}
