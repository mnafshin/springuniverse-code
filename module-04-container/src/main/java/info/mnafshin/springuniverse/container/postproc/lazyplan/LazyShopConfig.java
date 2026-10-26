package info.mnafshin.springuniverse.container.postproc.lazyplan;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ShopConfig.class)
public class LazyShopConfig {

    @Bean
    public static MakeReportJobLazy makeReportJobLazy() {
        return new MakeReportJobLazy();
    }
}
