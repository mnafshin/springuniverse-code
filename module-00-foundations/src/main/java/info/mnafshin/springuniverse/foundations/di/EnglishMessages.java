package info.mnafshin.springuniverse.foundations.di;

public class EnglishMessages implements MessageSource {

    @Override
    public String greetingFor(String name) {
        return "Hello, " + name + "!";
    }
}
