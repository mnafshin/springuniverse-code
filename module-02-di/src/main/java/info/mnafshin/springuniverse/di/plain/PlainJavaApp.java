package info.mnafshin.springuniverse.di.plain;

/**
 * Step 3, the composition root: one place that creates the objects and wires them.
 * This is the job the Spring container takes over.
 */
public class PlainJavaApp {

    public static void main(String[] args) {
        UserRepository users = new InMemoryUserRepository();
        Notifier notifier = new EmailNotifier();
        SignupService signup = new SignupService(users, notifier);

        signup.register("Ada");
    }
}
