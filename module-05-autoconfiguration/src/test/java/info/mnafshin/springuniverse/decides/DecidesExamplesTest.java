package info.mnafshin.springuniverse.decides;

import java.util.List;

import javax.sql.DataSource;

import info.mnafshin.springuniverse.decides.candidates.CandidatesApp;
import info.mnafshin.springuniverse.decides.missingbean.NotifierApp;
import info.mnafshin.springuniverse.decides.onclass.DatabaseApp;
import info.mnafshin.springuniverse.decides.property.PropertyApp;
import info.mnafshin.springuniverse.decides.trap.TrapApp;
import info.mnafshin.springuniverse.decides.trapfix.TrapFixedApp;
import info.mnafshin.springuniverse.notifier.LogNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;
import info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/** The T5.1 examples, each started the way its {@code main} starts it (no web server). */
class DecidesExamplesTest {

    static ConfigurableApplicationContext run(Class<?> app, String... args) {
        return new SpringApplicationBuilder(app).web(WebApplicationType.NONE).run(args);
    }

    @Test
    void example1TheNotifierComesFromACandidate() {
        try (ConfigurableApplicationContext context = run(CandidatesApp.class)) {
            assertThat(context.getBean(Notifier.class)).isInstanceOf(LogNotifier.class);
        }
        List<String> candidates = ImportCandidates.load(AutoConfiguration.class, getClass().getClassLoader())
            .getCandidates();
        assertThat(candidates).contains(NotifierAutoConfiguration.class.getName());
    }

    @Test
    void example2WithoutTheJdbcModuleThereIsNoDataSource() {
        try (ConfigurableApplicationContext context = run(DatabaseApp.class)) {
            assertThat(context.getBeansOfType(DataSource.class)).isEmpty();
        }
    }

    @Test
    void example3OurOwnBeanMakesTheDefaultBackOff() {
        try (ConfigurableApplicationContext context = run(NotifierApp.class)) {
            assertThat(context.getBean(Notifier.class).name()).isEqualTo("log");
        }
        try (ConfigurableApplicationContext context = run(NotifierApp.class, "--spring.profiles.active=sms")) {
            assertThat(context.getBeansOfType(Notifier.class)).containsOnlyKeys("smsNotifier");
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource()
                .get(NotifierAutoConfiguration.class.getName() + "#notifier")
                .isFullMatch()).isFalse();
        }
    }

    @Test
    void example4OnMissingBeanInOurOwnConfigurationGivesTwoBeans() {
        try (ConfigurableApplicationContext context = run(TrapApp.class)) {
            assertThat(context.getBeansOfType(Notifier.class)).containsOnlyKeys("fallbackNotifier", "smsNotifier");
            assertThatExceptionOfType(NoUniqueBeanDefinitionException.class)
                .isThrownBy(() -> context.getBean(Notifier.class));
        }
    }

    @Test
    void example4TheFallbackBelongsInAnAutoConfiguration() {
        try (ConfigurableApplicationContext context = run(TrapFixedApp.class)) {
            assertThat(context.getBeansOfType(Notifier.class)).containsOnlyKeys("smsNotifier");
        }
    }

    @Test
    void example5OnlyTheExactPropertyNameSwitchesItOff() {
        try (ConfigurableApplicationContext context = run(PropertyApp.class)) {
            assertThat(context.getBeansOfType(Notifier.class)).hasSize(1);
        }
        try (ConfigurableApplicationContext context = run(PropertyApp.class, "--notifier.enable=false")) {
            assertThat(context.getBeansOfType(Notifier.class)).hasSize(1);
        }
        try (ConfigurableApplicationContext context = run(PropertyApp.class, "--notifier.enabled=false")) {
            assertThat(context.getBeansOfType(Notifier.class)).isEmpty();
        }
    }

}
