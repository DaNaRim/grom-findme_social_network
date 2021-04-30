package com.findme.controller.restController;

import com.findme.model.User;
import com.findme.service.UserService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestBody User user) throws Exception {

        user.setId(SecurityUtil.getAuthorizedUserId());
        return userService.updateUser(user);
    }

}
