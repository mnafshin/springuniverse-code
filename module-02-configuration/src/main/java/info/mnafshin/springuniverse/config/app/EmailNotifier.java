package info.mnafshin.springuniverse.config.app;

import org.springframework.stereotype.Component;

/** Our own class: one annotation and component scanning finds it. */
@Component
public class EmailNotifier implements Notifier {

    @Override
    public void send(String to, String message) {
        System.out.println("EMAIL to " + to + ": " + message);
    }
}
