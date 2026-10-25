package info.mnafshin.springuniverse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A little SpEL that is still useful.
 * <ul>
 * <li>{@code ${...}} is a property placeholder: it looks a key up (with an optional default after the colon).</li>
 * <li>{@code #{...}} is a SpEL expression: it is evaluated. Placeholders inside it are resolved first.</li>
 * </ul>
 */
@Component
public class SignupLimits {

    private final int warnAt;
    private final String supportEmail;

    public SignupLimits(@Value("#{${app.signup.daily-limit} * 8 / 10}") int warnAt,
                        @Value("${app.signup.support-email:support@example.com}") String supportEmail) {
        this.warnAt = warnAt;
        this.supportEmail = supportEmail;
    }

    /** Warn when 80% of today's limit is used: 80 of 100, 4 of 5, 800 of 1000. */
    public int warnAt() {
        return warnAt;
    }

    /** No key in any file, so the default after the colon is used. */
    public String supportEmail() {
        return supportEmail;
    }
}
