package info.mnafshin.springuniverse.container.postproc.wrapper;

import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Proxy;

/** Hands back a wrapper (a JDK proxy) instead of the bean: every call is logged, then passed on. */
public class LoggingPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (!(bean instanceof PriceService target)) {
            return bean;
        }
        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] {PriceService.class},
                (proxy, method, args) -> {
                    System.out.println("log: " + method.getName() + "(" + args[0] + ")");
                    return method.invoke(target, args);
                });
    }
}
