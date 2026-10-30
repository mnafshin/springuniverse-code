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

    /**
     * T5.1 v4, "inside one candidate": the hand-written {@link NotifierAutoConfiguration} has one condition on the
     * class and one on its bean method. Plain run: both match, the default log notifier is created. With the shop's
     * own notifier: the class still matches, only the method's condition says no, so only that bean is skipped.
     */
    @Test
    void theSameAutoConfigurationDecidesBothWays() {
        String type = NotifierAutoConfiguration.class.getName();
        try (ConfigurableApplicationContext context = run()) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource().get(type).isFullMatch()).isTrue();
            assertThat(report.getConditionAndOutcomesBySource().get(type + "#notifier").isFullMatch()).isTrue();
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("log");
        }
        try (ConfigurableApplicationContext context = run("--spring.profiles.active=sms")) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource().get(type).isFullMatch()).isTrue();
            assertThat(report.getConditionAndOutcomesBySource().get(type + "#notifier").isFullMatch()).isFalse();
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("sms");
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
