package info.mnafshin.springuniverse.shop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

    @GetMapping("/")
    String welcome() {
        return "Welcome to the shop";
    }

}
