package io.squant.app;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/bets", "/add"})
    public String home() {
        return "index";
    }
}
