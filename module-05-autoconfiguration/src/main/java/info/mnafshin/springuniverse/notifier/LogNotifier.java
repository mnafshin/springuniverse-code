package info.mnafshin.springuniverse.notifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** The default {@link Notifier}: it only writes the message to the log. */
public class LogNotifier implements Notifier {

    private static final Logger log = LoggerFactory.getLogger(LogNotifier.class);

    @Override
    public String name() {
        return "log";
    }

    @Override
    public String send(String to, String message) {
        log.info("to {}: {}", to, message);
        return "logged for " + to;
    }

}
