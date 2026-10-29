package info.mnafshin.springuniverse.container.beanlife.prototype;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/** A prototype bean: a new object for every getBean(). Spring never calls its @PreDestroy. */
@Component
@Scope("prototype")
public class ReportConnection implements AutoCloseable {

    private static int opened = 0;
    private final int id = ++opened;

    public ReportConnection() {
        System.out.println("connection " + id + " opened");
    }

    @PreDestroy
    @Override
    public void close() {
        System.out.println("connection " + id + " closed");
    }
}
