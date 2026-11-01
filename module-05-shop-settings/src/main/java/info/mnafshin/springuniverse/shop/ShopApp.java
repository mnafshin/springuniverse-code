package info.mnafshin.springuniverse.shop;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) {
        SpringApplication.run(ShopApp.class, args);
    }

    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> secondPort() {
        return factory -> {
            Connector connector = new Connector();
            connector.setPort(8081);
            factory.addAdditionalConnectors(connector);
        };
    }

}
