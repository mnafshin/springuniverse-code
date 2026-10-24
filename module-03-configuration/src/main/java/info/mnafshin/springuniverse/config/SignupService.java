package info.mnafshin.springuniverse.config;

import org.springframework.stereotype.Service;

@Service
public class SignupService {

    private final SignupProperties props;

    public SignupService(SignupProperties props) {
        this.props = props;
    }

    public String welcome(String name) {
        return props.welcomeMessage() + ", " + name + "!";
    }

    public int dailyLimit() {
        return props.dailyLimit();
    }
}
