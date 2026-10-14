package info.mnafshin.springuniverse.foundations.di;

/**
 * The "container" is just this main method: it creates objects and wires them together.
 * Spring's IoC container does the same job for you, at scale.
 */
public final class ManualWiring {

    private ManualWiring() {
    }

    public static void main(String[] args) {
        MessageSource messages = new EnglishMessages();
        GreetingService service = new GreetingService(messages); // inject
        System.out.println(service.greet("Afshin"));
    }
}
