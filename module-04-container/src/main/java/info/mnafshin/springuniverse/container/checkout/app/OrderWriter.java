package info.mnafshin.springuniverse.container.checkout.app;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Saves the order and its lines in one transaction; returns the total. Public and not final. */
@Service
public class OrderWriter {

    private final OrderTables tables;
    private final PriceTable prices;

    public OrderWriter(OrderTables tables, PriceTable prices) {
        this.tables = tables;
        this.prices = prices;
    }

    @Transactional
    public int save(String id, String... items) {
        System.out.println("save(" + id + "): transaction active = " + Tx.active());
        tables.addOrder(id);
        int total = 0;
        for (String item : items) {
            int price = prices.price(item);
            tables.addLine(id, item, price);
            total += price;
        }
        return total;
    }
}
