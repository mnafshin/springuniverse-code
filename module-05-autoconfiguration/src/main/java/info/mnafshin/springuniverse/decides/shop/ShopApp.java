package info.mnafshin.springuniverse.decides.shop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * T5.1 (rework, ADR-0019): the one shop app that runs through the whole video.
 * It prints how many auto-configuration candidates Spring Boot read, which notifier it got,
 * and how many data sources exist. Run it:
 * <ul>
 * <li>plain: 31 candidates, notifier log, 0 data sources;</li>
 * <li>with {@code -Pjdbc} (the JDBC starter): it fails to start, "Failed to configure a DataSource";</li>
 * <li>with {@code -Pjdbc,h2}: 44 candidates and one in-memory H2 data source;</li>
 * <li>with {@code --spring.profiles.active=sms}: our own notifier wins, the default backs off;</li>
 * <li>with {@code --notifier.enable=false} (typo): nothing changes; {@code --notifier.enabled=false}: no notifier.</li>
 * </ul>
 */
@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(ShopApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            List<String> candidates = ImportCandidates.load(AutoConfiguration.class, ShopApp.class.getClassLoader())
                .getCandidates();
            System.out.println("candidates: " + candidates.size());

            Notifier notifier = context.getBeanProvider(Notifier.class).getIfAvailable();
            System.out.println("notifier: " + (notifier == null ? "none" : notifier.name()));

            Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class);
            System.out.println("data sources: " + dataSources.size());
            for (DataSource dataSource : dataSources.values()) {
                try (Connection connection = dataSource.getConnection()) {
                    System.out.println("  " + connection.getMetaData().getURL());
                }
            }
        }
    }

}
