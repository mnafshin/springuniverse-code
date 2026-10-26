package info.mnafshin.springuniverse.container.beanlife.whencreated;

import org.springframework.stereotype.Component;

/** A normal singleton: created while the container starts. */
@Component
public class Menu {

    public Menu() {
        System.out.println("Menu created");
    }
}
