package info.mnafshin.springuniverse.container.proxies.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/** The same trap with @Async: send() is called on "this", so it runs on the caller's thread. */
@Service
public class ReportService {

    public CompletableFuture<String> finish() {
        return send(); // this.send(): skips the proxy, so not async
    }

    @Async
    public CompletableFuture<String> send() {
        return CompletableFuture.completedFuture(Thread.currentThread().getName());
    }
}
