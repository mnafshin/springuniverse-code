package info.mnafshin.springuniverse.container.postproc;

import info.mnafshin.springuniverse.container.postproc.builtins.BuiltinsApp;
import info.mnafshin.springuniverse.container.postproc.lazyplan.LazyPlanApp;
import info.mnafshin.springuniverse.container.postproc.lazyplan.LazyShopConfig;
import info.mnafshin.springuniverse.container.postproc.plan.PlanApp;
import info.mnafshin.springuniverse.container.postproc.staticrule.StaticRuleApp;
import info.mnafshin.springuniverse.container.postproc.tracing.TracingApp;
import info.mnafshin.springuniverse.container.postproc.wrapper.PriceService;
import info.mnafshin.springuniverse.container.postproc.wrapper.SimplePriceService;
import info.mnafshin.springuniverse.container.postproc.wrapper.WrapperApp;
import info.mnafshin.springuniverse.container.postproc.wrapper.WrapperConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Checks every line the T4.2 video shows, in order. */
@ExtendWith(OutputCaptureExtension.class)
class PostProcessorsTest {

    private static String[] lines(CapturedOutput output) {
        return output.getOut().lines().filter(l -> !l.contains(" INFO ") && !l.contains(" WARN ")).toArray(String[]::new);
    }

    @Test
    void registerMakesThePlanRefreshMakesTheBeans(CapturedOutput output) {
        PlanApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- after register()",
                "definition menu: class=Menu scope=singleton lazy=false",
                "definition reportJob: class=ReportJob scope=singleton lazy=true",
                "-- refresh()",
                "Menu created",
                "container ready");
    }

    @Test
    void beanFactoryPostProcessorMakesTheLibraryBeanLazy(CapturedOutput output) {
        LazyPlanApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- without a post-processor", "Menu created", "ReportJob created (slow)", "container ready",
                "-- with MakeReportJobLazy", "MakeReportJobLazy: reportJob lazy=true", "Menu created", "container ready",
                "asking for the report job", "ReportJob created (slow)");
    }

    @Test
    void theDefinitionReallyChanged() {
        try (var context = new AnnotationConfigApplicationContext(LazyShopConfig.class)) {
            assertThat(context.getBeanFactory().getBeanDefinition("reportJob").isLazyInit()).isTrue();
        }
    }

    @Test
    void nonStaticPostProcessorMethodBreaksValueInjection(CapturedOutput output) {
        StaticRuleApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- non-static @Bean method", "Menu for null", "-- static @Bean method", "Menu for Corner Shop");
    }

    @Test
    void beanPostProcessorSeesEveryBeanAroundItsInitCallbacks(CapturedOutput output) {
        TracingApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "before init: tracingConfig", "after init:  tracingConfig",
                "before init: menu", "after init:  menu",
                "before init: priceCache", "  @PostConstruct load()", "after init:  priceCache",
                "container ready");
    }

    @Test
    void wrapperReplacesTheBean(CapturedOutput output) {
        WrapperApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- asking for the class",
                "failed: NoSuchBeanDefinitionException: No qualifying bean of type "
                        + "'info.mnafshin.springuniverse.container.postproc.wrapper.SimplePriceService' available",
                "-- asking for the interface", "same object as created? false", "log: price(coffee)", "coffee costs 3");
    }

    @Test
    void theBeanIsAJdkProxy() {
        try (var context = new AnnotationConfigApplicationContext(WrapperConfig.class)) {
            assertThat(Proxy.isProxyClass(context.getBean(PriceService.class).getClass())).isTrue();
            assertThatThrownBy(() -> context.getBean(SimplePriceService.class)).isInstanceOf(NoSuchBeanDefinitionException.class);
        }
    }

    @Test
    void annotationsNeedSpringsOwnPostProcessors(CapturedOutput output) {
        BuiltinsApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- container without Spring's annotation processors", "source = null",
                "-- with them",
                "post-processor: internalAutowiredAnnotationProcessor",
                "post-processor: internalCommonAnnotationProcessor",
                "@PostConstruct load(): source = PriceSource", "source = PriceSource");
    }
}
