package info.mnafshin.springuniverse.decides.onclass;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 2: what's on the classpath decides. Run it three ways (see the module README):
 * plain (no JDBC module: no candidate, no DataSource), with {@code spring-boot-starter-jdbc}
 * (a DataSource is wanted, but there is no database: startup fails), and with H2 added
 * (an embedded database is detected and configured).
 */
@SpringBootApplication
public class DatabaseApp {

    public static void main(String[] args) throws SQLException {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(DatabaseApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            Map<String, DataSource> dataSources = context.getBeansOfType(DataSource.class);
            System.out.println("DataSource beans: " + dataSources.size());
            for (DataSource dataSource : dataSources.values()) {
                try (Connection connection = dataSource.getConnection()) {
                    System.out.println("database: " + connection.getMetaData().getURL());
                }
            }
        }
    }

}
