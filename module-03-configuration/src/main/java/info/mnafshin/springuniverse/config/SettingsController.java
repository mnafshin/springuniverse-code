package info.mnafshin.springuniverse.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingsController {

    private final SignupProperties props;

    public SettingsController(SignupProperties props) {
        this.props = props;
    }

    /** GET /settings: the bound values, so you can see which property source won. */
    @GetMapping("/settings")
    public SignupProperties settings() {
        return props;
    }
}
