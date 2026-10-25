package info.mnafshin.springuniverse.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Only in prod: the notifier that uses the mail server from {@link MailProperties}.
 * Sending real mail is out of scope for this module, so this stand-in only reports the server it would use.
 */
@Component
@Profile("prod")
public class SmtpWelcomeNotifier implements WelcomeNotifier {

    private final MailProperties mail;

    public SmtpWelcomeNotifier(MailProperties mail) {
        this.mail = mail;
    }

    @Override
    public String kind() {
        return "smtp";
    }

    @Override
    public String welcome(String email) {
        return "Welcome mail to " + email + " via " + mail.host() + " as " + mail.username();
    }
}
