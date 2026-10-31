package info.mnafshin.springuniverse.shop;

import java.sql.Connection;

import javax.sql.DataSource;

import info.mnafshin.springuniverse.shopdb.MyDataSourceAutoConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * T5.1, "inside one auto-configuration" (run with {@code -Pmydb}): the same {@link MyDataSourceAutoConfiguration}
 * decides both ways. Run 1: both conditions match and ours makes the data source. Run 2 (the shop's own data source):
 * the class still matches, the method's condition finds the shop's bean, so ours backs off.
 */
class ShopMyDataSourceTest {

    static ConfigurableApplicationContext run(String... args) {
        return SpringApplication.run(ShopApp.class, args);
    }

    @Test
    void theSameAutoConfigurationDecidesBothWays() throws Exception {
        String type = MyDataSourceAutoConfiguration.class.getName();
        try (ConfigurableApplicationContext context = run()) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource().get(type).isFullMatch()).isTrue();
            assertThat(report.getConditionAndOutcomesBySource().get(type + "#dataSource").isFullMatch()).isTrue();
            assertThat(context.getBeansOfType(DataSource.class)).containsOnlyKeys("dataSource");
            assertThat(url(context.getBean(DataSource.class))).isEqualTo("jdbc:h2:mem:ours");
        }
        try (ConfigurableApplicationContext context = run("--spring.profiles.active=owndb")) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource().get(type).isFullMatch()).isTrue();
            assertThat(report.getConditionAndOutcomesBySource().get(type + "#dataSource").isFullMatch()).isFalse();
            assertThat(context.getBeansOfType(DataSource.class)).containsOnlyKeys("shopDataSource");
            assertThat(url(context.getBean(DataSource.class))).isEqualTo("jdbc:h2:mem:shop");
        }
    }

    @Test
    void theExactPropertyNameDecides() {
        String type = MyDataSourceAutoConfiguration.class.getName();
        try (ConfigurableApplicationContext context = run("--shop.datasource.enable=false")) {
            assertThat(context.getBeansOfType(DataSource.class)).containsOnlyKeys("dataSource");
        }
        try (ConfigurableApplicationContext context = run("--shop.datasource.enabled=false")) {
            ConditionEvaluationReport report = ConditionEvaluationReport.get(context.getBeanFactory());
            assertThat(report.getConditionAndOutcomesBySource().get(type).isFullMatch()).isFalse();
            assertThat(context.getBeansOfType(DataSource.class)).isEmpty();
        }
    }

    private static String url(DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getURL();
        }
    }

}
