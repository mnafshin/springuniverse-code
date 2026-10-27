package info.mnafshin.springuniverse.container.checkout.mail;

import org.springframework.stereotype.Service;

@Service
public class FixedOrderService {

    private final ConfirmationMailer mailer; // the mailer's proxy

    public FixedOrderService(ConfirmationMailer mailer) {
        this.mailer = mailer;
    }

    public void checkout(String id) {
        // ... save the order
        mailer.send(id);
    }
}
