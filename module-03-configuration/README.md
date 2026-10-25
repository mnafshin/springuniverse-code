# Module 3: Configuration and properties

One small signup app whose settings live outside the code: `application.yml`, environment variables and command-line arguments, bound into a type-safe, validated record (T3.1). Then the same jar behaves differently in dev, test and prod through profiles, and a little SpEL computes a value from a property (T3.2).

| Video | What it shows |
|---|---|
| T3.1 External configuration: `application.yml`, precedence and `@ConfigurationProperties` (tutorial) | YAML vs properties, `@Value` vs a `@ConfigurationProperties` record, defaults and validation at startup, property sources and which one wins |
| Short 016 | Stop using `@Value` everywhere: `@ConfigurationProperties` with a record |
| Short 017 | Which property wins? Precedence in 30 seconds |
| Short 018 | Any Spring property as an environment variable |
| Short 019 | Bad config? Make Spring Boot refuse to start |
| T3.2 Profiles for dev/test/prod, plus a little SpEL that is still useful (tutorial) | `application-{profile}.yml` merged key by key, `spring.profiles.active` (command line or `SPRING_PROFILES_ACTIVE`), the `default` profile, last active profile wins, `@Profile` beans, `@ActiveProfiles` in tests, multi-document YAML, secrets from the environment, `${...}` vs `#{...}` |
| Short 020 | Profiles: one app, three environments |
| Short 021 | Secrets don't belong in application.yml |
| Short 022 | `${}` or `#{}`: the one SpEL expression you'll actually use |

Videos: https://www.youtube.com/@springuniverse

## Files
| File | Role |
|---|---|
| `application.yml` | `app.signup.welcome-message`, `daily-limit`, `link-expiry` (kebab-case keys) |
| `SignupProperties` | `@ConfigurationProperties("app.signup")` record, `@Validated` (`@NotBlank`, `@Min(1)`), `@DefaultValue("30m") Duration linkExpiry` |
| `ConfigApplication` | `@SpringBootApplication` + `@ConfigurationPropertiesScan` (finds the record) |
| `SignupService` | gets the whole record through its constructor |
| `ValueSignupService` | the same keys read one by one with `@Value("${...}")`, for comparison |
| `SettingsController` | `GET /settings` returns the bound values as JSON |
| `application-dev.yml` | profile `dev`: only the keys that differ (`welcome-message`, `daily-limit: 5`) |
| `application-prod.yml` | profile `prod`: `daily-limit: 1000` and the mail server, but no password |
| `MailProperties` | `@Profile("prod")` `@ConfigurationProperties("app.mail")` record; `@NotBlank password` comes from `APP_MAIL_PASSWORD`; `toString()` masks it |
| `WelcomeNotifier` | one interface, two implementations chosen by profile |
| `LogWelcomeNotifier` | `@Profile("!prod")`: everywhere except prod, only logs |
| `SmtpWelcomeNotifier` | `@Profile("prod")`: uses `MailProperties` (a stand-in: sending real mail is out of scope here) |
| `SignupLimits` | SpEL `@Value("#{${app.signup.daily-limit} * 8 / 10}")` (warn at 80%) and a placeholder default `${app.signup.support-email:support@example.com}` |
| `ProfileController` | `GET /profile` returns the active profiles, `dailyLimit`, `warnAt` and the notifier |
| `SignupPropertiesTest` (test) | kebab-case binding, the default value, the environment-variable name `APP_SIGNUP_DAILYLIMIT`, validation failures |
| `ApplicationYamlTest` (test) | the values from `application.yml`, injected; `@Value` reads the same keys |
| `PrecedenceTest` (test) | test properties and command-line arguments override `application.yml` |
| `SettingsControllerTest` (test) | the whole app on a random port |
| `DefaultProfileTest` (test) | no profile: the `default` profile, no prod-only beans, SpEL `warnAt` 80, the placeholder default |
| `ProfileFilesTest` (test) | `@ActiveProfiles("dev")`: the dev file overrides only its own keys, `warnAt` 4, the logging notifier |
| `TestProfileTest` (test) | `@ActiveProfiles("test")` with `src/test/resources/application-test.yml` |
| `ProdProfileTest` (test) | prod with the password from outside starts (limit 1000, SMTP notifier, masked `toString`), prod without it refuses to start, `prod,dev`: the last profile wins, the command line still beats profile files |
| `MultiDocumentTest` (test) | one YAML file split by `---`, the second document only for `prod \| staging` |

## Run
```bash
./mvnw -pl module-03-configuration -am -q package -DskipTests
J=module-03-configuration/target/module-03-configuration-0.1.0-SNAPSHOT.jar

java -jar $J                                             # dailyLimit 100 (application.yml)
APP_SIGNUP_DAILYLIMIT=50 java -jar $J                    # 50  (environment variable beats the file)
APP_SIGNUP_DAILYLIMIT=50 java -jar $J --app.signup.daily-limit=10   # 10 (command line beats both)
java -Dapp.signup.daily-limit=25 -jar $J                 # 25  (Java system property)

curl localhost:8080/settings
# {"welcomeMessage":"Welcome","dailyLimit":100,"linkExpiry":"PT30M"}
```

An `application.yml` next to the jar (or in `./config/`) overrides the one packaged inside it.

Try it: `java -jar $J --app.signup.daily-limit=0`. The app refuses to start:
```
Binding to target info.mnafshin.springuniverse.config.SignupProperties failed:

    Property: app.signup.dailyLimit
    Value: "0"
    Origin: "app.signup.daily-limit" from property source "commandLineArgs"
    Reason: must be greater than or equal to 1
```

## Profiles (T3.2)
```bash
java -jar $J                                            # No active profile set, falling back to 1 default profile: "default"
java -jar $J --spring.profiles.active=dev               # The following 1 profile is active: "dev"
SPRING_PROFILES_ACTIVE=prod APP_MAIL_PASSWORD=... java -jar $J

curl localhost:8080/profile
# default: {"activeProfiles":[],"dailyLimit":100,"warnAt":80,"notifier":"log"}
# dev:     {"activeProfiles":["dev"],"dailyLimit":5,"warnAt":4,"notifier":"log"}
# prod:    {"activeProfiles":["prod"],"dailyLimit":1000,"warnAt":800,"notifier":"smtp"}
```

`SPRING_PROFILES_ACTIVE=prod java -jar $J` without `APP_MAIL_PASSWORD` refuses to start:
```
Binding to target info.mnafshin.springuniverse.config.MailProperties failed:

    Property: app.mail.password
    Value: "null"
    Reason: must not be blank
```

With `--spring.profiles.active=prod,dev` both are active and `dev`, listed last, wins: `dailyLimit` is 5.

## Test
```bash
./mvnw -pl module-03-configuration verify
```

Docs: [Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html) · [Profile-specific files](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.files.profile-specific) · [@ActiveProfiles](https://docs.spring.io/spring-framework/reference/testing/annotations/integration-spring/annotation-activeprofiles.html) · [SpEL in bean definitions](https://docs.spring.io/spring-framework/reference/core/expressions/beandef.html) · [Externalized configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Type-safe configuration properties](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) · [Relaxed binding](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding)
