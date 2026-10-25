package info.mnafshin.springuniverse.autoconfig;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** {@code GET /signup?name=Ada} notifies the user and shows which {@link Notifier} did it. */
@RestController
public class SignupController {

    private final Notifier notifier;

    public SignupController(Notifier notifier) {
        this.notifier = notifier;
    }

    @GetMapping("/signup")
    public SignupReceipt signup(@RequestParam String name) {
        String result = notifier.send(name, "Welcome, " + name + "!");
        return new SignupReceipt(name, notifier.name(), result);
    }

    public record SignupReceipt(String name, String notifier, String result) {
    }

}
