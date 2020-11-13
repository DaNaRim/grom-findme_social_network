package com.findme.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String home() {
        return "index";
    }

    @GetMapping(path = "/test-ajax")
    public ResponseEntity<String> testAjax() {

        return new ResponseEntity<>("trouble", HttpStatus.NOT_FOUND);
    }

    @GetMapping(path = "/registration")
    public String registration() {
        return "registration";
    }
}
