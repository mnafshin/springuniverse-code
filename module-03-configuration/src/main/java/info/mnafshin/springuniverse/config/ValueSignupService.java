package info.mnafshin.springuniverse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The same values read one by one with {@code @Value}: it works, but the keys are strings scattered across classes,
 * with no grouping and no validation. Compare with {@link SignupProperties}.
 */
@Component
public class ValueSignupService {

    private final String welcomeMessage;
    private final int dailyLimit;

    public ValueSignupService(@Value("${app.signup.welcome-message}") String welcomeMessage,
                              @Value("${app.signup.daily-limit}") int dailyLimit) {
        this.welcomeMessage = welcomeMessage;
        this.dailyLimit = dailyLimit;
    }

    public String welcomeMessage() {
        return welcomeMessage;
    }

    public int dailyLimit() {
        return dailyLimit;
    }
}
