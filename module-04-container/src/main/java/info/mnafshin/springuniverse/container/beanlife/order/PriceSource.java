package info.mnafshin.springuniverse.container.beanlife.order;

import jakarta.annotation.PreDestroy;

/** Reads the prices table, so it needs Migrations to have run, but it never calls Migrations. */
public class PriceSource {

    public PriceSource() {
        if (Database.hasTable("prices")) {
            System.out.println("PriceSource: table prices found");
        } else {
            System.out.println("PriceSource: table prices is missing!");
        }
    }

    @PreDestroy
    public void close() {
        System.out.println("PriceSource closed");
    }
}
