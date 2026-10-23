package info.mnafshin.springuniverse.hello;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.catalina.util.ServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.core.SpringVersion;

/**
 * T1.2: we never wrote a version for Spring, Tomcat or Jackson. The pom passes the Spring Boot BOM's
 * version properties to the tests (see surefire's systemPropertyVariables), and we check that exactly
 * those versions ended up on the classpath.
 */
class ManagedVersionsTest {

    @Test
    void springFrameworkComesFromTheBom() {
        assertThat(SpringVersion.getVersion()).isEqualTo(System.getProperty("bom.spring-framework.version"));
    }

    @Test
    void tomcatComesFromTheBom() {
        assertThat(ServerInfo.getServerNumber()).startsWith(System.getProperty("bom.tomcat.version"));
    }

    @Test
    void jacksonComesFromTheBom() {
        assertThat(tools.jackson.databind.cfg.PackageVersion.VERSION.toString())
                .isEqualTo(System.getProperty("bom.jackson-bom.version"));
    }
}
