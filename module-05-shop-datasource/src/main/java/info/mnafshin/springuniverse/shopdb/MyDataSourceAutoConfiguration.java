package info.mnafshin.springuniverse.shopdb;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * T5.1 v5: a small DataSource auto-configuration, written by hand. It has the same shape as Spring Boot's own
 * {@code DataSourceAutoConfiguration}, just much smaller.
 *
 * <ul>
 * <li>It is listed in this jar's {@code META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports},
 * so Spring Boot treats it as a candidate.</li>
 * <li>{@code @ConditionalOnClass} on the class: the whole class is used only if H2's {@link JdbcDataSource} is on the
 * classpath.</li>
 * <li>{@code @ConditionalOnMissingBean} on the method: the default data source is created only if the app has no
 * {@link DataSource} bean of its own. Define one and this default backs off.</li>
 * </ul>
 */
@AutoConfiguration
@ConditionalOnClass(JdbcDataSource.class)
public class MyDataSourceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    DataSource dataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:ours");
        return dataSource;
    }

}
