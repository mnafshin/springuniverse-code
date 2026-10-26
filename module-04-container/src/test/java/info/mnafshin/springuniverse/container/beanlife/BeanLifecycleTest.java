package info.mnafshin.springuniverse.container.beanlife;

import info.mnafshin.springuniverse.container.beanlife.callbacks.CallbacksApp;
import info.mnafshin.springuniverse.container.beanlife.cleanup.BootCleanupApp;
import info.mnafshin.springuniverse.container.beanlife.cleanup.CloseApp;
import info.mnafshin.springuniverse.container.beanlife.cleanup.NoCloseApp;
import info.mnafshin.springuniverse.container.beanlife.cleanup.TempFiles;
import info.mnafshin.springuniverse.container.beanlife.nullfield.BrokenPriceCache;
import info.mnafshin.springuniverse.container.beanlife.nullfield.ConstructorPriceCache;
import info.mnafshin.springuniverse.container.beanlife.nullfield.FixedPriceCache;
import info.mnafshin.springuniverse.container.beanlife.nullfield.NullFieldApp;
import info.mnafshin.springuniverse.container.beanlife.nullfield.PriceSource;
import info.mnafshin.springuniverse.container.beanlife.order.OrderApp;
import info.mnafshin.springuniverse.container.beanlife.whencreated.WhenCreatedApp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Checks every line the T4.1 video shows, in order. */
@ExtendWith(OutputCaptureExtension.class)
class BeanLifecycleTest {

    private static String[] lines(CapturedOutput output) {
        return output.getOut().lines().filter(l -> !l.contains(" WARN ")).toArray(String[]::new);
    }

    @Test
    void fieldIsStillNullInTheConstructor() {
        assertThatThrownBy(() -> new AnnotationConfigApplicationContext(PriceSource.class, BrokenPriceCache.class))
                .isInstanceOf(BeanCreationException.class)
                .rootCause().isInstanceOf(NullPointerException.class).hasMessageContaining("\"this.source\" is null");
    }

    @Test
    void postConstructAndConstructorInjectionBothWork() {
        try (var context = new AnnotationConfigApplicationContext(PriceSource.class, FixedPriceCache.class, ConstructorPriceCache.class)) {
            assertThat(context.getBean(FixedPriceCache.class).price("coffee")).isEqualTo(3);
            assertThat(context.getBean(ConstructorPriceCache.class).price("coffee")).isEqualTo(3);
        }
    }

    @Test
    void nullFieldAppPrintsTheProblemAndBothFixes(CapturedOutput output) {
        NullFieldApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- field injection, used in the constructor",
                "constructor: source = null",
                "startup failed: NullPointerException: Cannot invoke \"info.mnafshin.springuniverse.container.beanlife.nullfield.PriceSource.load()\" because \"this.source\" is null",
                "-- fix 1: @PostConstruct",
                "constructor: source = null",
                "@PostConstruct: source = PriceSource",
                "coffee costs 3",
                "-- fix 2: constructor injection",
                "constructor: source = PriceSource",
                "coffee costs 3");
    }

    @Test
    void singletonsAtStartupOnceLazyOnFirstUse(CapturedOutput output) {
        WhenCreatedApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- normal singletons", "starting the container", "Menu created", "ReportJob created", "container ready",
                "asked twice, same object: true",
                "-- with @Lazy", "starting the container", "Menu created", "container ready", "asking for the report job",
                "LazyReportJob created");
    }

    @Test
    void callbacksRunInTheDocumentedOrder(CapturedOutput output) {
        CallbacksApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "constructor", "1 @PostConstruct load()", "2 afterPropertiesSet()", "3 initMethod warmUp()",
                "container ready", "closing the container",
                "1 @PreDestroy flush()", "2 destroy()", "3 destroyMethod shutdown()", "PriceStore close(), found by Spring");
    }

    @Test
    void preDestroyRunsOnlyWhenTheContainerIsClosed(CapturedOutput output) {
        NoCloseApp.main(new String[0]);
        assertThat(output.getOut()).doesNotContain("temp folder deleted");
        CloseApp.main(new String[0]);
        assertThat(output.getOut()).contains("TempFiles: temp folder deleted");
    }

    @Test
    void tempFolderReallyGoesAway() {
        Path folder;
        try (var context = new AnnotationConfigApplicationContext(TempFiles.class)) {
            folder = context.getBean(TempFiles.class).folder();
            assertThat(Files.isDirectory(folder)).isTrue();
        }
        assertThat(Files.exists(folder)).isFalse();
    }

    @Test
    void springBootClosesTheContextAndRunsPreDestroy(CapturedOutput output) {
        var context = SpringApplication.run(BootCleanupApp.class);
        assertThat(context.getBean(TempFiles.class)).isNotNull();
        context.close();   // what Boot's shutdown hook does on Ctrl+C or SIGTERM
        assertThat(output.getOut()).contains("TempFiles: temp folder deleted");
    }

    @Test
    void dependenciesFirstDependsOnForHiddenOnesShutdownReversed(CapturedOutput output) {
        OrderApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "-- without @DependsOn",
                "PriceSource: table prices is missing!", "PriceCache created", "Migrations: table prices created",
                "closing", "Migrations closed", "PriceCache closed", "PriceSource closed",
                "-- with @DependsOn(\"migrations\")",
                "Migrations: table prices created", "PriceSource: table prices found", "PriceCache created",
                "closing", "PriceCache closed", "PriceSource closed", "Migrations closed");
    }
}
