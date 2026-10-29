package info.mnafshin.springuniverse.tuning.replace;

import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/** Replace: our own mapper. Boot's jacksonJsonMapper is @ConditionalOnMissingBean, so it backs off. */
@Configuration(proxyBeanMethods = false)
class MapperConfig {

    @Bean
    @Primary
    JsonMapper jsonMapper() {
        return JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();
    }

}
