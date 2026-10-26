package info.mnafshin.springuniverse.container.checkout.app;

public interface Payments {

    boolean charge(String order, int amount);
}
