package info.mnafshin.springuniverse.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * T1.2: shows which kind of thread handled the request.
 * With {@code spring.threads.virtual.enabled=true} (Java 21+), Tomcat runs each request on a virtual thread.
 */
@RestController
public class ThreadController {

    @GetMapping("/thread")
    public ThreadInfo thread() {
        Thread current = Thread.currentThread();
        return new ThreadInfo(current.toString(), current.isVirtual());
    }

    public record ThreadInfo(String thread, boolean virtual) {
    }
}
