package info.mnafshin.springuniverse.container.checkout.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service
public class FixedOrderService {

    private final ConfirmationMailer mailer;

    public FixedOrderService(ConfirmationMailer mailer) {
        this.mailer = mailer;
    }

    public CompletableFuture<String> checkout(String id) {
        return mailer.send(id);
    }
}
