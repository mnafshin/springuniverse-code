package info.mnafshin.springuniverse.container.checkout.kinds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class KindsApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(KindsApp.class, args)) {
            Object shipping = context.getBean(Shipping.class);
            Object paymentBean = context.getBean(Payments.class);
            System.out.println("shippingService: " + shipping.getClass().getSimpleName());
            System.out.println("  is a ShippingService? " + (shipping instanceof ShippingService));
            System.out.println("paymentService:  " + paymentBean.getClass().getName());
            System.out.println("  is a PaymentService? " + (paymentBean instanceof PaymentService));
            System.out.println("-- asking for the class");
            try {
                context.getBean(PaymentService.class);
            } catch (RuntimeException e) {
                System.out.println("failed: " + e.getClass().getSimpleName());
            }
            System.out.println("-- asking for the interface");
            Payments payments = context.getBean(Payments.class);
            System.out.println("charge(A-1) works: " + payments.charge("A-1"));
        }
    }
}
