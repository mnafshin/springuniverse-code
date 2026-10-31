package info.mnafshin.springuniverse.shop;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.ConfigurableApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/** The T5.1 shop app, plain (no JDBC, no H2): candidates come only from Spring Boot's jars, and there is no data source. */
class ShopAppTest {

    @Test
    void plainRunHasNoDataSource() {
        try (ConfigurableApplicationContext context = SpringApplication.run(ShopApp.class)) {
            assertThat(context.getBeansOfType(DataSource.class)).isEmpty();
        }
    }

    @Test
    void everyCandidateComesFromSpringBoot() {
        assertThat(ImportCandidates.load(AutoConfiguration.class, ShopApp.class.getClassLoader()).getCandidates())
            .isNotEmpty()
            .allMatch(name -> name.startsWith("org.springframework.boot."));
    }

}
