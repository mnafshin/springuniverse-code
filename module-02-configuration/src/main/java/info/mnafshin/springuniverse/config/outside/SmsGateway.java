package info.mnafshin.springuniverse.config.outside;

import org.springframework.stereotype.Component;

/**
 * A @Component in a package that no configuration scans: ...config.outside is a sibling of ...config.app and
 * ...config.boot, not below them. So it never becomes a bean, even though it has the annotation.
 */
@Component
public class SmsGateway {

    public void send(String to, String message) {
        System.out.println("SMS to " + to + ": " + message);
    }
}
