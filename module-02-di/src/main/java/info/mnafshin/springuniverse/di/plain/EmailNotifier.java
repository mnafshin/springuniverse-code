package info.mnafshin.springuniverse.di.plain;

/** Stands in for a real email client: it only prints. */
public class EmailNotifier implements Notifier {

    @Override
    public void send(String to, String message) {
        System.out.println("EMAIL to " + to + ": " + message);
    }
}
