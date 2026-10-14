package info.mnafshin.springuniverse.foundations.di;

import java.util.Objects;

/**
 * Dependency injection in plain words: the service does not create its dependency,
 * it receives it through the constructor. Whoever builds the service decides which one.
 */
public class GreetingService {

    private final MessageSource messages;

    public GreetingService(MessageSource messages) { // constructor injection
        this.messages = Objects.requireNonNull(messages, "messages");
    }

    public String greet(String name) {
        return messages.greetingFor(name.strip());
    }
}
