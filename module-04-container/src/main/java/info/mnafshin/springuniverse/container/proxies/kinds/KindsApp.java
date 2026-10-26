package info.mnafshin.springuniverse.container.proxies.kinds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KindsApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(KindsApp.class, args)) {
            Object refunds = context.getBean(Refunds.class);
            Object paymentBean = context.getBean(Payments.class);
            System.out.println("refundService:  " + refunds.getClass().getName().replace("info.mnafshin.springuniverse.container.proxies.", ""));
            System.out.println("  is a RefundService? " + (refunds instanceof RefundService));
            System.out.println("paymentService: " + paymentBean.getClass().getName());
            System.out.println("  is a PaymentService? " + (paymentBean instanceof PaymentService));
            System.out.println("-- asking for the class");
            try {
                context.getBean(PaymentService.class);
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getClass().getSimpleName());
            }
            System.out.println("-- asking for the interface");
            Payments payments = context.getBean(Payments.class);
            System.out.println("charge() works: " + payments.charge("A-1"));
        }
    }
}
