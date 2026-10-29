package info.mnafshin.springuniverse.tuning.customize;

import info.mnafshin.springuniverse.customizing.Order;
import info.mnafshin.springuniverse.notifier.Notifier;

import tools.jackson.databind.json.JsonMapper;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/** T5.2 shop app, with a JsonMapperBuilderCustomizer (JsonConfig): Boot still builds the mapper and applies spring.jackson properties. It prints the order as JSON and the notifier it got. */
@SpringBootApplication
public class ShopApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(ShopApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            JsonMapper mapper = context.getBean(JsonMapper.class);
            System.out.println("order: " + mapper.writeValueAsString(Order.SAMPLE));
            Notifier notifier = context.getBeanProvider(Notifier.class).getIfAvailable();
            System.out.println("notifier: " + (notifier == null ? "none" : notifier.name()));
        }
    }

}
