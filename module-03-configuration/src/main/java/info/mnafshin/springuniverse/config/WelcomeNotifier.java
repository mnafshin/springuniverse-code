package info.mnafshin.springuniverse.config;

/** Tells a new user they signed up. One implementation per environment, chosen by profile. */
public interface WelcomeNotifier {

    /** A short name for the implementation, shown by {@code GET /profile}. */
    String kind();

    String welcome(String email);
}
