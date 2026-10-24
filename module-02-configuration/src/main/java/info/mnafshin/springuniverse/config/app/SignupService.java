package info.mnafshin.springuniverse.config.app;

import java.time.Clock;
import java.time.Instant;

import org.springframework.stereotype.Service;

/** Found by component scanning. Its Clock comes from a @Bean method, because we can't annotate java.time.Clock. */
@Service
public class SignupService {

    private final UserRepository users;
    private final Notifier notifier;
    private final Clock clock;

    public SignupService(UserRepository users, Notifier notifier, Clock clock) {
        this.users = users;
        this.notifier = notifier;
        this.clock = clock;
    }

    public String register(String name) {
        users.save(name);
        String message = "Welcome, " + name + "! (signed up " + Instant.now(clock) + ")";
        notifier.send(name, message);
        return message;
    }
}
