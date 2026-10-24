package info.mnafshin.springuniverse.di.plain;

/**
 * Step 2, dependency injection: the service only asks for what it needs, in its constructor.
 * It does not know (or care) which Notifier or UserRepository it gets.
 */
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
