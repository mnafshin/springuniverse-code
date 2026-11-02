package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.autoconfigure.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Short 049: ServerProperties binds only the names it has fields for. With the typo server.prot the port stays null
 * (Tomcat keeps its own default 8080); with server.port it is 9090.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ShopAppTest {

    @Autowired
    ServerProperties server;

    @Test
    void portIsBoundFromTheProperty() {
        assertThat(server.getPort()).isEqualTo(9090);
    }

}
