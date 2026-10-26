package info.mnafshin.springuniverse.greeting;

/** The starter's default {@link Greeter}. */
public class DefaultGreeter implements Greeter {

    @Override
    public String greet(String name) {
        return "Hello, " + name + "!";
    }

}
