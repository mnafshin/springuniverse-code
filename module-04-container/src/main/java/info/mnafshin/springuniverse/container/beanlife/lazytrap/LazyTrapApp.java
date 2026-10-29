package info.mnafshin.springuniverse.container.beanlife.lazytrap;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/** @Lazy on a class is ignored when an eager bean needs it. @Lazy on the injection point keeps it lazy. */
public class LazyTrapApp {

    public static void main(String[] args) {
        System.out.println("-- @Lazy on the class only");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(SalesReport.class, AdminPage.class)) {
            System.out.println("container ready");
        }

        System.out.println("-- @Lazy on the injection point too");
        System.out.println("starting the container");
        try (var context = new AnnotationConfigApplicationContext(SalesReport.class, LazyAdminPage.class)) {
            System.out.println("container ready");
            System.out.println("opening the admin page");
            context.getBean(LazyAdminPage.class).open();
        }
    }
}
