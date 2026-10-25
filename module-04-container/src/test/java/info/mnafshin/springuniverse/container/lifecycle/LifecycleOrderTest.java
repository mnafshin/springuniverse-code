package info.mnafshin.springuniverse.container.lifecycle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class LifecycleOrderTest {

    @BeforeEach
    void clear() {
        Events.clear();
    }

    @Test
    void definitionsAreReadAndChangedBeforeAnyBeanExists() {
        try (var context = new AnnotationConfigApplicationContext(LifecycleConfig.class)) {
            assertThat(Events.all()).startsWith(
                    "definition: priceCache scope=singleton lazy=false",
                    "definition: priceSource scope=singleton lazy=false",
                    "definition: migrations scope=singleton lazy=false",
                    "definition: reportJob scope=singleton lazy=false",
                    "instances so far: 0",
                    "reportJob is now lazy");
            assertThat(context.getBeanFactory().getBeanDefinition("reportJob").isLazyInit()).isTrue();
        }
    }

    @Test
    void initCallbacksRunInTheDocumentedOrder() {
        try (var context = new AnnotationConfigApplicationContext(LifecycleConfig.class)) {
            assertThat(Events.all()).containsSubsequence(
                    "Migrations ran",
                    "PriceSource created",
                    "1 constructor (dependencies injected)",
                    "2 postProcessBeforeInitialization(priceCache)",
                    "3 @PostConstruct load()",
                    "4 afterPropertiesSet()",
                    "5 initMethod warmUp()",
                    "6 postProcessAfterInitialization(priceCache)");
            assertThat(context.getBean(PriceCache.class).price("coffee")).isEqualTo(3);
        }
    }

    @Test
    void lazyBeanIsCreatedOnFirstUseOnly() {
        try (var context = new AnnotationConfigApplicationContext(LifecycleConfig.class)) {
            assertThat(Events.all()).doesNotContain("ReportJob created");
            context.getBean(ReportJob.class);
            assertThat(Events.all()).contains("ReportJob created");
        }
    }

    @Test
    void destroyCallbacksRunInOrderAndDependenciesGoLast() {
        var context = new AnnotationConfigApplicationContext(LifecycleConfig.class);
        Events.clear();
        context.close();
        assertThat(Events.all()).containsExactly(
                "@PreDestroy flush()",
                "DisposableBean destroy()",
                "destroyMethod shutdown()",
                "PriceSource closed");
    }

    @Test
    void appPrintsTheWholeStory() {
        LifecycleApp.main(new String[0]);
        assertThat(Events.all()).containsSubsequence(
                "context ready, coffee costs 3", "ReportJob created", "closing", "@PreDestroy flush()", "PriceSource closed");
    }
}
