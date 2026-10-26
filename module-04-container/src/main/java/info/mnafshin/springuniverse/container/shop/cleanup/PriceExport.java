package info.mnafshin.springuniverse.container.shop.cleanup;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Writes the nightly price export into a real temp folder at startup and deletes the folder in @PreDestroy. */
@Component
public class PriceExport {

    private Path folder;

    @PostConstruct
    void create() throws IOException {
        folder = Files.createTempDirectory("price-export-");
        Files.writeString(folder.resolve("prices.csv"), "coffee,3\ntea,2\n");
        System.out.println("PriceExport: export folder created");
    }

    @PreDestroy
    void delete() throws IOException {
        FileSystemUtils.deleteRecursively(folder);
        System.out.println("PriceExport: export folder deleted");
    }

    public Path folder() {
        return folder;
    }
}
