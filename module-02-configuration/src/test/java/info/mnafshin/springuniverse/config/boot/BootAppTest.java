package info.mnafshin.springuniverse.config.boot;

import static org.assertj.core.api.Assertions.assertThat;

import info.mnafshin.springuniverse.config.app.SignupService;
import info.mnafshin.springuniverse.config.outside.SmsGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;

@SpringBootTest
class BootAppTest {

    @Autowired
    ApplicationContext context;

    @Test
    void bootAppIsAConfigurationClassAndImportsAppConfig() {
        assertThat(context.getBean(SignupService.class).register("Ada")).startsWith("Welcome, Ada!");
        assertThat(AnnotatedElementUtils.hasAnnotation(BootApp.class, Configuration.class)).isTrue();   // via @SpringBootConfiguration
    }

    @Test
    void bootScansOnlyItsOwnPackageAndBelow() {
        assertThat(context.getBeanNamesForType(SmsGateway.class)).isEmpty();
    }
}
