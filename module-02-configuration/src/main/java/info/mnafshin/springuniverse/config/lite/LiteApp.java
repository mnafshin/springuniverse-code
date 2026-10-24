package info.mnafshin.springuniverse.config.lite;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Prints "AuditLog created" twice: one for the bean, one from the plain call inside userRepository(). */
public class LiteApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(LiteConfig.class)) {
            System.out.println("beans of type AuditLog: " + context.getBeansOfType(
                    info.mnafshin.springuniverse.config.app.AuditLog.class).size());
        }
    }
}
