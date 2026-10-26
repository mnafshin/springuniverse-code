package info.mnafshin.springuniverse.container.checkout.app;

import org.springframework.stereotype.Service;

/** The whole checkout: every call to an annotated method goes to another bean, so every call passes a proxy. */
@Service
public class CheckoutService {

    private final OrderWriter orders;
    private final Payments payments;
    private final ConfirmationMailer mailer;

    public CheckoutService(OrderWriter orders, Payments payments, ConfirmationMailer mailer) {
        this.orders = orders;
        this.payments = payments;
        this.mailer = mailer;
    }

    public void checkout(String id, String... items) {
        int total = orders.save(id, items);
        payments.charge(id, total);
        System.out.println("confirmation sent on: " + mailer.send(id).join());
    }
}
