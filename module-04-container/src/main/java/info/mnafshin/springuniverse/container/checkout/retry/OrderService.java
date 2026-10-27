package info.mnafshin.springuniverse.container.checkout.retry;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

/** The same trap with @Retryable: checkout() calls charge() on "this", so a failure is never retried. */
@Service
public class OrderService {

    private int attempts;

    public String checkout(String id) {
        return charge(id); // this.charge(): skips the proxy, so no retry
    }

    @Retryable(maxRetries = 2, delay = 100)
    public String charge(String id) {
        attempts++;
        System.out.println("charge attempt " + attempts);
        if (attempts == 1) {
            throw new IllegalStateException("payment provider busy");
        }
        return id + " charged";
    }
}
