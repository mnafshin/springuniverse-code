package info.mnafshin.springuniverse.container.postproc.tracing;

import org.springframework.beans.factory.config.BeanPostProcessor;

/** Called for every bean instance, once before and once after its init callbacks. */
public class TracingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("before init: " + beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("after init:  " + beanName);
        return bean;
    }
}
