package info.mnafshin.springuniverse.autoconfig;

import info.mnafshin.springuniverse.notifier.LogNotifier;
import info.mnafshin.springuniverse.notifier.Notifier;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * With the {@code sms} and {@code pretty} profiles, the app defines its own {@link Notifier}
 * and its own {@code JsonMapper}. Both defaults back off.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({ "sms", "pretty" })
class OverrideTest {

    @Autowired
    MockMvcTester mvc;

    @Autowired
    ApplicationContext context;

    @Test
    void ourNotifierReplacesTheDefault() {
        assertThat(context.getBeansOfType(Notifier.class)).containsOnlyKeys("smsNotifier");
        assertThat(context.getBeansOfType(LogNotifier.class)).isEmpty();
    }

    @Test
    void ourJsonMapperReplacesBootsMapper() {
        assertThat(mvc.get().uri("/signup?name=Ada"))
            .hasStatusOk()
            .bodyText()
            .contains("\"notifier\" : \"sms\"")
            .contains("\n");
    }

}
