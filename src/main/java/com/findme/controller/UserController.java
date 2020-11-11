package com.findme.controller;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/user/{userId}")
    public String profile(Model model, @PathVariable long userId) {
        try {
            User user = userService.findById(userId);

            model.addAttribute("user", user);
            return "profile";
        } catch (ObjectNotFoundException e) {

            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (InternalServerException e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "500";
        }
    }
}
