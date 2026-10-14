package info.mnafshin.springuniverse.foundations.ioc;

/** The "slot" a framework user fills in. The framework decides when it is called. */
@FunctionalInterface
public interface RequestHandler {

    String handle(String path);
}
