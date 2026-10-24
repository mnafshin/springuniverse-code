package info.mnafshin.springuniverse.config;

import java.time.Duration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * Every key under {@code app.signup}, bound once at startup, type-safe and validated.
 * A record has one constructor, so Spring Boot uses constructor binding (no {@code @ConstructorBinding} needed).
 */
@Validated
@ConfigurationProperties("app.signup")
public record SignupProperties(
        @NotBlank String welcomeMessage,
        @Min(1) int dailyLimit,
        @DefaultValue("30m") Duration linkExpiry) {
}
