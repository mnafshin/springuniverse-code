package info.mnafshin.springuniverse.container.checkout.retry;

import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;

/** The fix: the @Retryable method lives in another bean and is called through its proxy. */
@Service
public class PaymentClient {

    private int attempts;

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
