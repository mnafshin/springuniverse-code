package info.mnafshin.springuniverse.container.proxies.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** The fix: the @Async method lives in another bean and is called through its proxy. */
@Service
public class Mailer {

    @Async
    public CompletableFuture<String> send() {
        return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }
}
