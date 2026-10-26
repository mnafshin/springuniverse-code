package info.mnafshin.springuniverse.decides;

import info.mnafshin.springuniverse.notifier.Notifier;

/** A {@link Notifier} of our own, used by the examples that define their own bean. */
public class SmsNotifier implements Notifier {

    @Override
    public String name() {
        return "sms";
    }

    @Override
    public String send(String to, String message) {
        return "texted " + to;
    }

}
