package info.mnafshin.springuniverse.config;

import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

/**
 * Mail server settings, only needed (and only bound) in production.
 * The password is never written in a committed file: set {@code APP_MAIL_PASSWORD} in the environment.
 * Without it, the prod profile refuses to start.
 */
@Profile("prod")
@Validated
@ConfigurationProperties("app.mail")
public record MailProperties(@NotBlank String host, @NotBlank String username, @NotBlank String password) {

    /** Records print every component by default; keep the secret out of logs. */
    @Override
    public String toString() {
        return "MailProperties[host=" + host + ", username=" + username + ", password=******]";
    }
}
