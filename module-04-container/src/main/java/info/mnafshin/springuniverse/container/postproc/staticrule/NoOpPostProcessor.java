package info.mnafshin.springuniverse.container.postproc.staticrule;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/** Does nothing. Only how it is declared matters in this example. */
public class NoOpPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
    }
}
