package info.mnafshin.springuniverse.container.postproc.lazyplan;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/** Runs after all bean definitions are loaded, before any regular bean exists. Changes the plan, not the class. */
public class MakeReportJobLazy implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) {
        BeanDefinition definition = factory.getBeanDefinition("reportJob");
        definition.setLazyInit(true);
        System.out.println("MakeReportJobLazy: reportJob lazy=" + definition.isLazyInit());
    }
}
