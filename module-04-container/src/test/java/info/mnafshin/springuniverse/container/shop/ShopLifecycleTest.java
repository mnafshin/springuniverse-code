package info.mnafshin.springuniverse.container.shop;

import info.mnafshin.springuniverse.container.shop.app.ShopApp;
import info.mnafshin.springuniverse.container.shop.app.ShopConfig;
import info.mnafshin.springuniverse.container.shop.callbacks.CallbacksApp;
import info.mnafshin.springuniverse.container.shop.cleanup.BootCleanupApp;
import info.mnafshin.springuniverse.container.shop.cleanup.CloseApp;
import info.mnafshin.springuniverse.container.shop.cleanup.NoCloseApp;
import info.mnafshin.springuniverse.container.shop.cleanup.PriceExport;
import info.mnafshin.springuniverse.container.shop.nullfield.BrokenPriceCache;
import info.mnafshin.springuniverse.container.shop.nullfield.ConstructorPriceCache;
import info.mnafshin.springuniverse.container.shop.nullfield.FixedPriceCache;
import info.mnafshin.springuniverse.container.shop.nullfield.NullFieldApp;
import info.mnafshin.springuniverse.container.shop.nullfield.PriceSource;
import info.mnafshin.springuniverse.container.shop.order.OrderApp;
import info.mnafshin.springuniverse.container.shop.whencreated.WhenCreatedApp;
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

/** Checks every line the T4.1 (v2) video shows, in order: one shop app built around its price service. */
@ExtendWith(OutputCaptureExtension.class)
class ShopLifecycleTest {

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
                "startup failed: NullPointerException: Cannot invoke \"info.mnafshin.springuniverse.container.shop.nullfield.PriceSource.load()\" because \"this.source\" is null",
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
                "-- normal singletons", "starting the container", "PriceCache created", "SalesReport created", "container ready",
                "asked twice, same object: true",
                "-- with @Lazy", "starting the container", "PriceCache created", "container ready", "asking for the sales report",
                "LazySalesReport created");
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
        assertThat(output.getOut()).doesNotContain("export folder deleted");
        CloseApp.main(new String[0]);
        assertThat(output.getOut()).contains("PriceExport: export folder deleted");
    }

    @Test
    void exportFolderReallyGoesAway() {
        Path folder;
        try (var context = new AnnotationConfigApplicationContext(PriceExport.class)) {
            folder = context.getBean(PriceExport.class).folder();
            assertThat(Files.isDirectory(folder)).isTrue();
        }
        assertThat(Files.exists(folder)).isFalse();
    }

    @Test
    void springBootClosesTheContextAndRunsPreDestroy(CapturedOutput output) {
        var context = SpringApplication.run(BootCleanupApp.class);
        assertThat(context.getBean(PriceExport.class)).isNotNull();
        context.close();   // what Boot's shutdown hook does on Ctrl+C or SIGTERM
        assertThat(output.getOut()).contains("PriceExport: export folder deleted");
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

    @Test
    void wholeShopAppRunsEveryStepInOrder(CapturedOutput output) {
        ShopApp.main(new String[0]);
        assertThat(lines(output)).containsExactly(
                "Migrations: table prices created", "PriceSource: table prices found", "PriceCache: 2 prices loaded",
                "PriceExport: export folder created", "shop ready", "coffee costs 3",
                "asking for the sales report", "SalesReport created", "closing the shop",
                "PriceExport: export folder deleted", "PriceCache closed", "PriceSource closed", "Migrations closed");
    }

    @Test
    void wholeShopAppDeletesItsExportFolder() {
        Path folder;
        try (var context = new AnnotationConfigApplicationContext(ShopConfig.class)) {
            folder = context.getBean(info.mnafshin.springuniverse.container.shop.app.PriceExport.class).folder();
            assertThat(folder.resolve("prices.csv")).exists();
        }
        assertThat(Files.exists(folder)).isFalse();
    }
}
