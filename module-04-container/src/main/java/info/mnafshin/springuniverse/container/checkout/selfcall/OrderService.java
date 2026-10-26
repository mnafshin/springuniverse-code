package info.mnafshin.springuniverse.container.checkout.selfcall;

import info.mnafshin.springuniverse.container.checkout.OrderTables;
import info.mnafshin.springuniverse.container.checkout.Tx;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** The self-call trap: checkout() calls save() on "this", not on the proxy, so a failure leaves a half-saved order. */
@Service
public class OrderService {

    private final OrderTables tables;
    private final PriceTable prices;

    public OrderService(OrderTables tables, PriceTable prices) {
        this.tables = tables;
        this.prices = prices;
    }

    public void checkout(String id, String... items) {
        save(id, items); // this.save(...): skips the proxy, so no transaction
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
