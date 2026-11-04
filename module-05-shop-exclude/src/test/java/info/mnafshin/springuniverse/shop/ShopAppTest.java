package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * T5.4: the JDBC starter is on the classpath, but spring.autoconfigure.exclude turns off
 * DataSourceAutoConfiguration, so the shop starts without a database and still serves the price.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopAppTest {

    @Autowired
    ApplicationContext context;

    @LocalServerPort
    int port;

    @Test
    void startsWithoutADataSource() {
        assertThat(context.getBeanNamesForType(DataSource.class)).isEmpty();
        String body = RestClient.create("http://localhost:" + port).get().uri("/").retrieve().body(String.class);
        assertThat(body).isEqualTo("Coffee: 4.50 USD");
    }

}
