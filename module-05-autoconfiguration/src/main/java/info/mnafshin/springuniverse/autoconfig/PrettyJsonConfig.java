package info.mnafshin.springuniverse.autoconfig;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 * Replacing one of Spring Boot's own defaults with a single bean. Boot auto-configures a
 * {@link JsonMapper} only if there is none yet, so this one wins and every JSON response is
 * pretty-printed. Note: replacing the mapper also switches off Boot's {@code spring.jackson.*}
 * settings for it. To only tweak it, a property such as
 * {@code spring.jackson.serialization.indent-output=true} or a {@code JsonMapperBuilderCustomizer}
 * is usually the better choice.
 */
@Configuration(proxyBeanMethods = false)
@Profile("pretty")
class PrettyJsonConfig {

    @Bean
    @Primary
    JsonMapper jsonMapper() {
        return JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();
    }

}
