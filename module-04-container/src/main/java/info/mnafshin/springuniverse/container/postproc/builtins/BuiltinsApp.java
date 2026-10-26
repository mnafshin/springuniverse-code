package info.mnafshin.springuniverse.container.postproc.builtins;

import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.GenericApplicationContext;

/** @Autowired and @PostConstruct are not magic: two BeanPostProcessors that ship with Spring handle them. */
public class BuiltinsApp {

    public static void main(String[] args) {
        System.out.println("-- container without Spring's annotation processors");
        try (var context = new GenericApplicationContext()) {
            context.registerBean(PriceSource.class);
            context.registerBean(PriceCache.class);
            context.refresh();
            System.out.println("source = " + context.getBean(PriceCache.class).source());
        }
        System.out.println("-- with them");
        try (var context = new GenericApplicationContext()) {
            AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
            context.registerBean(PriceSource.class);
            context.registerBean(PriceCache.class);
            for (String name : context.getBeanDefinitionNames()) {
                if (name.endsWith("AutowiredAnnotationProcessor") || name.endsWith("CommonAnnotationProcessor")) {
                    System.out.println("post-processor: " + name.substring(name.lastIndexOf('.') + 1));
                }
            }
            context.refresh();
            System.out.println("source = " + context.getBean(PriceCache.class).source());
        }
    }
}
