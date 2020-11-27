package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{userId}")
    public String profile(Model model, @PathVariable String userId) {
        try {
            long id = Long.parseLong(userId);

            User user = userService.findById(id);

            model.addAttribute("user", user);
            return "profile";
        } catch (NotFoundException e) {

            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (NumberFormatException e) {

            model.addAttribute("error", "Field filed incorrect");
            return "400";
        } catch (Exception e) {
            System.err.println(e.getMessage());
            model.addAttribute("error", "Something went wrong");
            return "500";
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
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping(path = "/userLogin")
    public @ResponseBody
    ResponseEntity<String> loginUser(HttpSession session,
                                     @RequestParam("mail") String mail,
                                     @RequestParam("password") String password) {
        try {
            if (session.getAttribute("user") != null) {
                throw new BadRequestException("You`re already log in");
            }

            User user = userService.loginUser(mail, password);

            userService.updateDateLastActive(user);
            user.setDateLastActive(new Date());

            session.setAttribute("user", user);

            return new ResponseEntity<>("Login success", HttpStatus.OK);
        } catch (NotFoundException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/userLogout")
    public @ResponseBody
    ResponseEntity<String> logoutUser(HttpSession session) {
        try {
            User user = (User) session.getAttribute("user");

            if (user == null) {
                throw new BadRequestException("You`re not log in");
            }

            userService.updateDateLastActive(user);

            session.removeAttribute("user");

            return new ResponseEntity<>("Logout success", HttpStatus.OK);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            System.err.println(e.getMessage());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
