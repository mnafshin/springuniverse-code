package info.mnafshin.springuniverse.tuning.customize;

import tools.jackson.databind.SerializationFeature;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Customize: change Boot's builder instead of replacing the mapper. */
@Configuration(proxyBeanMethods = false)
class JsonConfig {

    @Bean
    JsonMapperBuilderCustomizer indentOutput() {
        return builder -> builder.enable(SerializationFeature.INDENT_OUTPUT);
    }

}
