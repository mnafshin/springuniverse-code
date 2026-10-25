package info.mnafshin.springuniverse.container.cyclefix;

import org.springframework.stereotype.Component;

/** The part both services needed from each other, moved into its own bean. Now the arrows point one way. */
@Component
public class CustomerDirectory {

    public String nameOf(String customerId) {
        return "Ada";
    }
}
