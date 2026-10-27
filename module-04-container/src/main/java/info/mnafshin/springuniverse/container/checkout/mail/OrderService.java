package info.mnafshin.springuniverse.container.checkout.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * T4.2 cold open: checkout() calls its own @Async sendConfirmation() through "this". The call skips the proxy,
 * so the email is sent on the caller's thread (main) and the customer waits for the slow mail server.
 */
@Service
public class OrderService {

    private final MailServer mailServer;

    public OrderService(MailServer mailServer) {
        this.mailServer = mailServer;
    }

    public void checkout(String id) {
        // ... save the order
        sendConfirmation(id); // this.sendConfirmation(id): skips the proxy, so not async
    }

    @Async
    public void sendConfirmation(String id) {
        mailServer.send(id); // takes 3 seconds
    }
}
