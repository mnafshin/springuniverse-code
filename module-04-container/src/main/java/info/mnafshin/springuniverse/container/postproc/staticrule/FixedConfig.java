package info.mnafshin.springuniverse.container.postproc.staticrule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FixedConfig {

    @Value("${shop.name:Corner Shop}")
    private String shopName;

    @Bean
    public static NoOpPostProcessor noOpPostProcessor() {   // static: no FixedConfig instance needed
        return new NoOpPostProcessor();
    }

    @Bean
    public Menu menu() {
        return new Menu(shopName);
    }
}
