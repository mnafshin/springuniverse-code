package info.mnafshin.springuniverse.di.plain;

/**
 * Step 1, the problem: the service builds its own collaborators with {@code new}.
 * Every test of {@link #register} prints (in real life: sends) an email, and there is
 * no way to swap the email for SMS or a fake without editing this class.
 */
public class HardWiredSignupService {

    private final UserRepository users = new InMemoryUserRepository();
    private final EmailNotifier notifier = new EmailNotifier();

    public String register(String name) {
        users.save(name);
        String message = "Welcome, " + name + "!";
        notifier.send(name, message);
        return message;
    }
}
