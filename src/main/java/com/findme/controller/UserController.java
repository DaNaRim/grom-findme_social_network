package com.findme.controller;

import com.findme.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    @GetMapping(path = "/user/{userId}")
    public String profile(Model model, @PathVariable String userId) {
        //TODO controller-service-dao


        User user = new User();
        user.setFirstName("Andrey");
        user.setCity("TestCity");

        model.addAttribute("user", user);
        return "profile";
    }
}
