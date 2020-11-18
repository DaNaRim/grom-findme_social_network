package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    @PostMapping(path = "/userRegistration")
    public @ResponseBody
    ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            userService.registerUser(user);

            return new ResponseEntity<>("Registration success", HttpStatus.OK);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
