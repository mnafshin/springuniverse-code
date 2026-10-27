package info.mnafshin.springuniverse.container.checkout.mail;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** The fix: the @Async method lives in another bean, so the call from FixedOrderService goes through its proxy. */
@Service
public class ConfirmationMailer {

    private final MailServer mailServer;

    public ConfirmationMailer(MailServer mailServer) {
        this.mailServer = mailServer;
    }

    @Async
    public void send(String id) {
        mailServer.send(id); // takes 3 seconds, now on another thread
    }
}
