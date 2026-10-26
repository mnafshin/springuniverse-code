package info.mnafshin.springuniverse.container.proxies.whatis;

import org.springframework.stereotype.Service;

/** No annotations on its methods: Spring hands out the object itself. */
@Service
public class MenuService {

    public String today() {
        return "soup";
    }
}
