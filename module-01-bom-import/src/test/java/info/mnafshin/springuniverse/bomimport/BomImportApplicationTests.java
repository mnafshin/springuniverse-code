package info.mnafshin.springuniverse.bomimport;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BomImportApplicationTests {

    @Test
    void contextLoadsWithTheImportedBomVersion() {
        // spring-boot itself was resolved through the imported BOM (spring-boot.version in the pom)
        assertThat(SpringBootVersion.getVersion()).isEqualTo(System.getProperty("expected.spring-boot.version", "4.1.2"));
    }
}
