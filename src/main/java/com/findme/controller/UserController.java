package com.findme.controller;

import com.findme.exception.*;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;
    private final RelationshipService relationshipService;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @GetMapping(path = "/{userId}")
    public String profile(@PathVariable String userId, Model model, HttpSession session) {
        try {
            long id;
            try {
                id = Long.parseLong(userId);

            } catch (ArithmeticException e) {
                throw new BadRequestException("Id`s filed incorrect");
            }

            User user = userService.findById(id);

            if (session.getAttribute("userId") != null) {
                RelationshipStatus relationshipStatus = relationshipService.getRelationShipStatus(
                        (long) session.getAttribute("userId"),
                        Long.parseLong(userId));

                user.setRelationshipStatus(relationshipStatus);
            } else {
                user.setRelationshipStatus(RelationshipStatus.NEVER_FRIENDS);
            }

            model.addAttribute("user", user);
            return "profile";
        } catch (Exception e) {
            return errorHandlingWithModel(e, model);
        }
    }

    @GetMapping(path = "/registration")
    public String registrationForm() {
        return "registration";
    }

    @PostMapping(path = "/userRegistration")
    public @ResponseBody
    ResponseEntity<String> registerUser(@ModelAttribute User user) {
        try {
            userService.registerUser(user);

            return new ResponseEntity<>("Registration success", HttpStatus.CREATED);
        } catch (Exception e) {
            return errorHandlingWithResponseEntity(e);
        }
    }

    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping(path = "/login")
    public @ResponseBody
    ResponseEntity<String> login(@RequestParam String mail,
                                 @RequestParam String password,
                                 HttpSession session) {
        try {
            if (session.getAttribute("userId") != null) {
                throw new BadRequestException("You`re already log in");
            }

            User user = userService.login(mail, password);

            session.setAttribute("userId", user.getId());

            return new ResponseEntity<>("Login success", HttpStatus.OK);
        } catch (Exception e) {
            return errorHandlingWithResponseEntity(e);
        }
    }

    @GetMapping(path = "/logout")
    public @ResponseBody
    ResponseEntity<String> logout(HttpSession session) {
        try {
            if (session.getAttribute("userId") == null) {
                throw new UnauthorizedException("You`re not log in");
            }

            userService.logout((long) session.getAttribute("userId"));

            session.removeAttribute("userId");

            return new ResponseEntity<>("Logout success", HttpStatus.OK);
        } catch (Exception e) {
            return errorHandlingWithResponseEntity(e);
        }
    }

    private ResponseEntity<String> errorHandlingWithResponseEntity(Exception e) {
        if (e.getCause() instanceof UnauthorizedException) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } else if (e.getCause() instanceof ServiceException) {
            if (e.getCause() instanceof NoAccessException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
            }
            if (e.getCause() instanceof NotFoundException) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        System.err.println(e.getMessage());
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String errorHandlingWithModel(Exception e, Model model) {
        if (e.getCause() instanceof UnauthorizedException) {
            model.addAttribute("error", e.getMessage());
            return "401";
        } else if (e.getCause() instanceof ServiceException) {
            if (e.getCause() instanceof NoAccessException) {
                model.addAttribute("error", e.getMessage());
                return "403";
            } else if (e.getCause() instanceof NotFoundException) {
                model.addAttribute("error", e.getMessage());
                return "404";
            } else {
                model.addAttribute("error", e.getMessage());
                return "400";
            }
        }
        System.err.println(e.getMessage());
        model.addAttribute("error", "Something went wrong");
        return "500";
    }

}
