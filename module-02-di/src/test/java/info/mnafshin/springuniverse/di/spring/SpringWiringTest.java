package info.mnafshin.springuniverse.di.spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

/** What the container does for us, and the two errors you meet when it can't. */
@SpringBootTest
class SpringWiringTest {

    @Autowired
    ApplicationContext context;

    @Test
    void theContainerCreatedAndWiredTheBeans() {
        SignupService signup = context.getBean(SignupService.class);

        assertThat(ReflectionTestUtils.getField(signup, "notifier")).isInstanceOf(EmailNotifier.class);
        assertThat(ReflectionTestUtils.getField(signup, "users")).isSameAs(context.getBean(UserRepository.class));
    }

    @Test
    void oneSharedInstancePerBeanByDefault() {
        assertThat(context.getBean(SignupService.class)).isSameAs(context.getBean(SignupService.class));
    }

    @Test
    void noNotifierBeanMeansTheAppDoesNotStart() {
        new ApplicationContextRunner()
                .withUserConfiguration(SignupService.class, InMemoryUserRepository.class)
                .run(ctx -> {
                    assertThat(ctx).hasFailed();
                    assertThat(ctx.getStartupFailure())
                            .rootCause()
                            .isInstanceOf(NoSuchBeanDefinitionException.class)
                            .hasMessageContaining(Notifier.class.getName());
                });
    }

    @Test
    void twoNotifierBeansAreAmbiguous() {
        new ApplicationContextRunner()
                .withUserConfiguration(SignupService.class, InMemoryUserRepository.class, EmailNotifier.class)
                .withBean("smsNotifier", Notifier.class, () -> (to, message) -> { })
                .run(ctx -> {
                    assertThat(ctx).hasFailed();
                    assertThat(ctx.getStartupFailure())
                            .rootCause()
                            .isInstanceOf(NoUniqueBeanDefinitionException.class)
                            .hasMessageContaining("expected single matching bean but found 2");
                });
    }
}
