package info.mnafshin.springuniverse.container.checkout.whatis;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WhatIsApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(WhatIsApp.class, args)) {
            Object catalog = context.getBean(ProductCatalog.class);
            Object orders = context.getBean(OrderService.class);
            System.out.println("productCatalog class: " + catalog.getClass().getSimpleName());
            System.out.println("orderService   class: " + orders.getClass().getSimpleName());
            System.out.println("-- is it a proxy?");
            System.out.println("productCatalog: " + AopUtils.isAopProxy(catalog));
            System.out.println("orderService:   " + AopUtils.isAopProxy(orders));
            System.out.println("orderService is an OrderService? " + (orders instanceof OrderService));
            System.out.println("the real object behind it: " + AopUtils.getTargetClass(orders).getSimpleName());
        }
    }
}
