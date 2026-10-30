package info.mnafshin.springuniverse.decides.shop;

import java.sql.Connection;

import javax.sql.DataSource;

import info.mnafshin.springuniverse.shopdb.MyDataSourceAutoConfiguration;
import org.junit.jupiter.api.Test;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.condition.ConditionEvaluationReport;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * T5.1 v5, "inside one auto-configuration" (run with {@code -Pmydb}): the same {@link MyDataSourceAutoConfiguration}
 * decides both ways. Run 1: both conditions match and ours makes the data source. Run 2 (the shop's own data source):
 * the class still matches, the method's condition finds the shop's bean, so ours backs off.
 */
class ShopMyDataSourceTest {

    static ConfigurableApplicationContext run(String... args) {
        return new SpringApplicationBuilder(ShopApp.class).web(WebApplicationType.NONE).run(args);
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

    private static String url(DataSource dataSource) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            return connection.getMetaData().getURL();
        }
    }

}
