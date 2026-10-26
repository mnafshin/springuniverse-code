package info.mnafshin.springuniverse.customizing.pretty;

import info.mnafshin.springuniverse.customizing.Order;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 1 (fix) and Example 2 (problem): our own {@link JsonMapper} replaces Boot's. Run it
 * with {@code --spring.jackson.property-naming-strategy=SNAKE_CASE} and nothing changes.
 */
@SpringBootApplication
public class PrettyJsonApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(PrettyJsonApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println(context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE));
        }
    }

}
