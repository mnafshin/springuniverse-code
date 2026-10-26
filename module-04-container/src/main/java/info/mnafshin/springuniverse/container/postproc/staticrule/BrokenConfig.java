package info.mnafshin.springuniverse.container.postproc.staticrule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrokenConfig {

    @Value("${shop.name:Corner Shop}")
    private String shopName;

    @Bean
    public NoOpPostProcessor noOpPostProcessor() {          // not static: BrokenConfig is created too early
        return new NoOpPostProcessor();
    }

    @Bean
    public Menu menu() {
        return new Menu(shopName);
    }
}
