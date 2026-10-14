package info.mnafshin.springuniverse.foundations.json;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * F05 companion: text in, objects out — Jackson maps {@link User} ↔ JSON.
 * Single quotes are invalid JSON; this demo always uses double quotes.
 */
public final class JsonRoundTrip {

    private static final ObjectMapper JSON = JsonMapper.builder().build();

    public static String toJson(User user) {
        return JSON.writeValueAsString(user);
    }

    public static User fromJson(String json) {
        return JSON.readValue(json, User.class);
    }

    public static void main(String[] args) {
        User sam = new User("Sam", 36);
        String json = toJson(sam);
        System.out.println(json);
        System.out.println(fromJson(json));
    }
}
