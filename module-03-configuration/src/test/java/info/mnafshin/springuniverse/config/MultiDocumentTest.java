package info.mnafshin.springuniverse.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/** One YAML file with ---: the second document only applies when "prod | staging" matches. */
class MultiDocumentTest {

    private static int dailyLimit(String... args) {
        String[] all = new String[args.length + 2];
        all[0] = "--server.port=0";
        all[1] = "--spring.config.location=classpath:/multi-document/application.yml";
        System.arraycopy(args, 0, all, 2, args.length);
        try (ConfigurableApplicationContext ctx = SpringApplication.run(ConfigApplication.class, all)) {
            return ctx.getBean(SignupProperties.class).dailyLimit();
        }
    }

    @Test
    void secondDocumentIsSkippedWithoutAMatchingProfile() {
        assertThat(dailyLimit()).isEqualTo(100);
    }

    @Test
    void secondDocumentAppliesForStaging() {
        assertThat(dailyLimit("--spring.profiles.active=staging")).isEqualTo(1000);
    }
}
