package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.servlet.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;

/**
 * T5.2 v2: our customizer changes Spring Boot's own web server factory; it does not replace it.
 * Spring Boot's tomcatServletWebServerFactory is still the only factory, with our second connector on 8081.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopAppTest {

    @Autowired
    ApplicationContext context;

    @Test
    void springBootStillBuildsTheServerAndOurCustomizerAddsTheSecondPort() {
        assertThat(context.getBeanNamesForType(ServletWebServerFactory.class))
            .containsExactly("tomcatServletWebServerFactory");
        TomcatServletWebServerFactory factory = context.getBean(TomcatServletWebServerFactory.class);
        assertThat(factory.getAdditionalConnectors()).singleElement()
            .satisfies((connector) -> assertThat(connector.getPort()).isEqualTo(8081));
    }

}
