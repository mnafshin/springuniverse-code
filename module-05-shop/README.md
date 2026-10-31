# Module 5: the shop app (T5.1 How auto-configuration decides)

One small shop app, one topic: the `DataSource`. Every run prints two things: how many auto-configuration candidates
Spring Boot read (from every `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` on the
classpath), and how many `DataSource` beans exist, with each one's URL. Maven profiles change the classpath; command-line
properties and Spring profiles change the decisions. Every output line below is real (Spring Boot 4.1.2, Java 25.0.4.1,
captured 2026-10-31).

| Run | What decides | Output |
|---|---|---|
| plain | nothing to decide about a database | `candidates: 12`, `data sources: 0` |
| `-Pjdbc` (the JDBC starter) | `DataSourceAutoConfiguration`: `@ConditionalOnClass` found `javax.sql.DataSource` | APPLICATION FAILED TO START: `Failed to configure a DataSource` |
| `-Pjdbc,h2` | the same, and H2 is an embedded database | `candidates: 25`, `data sources: 1`, `jdbc:h2:mem:<random>` |
| `-Pmydb` | our own `MyDataSourceAutoConfiguration` (module-05-shop-datasource): both conditions match | `candidates: 13`, `data sources: 1`, `jdbc:h2:mem:ours` |
| `-Pmydb`, `--spring.profiles.active=owndb` | `@ConditionalOnMissingBean` finds the shop's own `shopDataSource`, ours backs off | `data sources: 1`, `jdbc:h2:mem:shop` |
| `-Pmydb`, `--shop.datasource.enable=false` (typo) | nobody set `shop.datasource.enabled`, `matchIfMissing = true` wins | `data sources: 1`, `jdbc:h2:mem:ours` |
| `-Pmydb`, `--shop.datasource.enabled=false` | `@ConditionalOnBooleanProperty` found a different value: the whole class is skipped | `data sources: 0` |

## Run
`spring-boot:run` does not exit for this app in our setup, so build a classpath once and use `java`:

```bash
./mvnw -q -pl module-05-shop-datasource install            # only for -Pmydb
./mvnw -q -Pmydb -pl module-05-shop clean compile dependency:build-classpath -Dmdep.outputFile=target/cp.txt
cd module-05-shop
CP=target/classes:$(cat target/cp.txt)

java -cp "$CP" info.mnafshin.springuniverse.shop.ShopApp --debug
#    MyDataSourceAutoConfiguration matched:
#       - @ConditionalOnClass found required class 'org.h2.jdbcx.JdbcDataSource' (OnClassCondition)
#       - @ConditionalOnBooleanProperty (shop.datasource.enabled=true) matched (OnPropertyCondition)
#    MyDataSourceAutoConfiguration#dataSource matched:
#       - @ConditionalOnMissingBean (types: javax.sql.DataSource; SearchStrategy: all) did not find any beans (OnBeanCondition)
# candidates: 13
# data sources: 1
#   jdbc:h2:mem:ours

java -cp "$CP" info.mnafshin.springuniverse.shop.ShopApp --debug --spring.profiles.active=owndb
#    MyDataSourceAutoConfiguration#dataSource:
#       Did not match:
#          - @ConditionalOnMissingBean (types: javax.sql.DataSource; SearchStrategy: all) found beans of type 'javax.sql.DataSource' shopDataSource (OnBeanCondition)
# data sources: 1
#   jdbc:h2:mem:shop

java -cp "$CP" info.mnafshin.springuniverse.shop.ShopApp --debug --shop.datasource.enabled=false
#    MyDataSourceAutoConfiguration:
#       Did not match:
#          - @ConditionalOnBooleanProperty (shop.datasource.enabled=true) found different value in property 'shop.datasource.enabled' (OnPropertyCondition)
#       Matched:
#          - @ConditionalOnClass found required class 'org.h2.jdbcx.JdbcDataSource' (OnClassCondition)
# data sources: 0
```

For the plain, `-Pjdbc` and `-Pjdbc,h2` runs, build the classpath with that profile instead of `-Pmydb` (use `clean`, so
`src/mydb/java` is not left in `target/classes`).

In the video, the chapter "Inside one auto-configuration" shows `MyDataSourceAutoConfiguration` before the property line was
added (commit `ce26f9d`, runs captured then); the chapter "A property decides" adds that one line (the next commit) and runs again.

## Test
```bash
./mvnw -pl module-05-shop verify                                        # ShopAppTest (plain: no data source, every candidate is Boot's)
./mvnw -pl module-05-shop-datasource install && ./mvnw -Pmydb -pl module-05-shop verify   # ShopMyDataSourceTest (both runs, the property)
```
`module-05-shop-datasource` has its own `MyDataSourceAutoConfigurationTest` (5 tests, `ApplicationContextRunner`).

Docs: [Auto-configuration](https://docs.spring.io/spring-boot/reference/using/auto-configuration.html) · [Condition annotations](https://docs.spring.io/spring-boot/reference/features/developing-auto-configuration.html#features.developing-auto-configuration.condition-annotations) · [Troubleshoot auto-configuration](https://docs.spring.io/spring-boot/how-to/application.html#howto.application.troubleshoot-auto-configuration)
