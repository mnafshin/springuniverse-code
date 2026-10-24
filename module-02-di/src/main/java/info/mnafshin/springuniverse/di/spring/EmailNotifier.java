package info.mnafshin.springuniverse.di.spring;

import org.springframework.stereotype.Component;

@Component
public class EmailNotifier implements Notifier {

    @Override
    public void send(String to, String message) {
        System.out.println("EMAIL to " + to + ": " + message);
    }
}
