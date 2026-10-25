package info.mnafshin.springuniverse.container.lifecycle;

import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Called for every bean instance, before and after its init callbacks.
 * It only reports on PriceCache to keep the output short. It could also return a different object (a proxy).
 */
public class TracingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof PriceCache) {
            Events.log("2 postProcessBeforeInitialization(" + beanName + ")");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof PriceCache) {
            Events.log("6 postProcessAfterInitialization(" + beanName + ")");
        }
        return bean;
    }
}
