package info.mnafshin.springuniverse.shop;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * T5.1 (v6): the one shop app that runs through the whole video. It prints how many auto-configuration candidates
 * Spring Boot read, and how many data sources exist (with each one's URL). Runs:
 * <ul>
 * <li>plain: no data source;</li>
 * <li>{@code -Pjdbc} (the JDBC starter): it fails to start, "Failed to configure a DataSource";</li>
 * <li>{@code -Pjdbc,h2}: Spring Boot builds one in-memory H2 data source;</li>
 * <li>{@code -Pmydb}: our own MyDataSourceAutoConfiguration builds it ({@code jdbc:h2:mem:ours}); with
 * {@code --spring.profiles.active=owndb} the shop's own data source wins and ours backs off.</li>
 * </ul>
 */
@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext context = SpringApplication.run(ShopApp.class, args)) {
            List<String> candidates = ImportCandidates.load(AutoConfiguration.class, ShopApp.class.getClassLoader())
                .getCandidates();
            System.out.println("candidates: " + candidates.size());

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
