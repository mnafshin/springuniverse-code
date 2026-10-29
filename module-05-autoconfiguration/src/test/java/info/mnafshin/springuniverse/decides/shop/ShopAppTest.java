package info.mnafshin.springuniverse.decides.shop;

import javax.sql.DataSource;

import info.mnafshin.springuniverse.notifier.Notifier;
import info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/** The T5.1 shop app runs, started the way its {@code main} starts it (no web server, no JDBC module). */
class ShopAppTest {

    static ConfigurableApplicationContext run(String... args) {
        return new SpringApplicationBuilder(ShopApp.class).web(WebApplicationType.NONE).run(args);
    }

    @Test
    void plainRunGetsTheDefaultNotifierAndNoDataSource() {
        try (ConfigurableApplicationContext context = run()) {
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("log");
            assertThat(context.getBeansOfType(DataSource.class)).isEmpty();
        }
    }

    @Test
    void ourOwnBeanMakesTheDefaultBackOff() {
        try (ConfigurableApplicationContext context = run("--spring.profiles.active=sms")) {
            assertThat(context.getBeansOfType(Notifier.class)).containsOnlyKeys("smsNotifier");
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource()
                .get(NotifierAutoConfiguration.class.getName() + "#notifier")
                .isFullMatch()).isFalse();
        }
    }

    @Test
    void aTypoInThePropertyNameChangesNothing() {
        try (ConfigurableApplicationContext context = run("--notifier.enable=false")) {
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("log");
        }
    }

    @Test
    void theRightPropertySwitchesTheAutoConfigurationOff() {
        try (ConfigurableApplicationContext context = run("--notifier.enabled=false")) {
            assertThat(context.getBeansOfType(Notifier.class)).isEmpty();
        }
    }

}
