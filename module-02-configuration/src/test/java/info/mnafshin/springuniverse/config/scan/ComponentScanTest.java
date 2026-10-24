package info.mnafshin.springuniverse.config.scan;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import info.mnafshin.springuniverse.config.app.AppConfig;
import info.mnafshin.springuniverse.config.outside.SmsGateway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ComponentScanTest {

    @Test
    void componentOutsideTheScannedPackageIsNotABean() {
        try (var context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            assertThatThrownBy(() -> context.getBean(SmsGateway.class))
                    .isInstanceOf(NoSuchBeanDefinitionException.class)
                    .hasMessageContaining("No qualifying bean of type '" + SmsGateway.class.getName() + "' available");
        }
    }

    @Test
    void scanningThatPackageToo() {
        try (var context = new AnnotationConfigApplicationContext()) {
            context.register(AppConfig.class);
            context.scan("info.mnafshin.springuniverse.config.outside");
            context.refresh();

            assertThat(context.getBean(SmsGateway.class)).isNotNull();
        }
    }
}
