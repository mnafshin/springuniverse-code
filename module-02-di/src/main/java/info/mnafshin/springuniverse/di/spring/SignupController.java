package info.mnafshin.springuniverse.di.spring;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SignupController {

    private final SignupService signup;

    public SignupController(SignupService signup) {
        this.signup = signup;
    }

    @PostMapping("/signups")
    public Welcome register(@RequestParam String name) {
        return new Welcome(name, signup.register(name));
    }
}
