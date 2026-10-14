package info.mnafshin.springuniverse.foundations.maven;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MavenNoteTest {

    @Test
    void thisModuleIsBuiltByMaven() throws Exception {
        assertThat(MavenNote.whatMavenDoes()).contains("Maven");
        Path pom = Path.of("pom.xml");
        assertThat(Files.exists(pom)).isTrue();
        assertThat(Files.readString(pom)).contains("<artifactId>module-00-foundations</artifactId>");
    }
}
