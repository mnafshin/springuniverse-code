package info.mnafshin.springuniverse.customizing.pretty;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Example 1, the fix: one bean of our own. Boot's {@code jacksonJsonMapper} is
 * {@code @ConditionalOnMissingBean}, so it backs off. Example 2 shows the catch: Boot's
 * {@code spring.jackson.*} properties are applied to Boot's builder, which this mapper never uses.
 */
@Configuration(proxyBeanMethods = false)
class PrettyJsonConfig {

    @Bean
    JsonMapper jsonMapper() {
        return JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();
    }

}
