package info.mnafshin.springuniverse.container.beanlife.prototype;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Same thing in Spring Boot: the shutdown hook closes the context when the JVM exits, the singleton Database gets its
 * @PreDestroy, the prototype connections still get none. */
@SpringBootApplication
public class BootPrototypeApp {
    public static void main(String[] args) {
        var context = SpringApplication.run(BootPrototypeApp.class, args);
        for (int i = 0; i < 3; i++) {
            context.getBean(ReportConnection.class);
        }
        System.out.println("main ends, Boot closes the context on shutdown");
    }
}
