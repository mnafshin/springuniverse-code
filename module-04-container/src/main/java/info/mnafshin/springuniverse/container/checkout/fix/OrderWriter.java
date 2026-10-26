package info.mnafshin.springuniverse.container.checkout.fix;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The fix: the @Transactional save() lives in its own bean, so callers reach it through the proxy. */
@Service
public class OrderWriter {

    private final OrderTables tables;
    private final PriceTable prices;

    public OrderWriter(OrderTables tables, PriceTable prices) {
        this.tables = tables;
        this.prices = prices;
    }

    @Transactional
    public void save(String id, String... items) {
        System.out.println("save(" + id + "): transaction active = " + Tx.active());
        tables.addOrder(id);
        for (String item : items) {
            tables.addLine(id, item, prices.price(item));
        }
    }
}
