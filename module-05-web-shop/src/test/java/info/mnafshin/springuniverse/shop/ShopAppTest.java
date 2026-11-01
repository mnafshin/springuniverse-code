package info.mnafshin.springuniverse.shop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.servlet.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;

/**
 * T5.1 v7: the shop's own web server factory wins, so Spring Boot's tomcatServletWebServerFactory
 * (@ConditionalOnMissingBean) is not created.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShopAppTest {

    @Autowired
    ApplicationContext context;

    @Test
    void ourFactoryWinsAndSpringBootsBacksOff() {
        assertThat(context.getBeanNamesForType(ServletWebServerFactory.class)).containsExactly("shopServer");
        assertThat(context.containsBean("tomcatServletWebServerFactory")).isFalse();
    }

}
