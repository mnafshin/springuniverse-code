package info.mnafshin.springuniverse.container.checkout.fix;

import org.springframework.stereotype.Service;

/** checkout() calls save() on the injected OrderWriter, which is a proxy, so the transaction starts. */
@Service
public class OrderService {

    private final OrderWriter writer;

    public OrderService(OrderWriter writer) {
        this.writer = writer;
    }

    public void checkout(String id, String... items) {
        writer.save(id, items);
    }
}
