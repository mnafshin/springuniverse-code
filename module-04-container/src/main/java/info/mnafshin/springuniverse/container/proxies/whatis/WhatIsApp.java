package info.mnafshin.springuniverse.container.proxies.whatis;

import org.springframework.aop.support.AopUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WhatIsApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(WhatIsApp.class, args)) {
            Object menu = context.getBean(MenuService.class);
            Object orders = context.getBean(OrderService.class);
            System.out.println("menuService  class: " + menu.getClass().getSimpleName());
            System.out.println("orderService class: " + orders.getClass().getSimpleName());
            System.out.println("-- is it a proxy?");
            System.out.println("menuService:  " + AopUtils.isAopProxy(menu));
            System.out.println("orderService: " + AopUtils.isAopProxy(orders));
            System.out.println("orderService is an OrderService? " + (orders instanceof OrderService));
            System.out.println("the real object behind it: " + AopUtils.getTargetClass(orders).getSimpleName());
        }
    }
}
