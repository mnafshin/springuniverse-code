package info.mnafshin.springuniverse.container.shop.order;

import jakarta.annotation.PreDestroy;

/** Creates the prices table. Nothing injects this bean. */
public class Migrations {

    public Migrations() {
        Database.createTable("prices");
        System.out.println("Migrations: table prices created");
    }

    @PreDestroy
    public void close() {
        System.out.println("Migrations closed");
    }
}
