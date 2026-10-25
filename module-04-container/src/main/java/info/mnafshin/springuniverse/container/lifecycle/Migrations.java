package info.mnafshin.springuniverse.container.lifecycle;

/** Nothing injects this bean, but PriceSource needs it to have run: @DependsOn makes that explicit. */
public class Migrations {

    public Migrations() {
        Events.log("Migrations ran");
    }
}
