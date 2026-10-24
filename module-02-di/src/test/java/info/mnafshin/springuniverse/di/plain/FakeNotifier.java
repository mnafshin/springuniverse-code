package info.mnafshin.springuniverse.di.plain;

import java.util.ArrayList;
import java.util.List;

/** A test double: records messages instead of sending them. */
class FakeNotifier implements Notifier {

    final List<String> sent = new ArrayList<>();

    @Override
    public void send(String to, String message) {
        sent.add(to + ": " + message);
    }
}
