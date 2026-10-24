package info.mnafshin.springuniverse.config.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** A Spring container with no Spring Boot: create it from AppConfig, ask it for a bean, close it. */
public class ContainerApp {

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            SignupService signup = context.getBean(SignupService.class);
            signup.register("Ada");

            for (String name : context.getBeanDefinitionNames()) {
                if (!name.startsWith("org.springframework")) {
                    System.out.println("bean: " + name + " -> " + context.getType(name).getSimpleName());
                }
            }
        }
    }
}
