package info.mnafshin.springuniverse.di.plain;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

/** Plain JUnit, no Spring: dependency injection is what makes the fake possible. */
class SignupServiceTest {

    @Test
    void sendsAWelcomeMessage() {
        var fake = new FakeNotifier();
        var signup = new SignupService(new InMemoryUserRepository(), fake);

        signup.register("Ada");

        assertThat(fake.sent).containsExactly("Ada: Welcome, Ada!");
    }

    @Test
    void savesTheUser() {
        var users = new InMemoryUserRepository();
        new SignupService(users, new FakeNotifier()).register("Ada");

        assertThat(users.findAll()).containsExactly("Ada");
    }

    @Test
    void hardWiredServiceAlwaysUsesTheRealEmail() {
        String out = captureStdout(() -> new HardWiredSignupService().register("Ada"));

        // No way to pass a fake: the "email" really goes out (here: to System.out).
        assertThat(out).contains("EMAIL to Ada: Welcome, Ada!");
    }

    @Test
    void compositionRootWiresTheRealObjects() {
        String out = captureStdout(() -> PlainJavaApp.main(new String[0]));

        assertThat(out).isEqualToIgnoringNewLines("EMAIL to Ada: Welcome, Ada!");
    }

    private static String captureStdout(Runnable action) {
        PrintStream original = System.out;
        var buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true));
        try {
            action.run();
        } finally {
            System.setOut(original);
        }
        return buffer.toString();
    }
}
