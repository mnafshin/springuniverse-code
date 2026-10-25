package info.mnafshin.springuniverse.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/** Everywhere except prod (dev, test, or no profile at all): just log, never send real mail. */
@Component
@Profile("!prod")
public class LogWelcomeNotifier implements WelcomeNotifier {

    private static final Logger log = LoggerFactory.getLogger(LogWelcomeNotifier.class);

    @Override
    public String kind() {
        return "log";
    }

    @Override
    public String welcome(String email) {
        String message = "Welcome mail to " + email + " (logged only, not sent)";
        log.info(message);
        return message;
    }
}
