package info.mnafshin.springuniverse.container.postproc.plan;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** Phase 1 makes a plan (bean definitions), phase 2 creates the beans. */
public class PlanApp {

    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext();
        context.register(Menu.class, ReportJob.class);
        System.out.println("-- after register()");
        for (String name : context.getBeanDefinitionNames()) {
            if (name.startsWith("org.springframework")) {
                continue; // Spring's own helpers, see the builtins example
            }
            System.out.println(describe(name, context.getBeanDefinition(name)));
        }
        System.out.println("-- refresh()");
        context.refresh();
        System.out.println("container ready");
        context.close();
    }

    static String describe(String name, BeanDefinition definition) {
        String className = definition.getBeanClassName();
        String scope = definition.getScope() == null || definition.getScope().isEmpty() ? "singleton" : definition.getScope();
        return "definition " + name + ": class=" + className.substring(className.lastIndexOf('.') + 1)
                + " scope=" + scope + " lazy=" + definition.isLazyInit();
    }
}
