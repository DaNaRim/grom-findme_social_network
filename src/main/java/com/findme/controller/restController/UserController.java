package com.findme.controller.restController;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/registration")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user) throws Exception {

        return userService.registerUser(user);
    }

    @PutMapping(path = "/updateUser")
    public User updateUser(@RequestBody User user,
                           @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        user.setId(userId);
        return userService.updateUser(user);
    }

    @PostMapping(path = "/login")
    public void login(@RequestParam String mail,
                      @RequestParam String password,
                      HttpSession session) throws Exception {

        if (session.getAttribute("userId") != null) {
            throw new BadRequestException("You`re already log in");
        }
        User user = userService.login(mail, password);

        session.setAttribute("userId", user.getId());
    }

    @GetMapping(path = "/logout")
    public void logout(HttpSession session) throws Exception {

        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedException("You`re not log in");
        }
        session.removeAttribute("userId");
    }
}
