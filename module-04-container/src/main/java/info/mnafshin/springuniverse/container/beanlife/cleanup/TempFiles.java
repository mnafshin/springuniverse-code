package info.mnafshin.springuniverse.container.beanlife.cleanup;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Creates a real temp folder at startup and deletes it in @PreDestroy. */
@Component
public class TempFiles {

    private Path folder;

    @PostConstruct
    void create() throws IOException {
        folder = Files.createTempDirectory("prices-");
        System.out.println("TempFiles: temp folder created");
    }

    @PreDestroy
    void delete() throws IOException {
        FileSystemUtils.deleteRecursively(folder);
        System.out.println("TempFiles: temp folder deleted");
    }

    public Path folder() {
        return folder;
    }
}
