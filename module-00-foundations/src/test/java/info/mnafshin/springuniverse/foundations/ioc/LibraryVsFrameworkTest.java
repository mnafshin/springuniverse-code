package info.mnafshin.springuniverse.foundations.ioc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class LibraryVsFrameworkTest {

    @Test
    void youCallALibrary() {
        assertThat(LibraryExample.toCsv(List.of("Ada", "Linus"))).isEqualTo("Ada,Linus");
    }

    @Test
    void aFrameworkCallsYou() {
        List<String> calledWith = new ArrayList<>();
        MiniFramework app = new MiniFramework().route("/hello", path -> {
            calledWith.add(path);
            return "Hello!";
        });

        // We never call our handler directly; the framework does.
        List<String> responses = app.run(List.of("/hello", "/missing", "/hello"));

        assertThat(calledWith).containsExactly("/hello", "/hello");
        assertThat(responses).containsExactly("Hello!", "404 Not Found", "Hello!");
    }
}
