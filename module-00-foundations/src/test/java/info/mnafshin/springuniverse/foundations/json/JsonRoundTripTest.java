package info.mnafshin.springuniverse.foundations.json;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonRoundTripTest {

    @Test
    void objectsBecomeJsonAndBack() {
        User sam = new User("Sam", 36);
        String json = JsonRoundTrip.toJson(sam);
        assertThat(json).isEqualTo("{\"name\":\"Sam\",\"age\":36}");
        assertThat(JsonRoundTrip.fromJson(json)).isEqualTo(sam);
    }

    @Test
    void singleQuotesAreNotJson() {
        assertThatThrownBy(() -> JsonRoundTrip.fromJson("{'name':'Sam','age':36}"))
                .isInstanceOf(JacksonException.class);
    }
}
