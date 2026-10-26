package info.mnafshin.springuniverse.container.proxies.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class AsyncApp {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(AsyncApp.class, args)) {
            System.out.println("main runs on: " + Thread.currentThread().getName());
            System.out.println("-- finish() calls this.send()");
            System.out.println("send() ran on: " + context.getBean(ReportService.class).finish().join());
            System.out.println("-- fix: send() in another bean");
            System.out.println("send() ran on: " + context.getBean(FixedReportService.class).finish().join());
        }
    }
}
