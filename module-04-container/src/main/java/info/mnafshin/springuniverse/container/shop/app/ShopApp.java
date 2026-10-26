package info.mnafshin.springuniverse.container.shop.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Runs the shop app from start to stop: every lifecycle step in one run. */
public class ShopApp {

    public static void main(String[] args) {
        Database.reset();
        try (var context = new AnnotationConfigApplicationContext(ShopConfig.class)) {
            System.out.println("shop ready");
            System.out.println("coffee costs " + context.getBean(PriceCache.class).price("coffee"));
            System.out.println("asking for the sales report");
            context.getBean(SalesReport.class);
            System.out.println("closing the shop");
        }
    }
}
