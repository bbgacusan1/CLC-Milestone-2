package edu.gcu.cst339.lab2_chinook_api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Spring MVC controller for the application home page.
 */
@Controller 
public class HomeController {

    /**
     * Shows the home page.
     *
     * @return the home template
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
