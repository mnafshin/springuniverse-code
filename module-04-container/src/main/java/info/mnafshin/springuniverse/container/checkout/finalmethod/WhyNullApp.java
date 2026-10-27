package info.mnafshin.springuniverse.container.checkout.finalmethod;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * T4.2b: why "this.prices" is null in the final total(): the proxy is a second object, made without our constructor.
 * Reads the private prices field of the proxy and of the real object behind it.
 */
public class WhyNullApp {

    public static void main(String[] args) throws ReflectiveOperationException {
        try (ConfigurableApplicationContext context = SpringApplication.run(FinalMethodApp.class, args)) {
            BrokenOrderService proxy = context.getBean(BrokenOrderService.class);
            Object real = AopProxyUtils.getSingletonTarget(proxy);
            Field prices = BrokenOrderService.class.getDeclaredField("prices");
            prices.setAccessible(true);
            System.out.println("orderService class: " + proxy.getClass().getSimpleName());
            System.out.println("prices in the proxy:       " + name(prices.get(proxy)));
            System.out.println("prices in the real object: " + name(prices.get(real)));
        }
    }

    private static String name(Object value) {
        return value == null ? "null" : value.getClass().getSimpleName();
    }
}
