package info.mnafshin.springuniverse.container.postproc.lazyplan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopConfig {

    @Bean
    public Menu menu() {
        return new Menu();
    }

    @Bean
    public ReportJob reportJob() {
        return new ReportJob();
    }
}
