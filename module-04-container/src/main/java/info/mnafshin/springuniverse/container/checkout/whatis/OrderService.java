package info.mnafshin.springuniverse.container.checkout.whatis;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** One @Transactional method: Spring hands out a proxy that stands in front of this object. */
@Service
public class OrderService {

    @Transactional
    public void save(String order) {
    }
}
