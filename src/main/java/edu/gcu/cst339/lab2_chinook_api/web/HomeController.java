package edu.gcu.cst339.lab2_chinook_api.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
