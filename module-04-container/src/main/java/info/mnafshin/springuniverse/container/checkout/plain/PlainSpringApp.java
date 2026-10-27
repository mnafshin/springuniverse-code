package info.mnafshin.springuniverse.container.checkout.plain;

import javax.sql.DataSource;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import info.mnafshin.springuniverse.container.checkout.kinds.Shipping;
import info.mnafshin.springuniverse.container.checkout.kinds.ShippingService;

/**
 * T4.2b: the same ShippingService (it implements Shipping) in plain Spring Framework, without Spring Boot.
 * Plain Spring makes a JDK proxy for a bean with an interface; Spring Boot (KindsApp) makes a CGLIB proxy.
 */
public class PlainSpringApp {

    @Configuration
    @EnableTransactionManagement
    @Import(ShippingService.class)
    static class Config {

        @Bean
        DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).generateUniqueName(true).build();
        }

        @Bean
        PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    public static void main(String[] args) {
        try (var context = new AnnotationConfigApplicationContext(Config.class)) {
            Object shipping = context.getBean(Shipping.class);
            System.out.println("plain Spring, shippingService: " + shipping.getClass().getName());
            System.out.println("  is a ShippingService? " + (shipping instanceof ShippingService));
        }
    }
}
