package com.findme.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {

    @GetMapping(path = "/")
    public String home() {
        return "index";
    }
}
