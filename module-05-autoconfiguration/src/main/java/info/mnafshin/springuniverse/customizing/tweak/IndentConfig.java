package info.mnafshin.springuniverse.customizing.tweak;

import tools.jackson.databind.SerializationFeature;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Example 2, the fix: don't replace Boot's mapper, customize the builder Boot uses. Boot still
 * creates the mapper and still applies {@code spring.jackson.*}; this adds one more change.
 */
@Configuration(proxyBeanMethods = false)
class IndentConfig {

    @Bean
    JsonMapperBuilderCustomizer indentOutput() {
        return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
    }

}
