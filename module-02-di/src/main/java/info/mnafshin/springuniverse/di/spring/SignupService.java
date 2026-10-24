package info.mnafshin.springuniverse.di.spring;

import org.springframework.stereotype.Service;

/**
 * The same class as the plain-Java SignupService, plus one annotation.
 * One constructor, so no @Autowired is needed: Spring calls it with the matching beans.
 */
@Service
public class SignupService {

    private final UserRepository users;
    private final Notifier notifier;

    public SignupService(UserRepository users, Notifier notifier) {
        this.users = users;
        this.notifier = notifier;
    }

    public String register(String name) {
        users.save(name);
        String message = "Welcome, " + name + "!";
        notifier.send(name, message);
        return message;
    }
}
