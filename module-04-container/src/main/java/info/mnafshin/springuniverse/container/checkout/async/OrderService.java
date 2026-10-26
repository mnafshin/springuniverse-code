package info.mnafshin.springuniverse.container.checkout.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** The same trap with @Async: sendConfirmation() is called on "this", so it runs on the caller's thread. */
@Service
public class OrderService {

    public CompletableFuture<String> checkout(String id) {
        return sendConfirmation(id); // this.sendConfirmation(): skips the proxy, so not async
    }

    @Async
    public CompletableFuture<String> sendConfirmation(String id) {
        return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }
}
