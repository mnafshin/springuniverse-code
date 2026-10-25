package info.mnafshin.springuniverse.config;

import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {

    /** What {@code GET /profile} returns. */
    public record ProfileInfo(List<String> activeProfiles, int dailyLimit, int warnAt, String notifier) {
    }

    private final Environment environment;
    private final SignupProperties props;
    private final SignupLimits limits;
    private final WelcomeNotifier notifier;

    public ProfileController(Environment environment, SignupProperties props, SignupLimits limits, WelcomeNotifier notifier) {
        this.environment = environment;
        this.props = props;
        this.limits = limits;
        this.notifier = notifier;
    }

    /** GET /profile: which profiles are active, and what they changed. */
    @GetMapping("/profile")
    public ProfileInfo profile() {
        return new ProfileInfo(List.of(environment.getActiveProfiles()), props.dailyLimit(), limits.warnAt(), notifier.kind());
    }
}
