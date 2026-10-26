package info.mnafshin.springuniverse.container.proxies.selfcall;

import org.springframework.stereotype.Service;

/** Calls save() through the injected OrderWriter, which is a proxy, so the transaction starts. */
@Service
public class FixedOrderService {

    private final OrderWriter writer;

    public FixedOrderService(OrderWriter writer) {
        this.writer = writer;
    }

    public void checkout(String order) {
        writer.save(order);
    }
}
