package info.mnafshin.springuniverse.autoconfig;

import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport.ConditionAndOutcomes;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The same {@link ConditionEvaluationReport} that {@code --debug} prints, read in a test.
 * With the {@code sms} profile our own bean exists, so the default notifier's
 * {@code @ConditionalOnMissingBean} does not match.
 */
@SpringBootTest
@ActiveProfiles("sms")
class ConditionsReportTest {

    @Autowired
    ConfigurableListableBeanFactory beanFactory;

    @Test
    void reportExplainsWhyTheDefaultBackedOff() {
        Map<String, ConditionAndOutcomes> bySource = ConditionEvaluationReport.get(beanFactory)
            .getConditionAndOutcomesBySource();

        ConditionAndOutcomes classLevel = bySource
            .get("info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration");
        assertThat(classLevel.isFullMatch()).isTrue();

        ConditionAndOutcomes beanLevel = bySource
            .get("info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration#notifier");
        assertThat(beanLevel.isFullMatch()).isFalse();
        assertThat(beanLevel.iterator().next().getOutcome().getMessage())
            .contains("@ConditionalOnMissingBean")
            .contains("smsNotifier");
    }

}
