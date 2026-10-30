package info.mnafshin.springuniverse.shopdb;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class MyDataSourceAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(MyDataSourceAutoConfiguration.class));

    @Test
    void createsTheDataSourceWhenTheAppHasNone() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(DataSource.class).hasBean("dataSource");
            assertThat(((JdbcDataSource) context.getBean(DataSource.class)).getURL()).isEqualTo("jdbc:h2:mem:ours");
        });
    }

    @Test
    void backsOffWhenTheAppHasItsOwnDataSource() {
        runner.withUserConfiguration(ShopDataSource.class).run(context -> {
            assertThat(context).hasSingleBean(DataSource.class).hasBean("shopDataSource").doesNotHaveBean("dataSource");
        });
    }

    @Test
    void theWholeClassIsSkippedWithoutH2() {
        runner.withClassLoader(new FilteredClassLoader(JdbcDataSource.class))
            .run(context -> assertThat(context).doesNotHaveBean(MyDataSourceAutoConfiguration.class).doesNotHaveBean(DataSource.class));
    }

    @Configuration(proxyBeanMethods = false)
    static class ShopDataSource {

        @Bean
        DataSource shopDataSource() {
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:mem:shop");
            return dataSource;
        }

    }

}
