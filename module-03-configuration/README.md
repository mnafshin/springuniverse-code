# Module 3: Configuration and properties

One small signup app whose settings live outside the code: `application.yml`, environment variables and command-line arguments, bound into a type-safe, validated record.

| Video | What it shows |
|---|---|
| T3.1 External configuration: `application.yml`, precedence and `@ConfigurationProperties` (tutorial) | YAML vs properties, `@Value` vs a `@ConfigurationProperties` record, defaults and validation at startup, property sources and which one wins |
| Short 016 | Stop using `@Value` everywhere: `@ConfigurationProperties` with a record |
| Short 017 | Which property wins? Precedence in 30 seconds |
| Short 018 | Any Spring property as an environment variable |

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
| `SignupPropertiesTest` (test) | kebab-case binding, the default value, the environment-variable name `APP_SIGNUP_DAILYLIMIT`, validation failures |
| `ApplicationYamlTest` (test) | the values from `application.yml`, injected; `@Value` reads the same keys |
| `PrecedenceTest` (test) | test properties and command-line arguments override `application.yml` |
| `SettingsControllerTest` (test) | the whole app on a random port |

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

## Test
```bash
./mvnw -pl module-03-configuration verify
```

Docs: [Externalized configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html) · [Type-safe configuration properties](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties) · [Relaxed binding](https://docs.spring.io/spring-boot/reference/features/external-config.html#features.external-config.typesafe-configuration-properties.relaxed-binding)
