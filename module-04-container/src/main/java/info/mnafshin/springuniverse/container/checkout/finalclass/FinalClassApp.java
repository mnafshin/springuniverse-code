package info.mnafshin.springuniverse.container.checkout.finalclass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/** Two tiny apps: one with the final ReceiptService (CGLIB proxy), one with the final JdkReceiptService (JDK proxy). */
public class FinalClassApp {

    @Configuration
    @EnableAutoConfiguration
    @Import(ReceiptService.class)
    static class WithCglib {
    }

    @Configuration
    @EnableAutoConfiguration
    @Import(JdkReceiptService.class)
    static class WithJdkProxy {
    }

    public static void main(String[] args) {
        System.out.println("-- final class ReceiptService, CGLIB proxy");
        try (ConfigurableApplicationContext context = SpringApplication.run(WithCglib.class, args)) {
            System.out.println(context.getBean(Receipts.class).print("A-8"));
        } catch (RuntimeException e) {
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            System.out.println("app failed to start: " + e.getClass().getSimpleName());
            System.out.println("cause: " + cause.getMessage());
        }
        System.out.println("-- final class JdkReceiptService, @Proxyable(INTERFACES)");
        try (ConfigurableApplicationContext context = SpringApplication.run(WithJdkProxy.class, args)) {
            Receipts receipts = context.getBean(Receipts.class);
            System.out.println("proxy: " + receipts.getClass().getName());
            System.out.println(receipts.print("A-8"));
        }
    }
}
