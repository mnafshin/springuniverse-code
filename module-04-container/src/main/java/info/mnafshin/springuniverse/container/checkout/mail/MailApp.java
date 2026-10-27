package info.mnafshin.springuniverse.container.checkout.mail;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * T4.2 (v4): the checkout should send its confirmation email in the background with @Async.
 * Part 1: the self-call, the customer waits 3 s and the email is sent on main.
 * Part 2: what Spring hands out for OrderService: a CGLIB proxy (a generated subclass).
 * Part 3: the fix, ConfirmationMailer in another bean: the customer waits 0 s, the email is sent on task-1.
 */
@SpringBootApplication
@EnableAsync
public class MailApp {

    public static void main(String[] args) throws InterruptedException {
        try (ConfigurableApplicationContext context = SpringApplication.run(MailApp.class, args)) {
            OrderService orders = context.getBean(OrderService.class);
            System.out.println("-- checkout A-1");
            long start = System.nanoTime();
            orders.checkout("A-1");
            System.out.println("customer waited: " + seconds(start) + " s");

            MailServer mailServer = context.getBean(MailServer.class);
            System.out.println("-- what did Spring give us?");
            System.out.println("mailServer   class: " + mailServer.getClass().getSimpleName());
            System.out.println("orderService class: " + orders.getClass().getSimpleName());
            System.out.println("mailServer is a proxy? " + AopUtils.isAopProxy(mailServer));
            System.out.println("orderService is a proxy? " + AopUtils.isAopProxy(orders));
            System.out.println("orderService is an OrderService? " + (orders instanceof OrderService));
            System.out.println("the real object behind it: " + AopUtils.getTargetClass(orders).getSimpleName());

            System.out.println("-- fix: ConfirmationMailer, another bean");
            System.out.println("-- checkout A-2");
            start = System.nanoTime();
            context.getBean(FixedOrderService.class).checkout("A-2");
            System.out.println("customer waited: " + seconds(start) + " s");
            Thread.sleep(3500); // let the background email finish before the app stops
        }
    }

    private static String seconds(long start) {
        return String.format(java.util.Locale.ROOT, "%.1f", (System.nanoTime() - start) / 1e9);
    }
}
