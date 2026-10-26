package info.mnafshin.springuniverse.customizing.tweak;

import info.mnafshin.springuniverse.customizing.Order;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 2, the fix: Boot's own {@link JsonMapper}, customized. Run it with
 * {@code --spring.jackson.property-naming-strategy=SNAKE_CASE}: indented and snake_case.
 */
@SpringBootApplication
public class TweakJsonApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(TweakJsonApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println(context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE));
        }
    }

}
