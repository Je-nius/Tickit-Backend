package jenius.tickitapi.tickit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TickitController {

    @GetMapping("/")
    public String connectTest() {
        return "Hi. We are Tickit";
    }

}
