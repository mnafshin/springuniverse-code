package info.mnafshin.springuniverse.notifier;

/**
 * Sends a short message to a user. The "library" side of this module: an app gets a
 * default {@link LogNotifier} from {@link NotifierAutoConfiguration} unless it defines
 * its own {@code Notifier} bean.
 */
public interface Notifier {

    /** A short name that shows which implementation is active, for example {@code "log"}. */
    String name();

    /** Sends {@code message} to {@code to} and returns a one-line description of what happened. */
    String send(String to, String message);

}
