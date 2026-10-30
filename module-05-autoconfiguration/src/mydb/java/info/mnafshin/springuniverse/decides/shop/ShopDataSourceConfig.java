package info.mnafshin.springuniverse.decides.shop;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * T5.1 v5, run 2: the shop defines its own data source (Spring profile {@code owndb}, Maven profile {@code mydb}).
 * {@code MyDataSourceAutoConfiguration}'s {@code @ConditionalOnMissingBean} finds this bean, so ours backs off.
 */
@Configuration(proxyBeanMethods = false)
@Profile("owndb")
class ShopDataSourceConfig {

    @Bean
    DataSource shopDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:shop");
        return dataSource;
    }

}
