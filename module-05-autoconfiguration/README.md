# Module 5: How auto-configuration decides

A small signup app plus our own auto-configuration, so you can watch Spring Boot's decisions: which candidates exist, which conditions matched, and how one bean of your own makes a default back off.

| Video | What it shows |
|---|---|
| T5.1 How auto-configuration decides (tutorial) | package `decides`: where the candidates come from, the classpath (`@ConditionalOnClass`), `@ConditionalOnMissingBean` and its trap, `@ConditionalOnBooleanProperty`, reading the `--debug` report |
| T5.1 v1 (the earlier all-in-one version, kept as raw material): conditions, the conditions report, overriding defaults, modular auto-config in Boot 4 (tutorial) | the `AutoConfiguration.imports` candidate list, `@ConditionalOnClass` / `@ConditionalOnMissingBean` / `@ConditionalOnBooleanProperty`, the `--debug` conditions report, replacing a default `Notifier` and Boot's `JsonMapper`, switching auto-configuration off, Spring Boot 4's modules, testing with `ApplicationContextRunner` |
| Short 030 | See every auto-configuration decision: `--debug` |
| Short 031 | Overriding a Boot default with one bean |
| Short 032 | `@ConditionalOnMissingBean` in 30 seconds |
| Short 033 | Boot 4 split into modules. What that means for you |

Videos: https://www.youtube.com/@springuniverse

## Package `decides` (T5.1, one small app per example)
Each example is its own `@SpringBootApplication` in its own package, started without a web server, prints what it found and exits. All of them also get our `NotifierAutoConfiguration` from `AutoConfiguration.imports`.

| Example | Main class | What it shows |
|---|---|---|
| 1 | `decides.candidates.CandidatesApp` | a `Notifier` we never wrote: it comes from the candidate list (every `AutoConfiguration.imports` file on the classpath) |
| 2 | `decides.onclass.DatabaseApp` | the classpath decides: no JDBC module, no `DataSource`; add `spring-boot-starter-jdbc` (`-Pjdbc`) and Boot wants one but has no URL; add H2 (`-Pjdbc,h2`) and it starts an in-memory database |
| 3 | `decides.missingbean.NotifierApp` | the default `log` notifier; with `--spring.profiles.active=sms` our own bean exists and the default backs off |
| 4 | `decides.trap.TrapApp` → `decides.trapfix.TrapFixedApp` | `@ConditionalOnMissingBean` in our own `@Configuration` checks too early: two notifiers. The fallback belongs in an auto-configuration |
| 5 | `decides.property.PropertyApp` | `notifier.enable=false` (typo) changes nothing; `notifier.enabled=false` switches the auto-configuration off |
| 6 | any of them with `--debug` | the report's four sections: positive matches, negative matches, exclusions, unconditional classes |

```bash
R='./mvnw -q -pl module-05-autoconfiguration spring-boot:run -Dspring-boot.run.main-class=info.mnafshin.springuniverse.decides'
$R.candidates.CandidatesApp
# notifier bean: LogNotifier
# imports files on the classpath: 7
# candidates: 31 (Spring Boot: 30, ours: 1)

$R.onclass.DatabaseApp                   # DataSource beans: 0
$R.onclass.DatabaseApp -Pjdbc            # APPLICATION FAILED TO START
                                         # Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
$R.onclass.DatabaseApp -Pjdbc,h2         # DataSource beans: 1
                                         # database: jdbc:h2:mem:<random id>

$R.missingbean.NotifierApp               # notifier: log (LogNotifier)
$R.missingbean.NotifierApp -Dspring-boot.run.profiles=sms   # notifier: sms (SmsNotifier)

$R.trap.TrapApp                          # notifier beans: [fallbackNotifier, smsNotifier]
                                         # NoUniqueBeanDefinitionException: ... expected single matching bean but found 2
$R.trapfix.TrapFixedApp                  # notifier beans: [smsNotifier]

$R.property.PropertyApp                                              # notifier beans: 1
$R.property.PropertyApp -Dspring-boot.run.arguments=--notifier.enable=false    # notifier beans: 1 (typo)
$R.property.PropertyApp -Dspring-boot.run.arguments=--notifier.enabled=false   # notifier beans: 0

$R.missingbean.NotifierApp -Dspring-boot.run.arguments=--debug   # the CONDITIONS EVALUATION REPORT
```

