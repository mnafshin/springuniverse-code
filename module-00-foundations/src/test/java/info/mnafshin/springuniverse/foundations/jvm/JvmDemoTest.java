package info.mnafshin.springuniverse.foundations.jvm;

import java.io.InputStream;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JvmDemoTest {

    @Test
    void bytecodeExistsAndRunsOnTheJvm() throws Exception {
        assertThat(JvmDemo.answer()).isEqualTo(42);
        assertThat(JvmDemo.runtimeName()).isNotBlank();
        try (InputStream in = JvmDemo.class.getResourceAsStream("JvmDemo.class")) {
            assertThat(in).isNotNull();
            byte[] magic = in.readNBytes(4);
            // CA FE BA BE — Java class file magic
            assertThat(magic).containsExactly((byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE);
        }
    }
}
