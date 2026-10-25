package info.mnafshin.springuniverse.autoconfig;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.annotation.ImportCandidates;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Candidates come from every {@code AutoConfiguration.imports} file on the classpath. With
 * Spring Boot 4's modules, a technology you did not add brings no candidates at all.
 */
class CandidatesTest {

    private final List<String> candidates = ImportCandidates
        .load(AutoConfiguration.class, getClass().getClassLoader())
        .getCandidates();

    @Test
    void ourOwnAutoConfigurationIsACandidate() {
        assertThat(candidates).contains("info.mnafshin.springuniverse.notifier.NotifierAutoConfiguration");
    }

    @Test
    void jacksonAndTomcatComeFromTheirOwnModules() {
        assertThat(candidates)
            .contains("org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration")
            .anyMatch(name -> name.startsWith("org.springframework.boot.tomcat."));
    }

    @Test
    void noJdbcModuleMeansNoDataSourceCandidate() {
        assertThat(candidates).noneMatch(name -> name.startsWith("org.springframework.boot.jdbc."));
    }

}
