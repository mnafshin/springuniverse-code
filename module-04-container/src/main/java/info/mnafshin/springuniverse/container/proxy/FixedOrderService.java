package info.mnafshin.springuniverse.container.proxy;

import org.springframework.stereotype.Service;

/** Calls save() through the injected OrderWriter, which is a proxy, so the transaction starts. */
@Service
public class FixedOrderService {

    private final OrderWriter writer;

    public FixedOrderService(OrderWriter writer) {
        this.writer = writer;
    }

    public boolean checkout(String order) {
        return writer.save(order);
    }
}
