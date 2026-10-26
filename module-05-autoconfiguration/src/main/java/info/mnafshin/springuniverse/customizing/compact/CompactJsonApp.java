package info.mnafshin.springuniverse.customizing.compact;

import info.mnafshin.springuniverse.customizing.Order;
import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/** Example 1, the problem: Boot's default {@link JsonMapper} writes everything on one line. */
@SpringBootApplication
public class CompactJsonApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(CompactJsonApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            System.out.println(context.getBean(JsonMapper.class).writeValueAsString(Order.SAMPLE));
        }
    }

}
