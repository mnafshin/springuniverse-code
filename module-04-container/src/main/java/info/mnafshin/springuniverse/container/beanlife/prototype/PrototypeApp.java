package info.mnafshin.springuniverse.container.beanlife.prototype;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Spring creates prototype beans and then forgets them: closing the container does not call their @PreDestroy. */
public class PrototypeApp {

    public static void main(String[] args) {
        System.out.println("-- three requests");
        try (var context = new AnnotationConfigApplicationContext(Database.class, ReportConnection.class)) {
            for (int i = 0; i < 3; i++) {
                context.getBean(ReportConnection.class);
            }
            System.out.println("closing the container");
        }

        System.out.println("-- fix: the caller closes it");
        try (var context = new AnnotationConfigApplicationContext(Database.class, ReportConnection.class)) {
            try (var connection = context.getBean(ReportConnection.class)) {
                System.out.println("using the connection");
            }
            System.out.println("closing the container");
        }
    }
}