Real report lines (Boot 4.1.2):
```
   DataSourceAutoConfiguration matched:                              (with -Pjdbc)
      - @ConditionalOnClass found required classes 'javax.sql.DataSource', 'org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType' (OnClassCondition)

   NotifierAutoConfiguration#notifier:                               (TrapApp)
      Did not match:
         - @ConditionalOnMissingBean (types: info.mnafshin.springuniverse.notifier.Notifier; SearchStrategy: all) found beans of type 'info.mnafshin.springuniverse.notifier.Notifier' fallbackNotifier, smsNotifier (OnBeanCondition)

   NotifierAutoConfiguration:                                        (--notifier.enabled=false)
      Did not match:
         - @ConditionalOnBooleanProperty (notifier.enabled=true) found different value in property 'notifier.enabled' (OnPropertyCondition)
```
With `-Pjdbc` the candidate list grows from 31 to 44 (`spring-boot-jdbc`, `spring-boot-transaction` and `spring-boot-persistence` bring their own imports files). `NotifierApp --debug` lists 26 positive and 37 negative matches, no exclusions and 6 unconditional classes; add `--spring.autoconfigure.exclude=info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration` and it appears under Exclusions.

The `jdbc` and `h2` profiles are only for running `DatabaseApp`; the tests run without them.

## Files
| File | Role |
|---|---|
| `notifier/Notifier` | the interface the app depends on |
| `notifier/LogNotifier` | the default implementation: just logs the message |
| `notifier/NotifierAutoConfiguration` | `@AutoConfiguration`, on unless `notifier.enabled=false` (`@ConditionalOnBooleanProperty(..., matchIfMissing = true)`); its `@Bean` is `@ConditionalOnMissingBean`, so any `Notifier` of yours wins |
| `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` | registers the class as an auto-configuration candidate (one class name per line) |
| `autoconfig/AutoConfigApplication` | `@SpringBootApplication`. Its package is a sibling of `notifier`, so the auto-configuration is never component-scanned (as the docs require) |
| `autoconfig/SignupController` | `GET /signup?name=` sends a welcome through whichever `Notifier` exists |
| `autoconfig/SmsNotifierConfig` | profile `sms`: our own `Notifier` bean, so the default backs off |
| `autoconfig/PrettyJsonConfig` | profile `pretty`: our own `JsonMapper`, so Boot's `jacksonJsonMapper` backs off (and `spring.jackson.*` no longer applies) |
| `NotifierAutoConfigurationTest` (test) | `ApplicationContextRunner`: the default, backing off for a user bean, the property switch |
| `SignupControllerTest`, `OverrideTest` (test) | the whole app with and without the `sms` and `pretty` profiles |
| `ConditionsReportTest` (test) | reads the same `ConditionEvaluationReport` that `--debug` prints |
| `DecidesExamplesTest` (test) | the six `decides` examples, started the way their `main` methods start them |
| `CandidatesTest` (test) | the candidate list: ours, Jackson and Tomcat are in it; nothing from JDBC, because that module is not on the classpath |

## Run
```bash
./mvnw -pl module-05-autoconfiguration -am -q package -DskipTests
J=module-05-autoconfiguration/target/module-05-autoconfiguration-0.1.0-SNAPSHOT.jar

java -jar $J --debug                                   # prints the CONDITIONS EVALUATION REPORT
curl 'localhost:8080/signup?name=Ada'
# {"name":"Ada","notifier":"log","result":"logged for Ada"}

java -jar $J --debug --spring.profiles.active=sms      # our Notifier wins
# {"name":"Ada","notifier":"sms","result":"texted Ada"}

java -jar $J --spring.profiles.active=sms,pretty       # our JsonMapper wins too: indented JSON
```

In the report, the default run shows:
```
   NotifierAutoConfiguration#notifier matched:
      - @ConditionalOnMissingBean (types: info.mnafshin.springuniverse.notifier.Notifier; SearchStrategy: all) did not find any beans (OnBeanCondition)
```
and with the `sms` profile, under negative matches:
```
   NotifierAutoConfiguration#notifier:
      Did not match:
         - @ConditionalOnMissingBean (types: info.mnafshin.springuniverse.notifier.Notifier; SearchStrategy: all) found beans of type 'info.mnafshin.springuniverse.notifier.Notifier' smsNotifier (OnBeanCondition)
```

Switching it off:
```bash
java -jar $J --spring.profiles.active=sms \
  --spring.autoconfigure.exclude=info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration
# the report lists it under "Exclusions"

java -jar $J --notifier.enabled=false
# the app refuses to start: SignupController needs a Notifier and nothing provides one
```

Spring Boot 4 is modular: this app (`spring-boot-starter-webmvc`) has 30 auto-configuration candidates from Boot, plus ours. The same app on `spring-boot-starter-classic` has 293 (measured with Boot 4.1.2).

## Test
```bash
./mvnw -pl module-05-autoconfiguration verify
```

Docs: [Auto-configuration](https://docs.spring.io/spring-boot/reference/using/auto-configuration.html) · [Creating your own auto-configuration](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html) · [Troubleshoot auto-configuration](https://docs.spring.io/spring-boot/how-to/application.html#howto.application.troubleshoot-auto-configuration) · [Spring Boot 4.0 migration guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
