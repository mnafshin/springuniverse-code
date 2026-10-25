package info.mnafshin.springuniverse.container.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class LifecycleConfig {

    // Post-processors: static @Bean methods, so the configuration class isn't created early.
    @Bean
    public static MakeReportJobLazy makeReportJobLazy() {
        return new MakeReportJobLazy();
    }

    @Bean
    public static TracingPostProcessor tracingPostProcessor() {
        return new TracingPostProcessor();
    }

    // Declared first, but created after its dependency.
    @Bean(initMethod = "warmUp", destroyMethod = "shutdown")
    public PriceCache priceCache(PriceSource priceSource) {
        return new PriceCache(priceSource);
    }

    @Bean
    @DependsOn("migrations")
    public PriceSource priceSource() {
        return new PriceSource();
    }

    @Bean
    public Migrations migrations() {
        return new Migrations();
    }

    @Bean
    public ReportJob reportJob() {
        return new ReportJob();
    }
}
