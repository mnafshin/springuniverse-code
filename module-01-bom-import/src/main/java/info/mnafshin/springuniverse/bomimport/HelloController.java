package info.mnafshin.springuniverse.bomimport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    public record Greeting(String message) {
    }

    // No explicit name in @RequestParam: works only because the compiler keeps parameter names (-parameters)
    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "World") String name) {
        return new Greeting("Hello, " + name + "!");
    }
}
