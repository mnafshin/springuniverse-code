package info.mnafshin.springuniverse.foundations.di;

/** A dependency, described by what it does, not how. */
public interface MessageSource {

    String greetingFor(String name);
}
