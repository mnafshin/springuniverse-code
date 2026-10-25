package info.mnafshin.springuniverse.container.lifecycle;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * Runs after all bean definitions are loaded and before any regular bean exists.
 * It reads the blueprints, and changes one of them.
 */
public class MakeReportJobLazy implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
        for (String name : new String[] {"priceCache", "priceSource", "migrations", "reportJob"}) {
            BeanDefinition definition = factory.getBeanDefinition(name);
            Events.log("definition: " + name + " scope=" + scope(definition) + " lazy=" + definition.isLazyInit());
        }
        Events.log("instances so far: " + Events.all().stream().filter(e -> !e.startsWith("definition")).count());
        factory.getBeanDefinition("reportJob").setLazyInit(true);
        Events.log("reportJob is now lazy");
    }

    private static String scope(BeanDefinition definition) {
        return definition.getScope().isEmpty() ? "singleton" : definition.getScope();
    }
}
