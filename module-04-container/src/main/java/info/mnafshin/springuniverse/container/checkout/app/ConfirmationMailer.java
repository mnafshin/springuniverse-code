package info.mnafshin.springuniverse.container.checkout.app;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationMailer {

    @Async
    public CompletableFuture<String> send(String id) {
        return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }
}
