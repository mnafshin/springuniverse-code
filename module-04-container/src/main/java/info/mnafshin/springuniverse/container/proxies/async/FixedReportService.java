package info.mnafshin.springuniverse.container.proxies.async;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Service;

@Service
public class FixedReportService {

    private final Mailer mailer;

    public FixedReportService(Mailer mailer) {
        this.mailer = mailer;
    }

    public CompletableFuture<String> finish() {
        return mailer.send();
    }
}
