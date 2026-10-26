package info.mnafshin.springuniverse.decides.candidates;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import info.mnafshin.springuniverse.notifier.Notifier;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Example 1: a {@link Notifier} bean exists although this app never defines one. Where does it come
 * from? From the candidate list: every {@code AutoConfiguration.imports} file on the classpath.
 */
@SpringBootApplication
public class CandidatesApp {

    static final String IMPORTS = "META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports";

    public static void main(String[] args) throws IOException {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(CandidatesApp.class)
            .web(WebApplicationType.NONE)
            .run(args)) {
            Notifier notifier = context.getBean(Notifier.class);
            System.out.println("notifier bean: " + notifier.getClass().getSimpleName());

            ClassLoader loader = CandidatesApp.class.getClassLoader();
            List<URL> files = Collections.list(loader.getResources(IMPORTS));
            System.out.println("imports files on the classpath: " + files.size());
            for (URL file : files) {
                System.out.println("  " + where(file));
            }
            List<String> candidates = ImportCandidates.load(AutoConfiguration.class, loader).getCandidates();
            long ours = candidates.stream().filter(name -> name.startsWith("info.mnafshin.")).count();
            System.out.println("candidates: " + candidates.size() + " (Spring Boot: " + (candidates.size() - ours)
                    + ", ours: " + ours + ")");
            candidates.stream().filter(name -> name.startsWith("info.mnafshin.")).forEach(name -> System.out.println("  " + name));
        }
    }

    /** The jar (or classes folder) that holds an imports file. */
    static String where(URL file) {
        String s = file.toString();
        int jar = s.indexOf(".jar!");
        if (jar >= 0) {
            return s.substring(s.lastIndexOf('/', jar) + 1, jar + 4);
        }
        return s.contains("/target/classes/") ? "module-05-autoconfiguration (target/classes)" : s;
    }

}
