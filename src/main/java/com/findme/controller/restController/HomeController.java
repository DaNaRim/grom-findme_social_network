package com.findme.controller.restController;

import com.findme.model.Post;
import com.findme.model.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HomeController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public HomeController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @PostMapping("/registerUser")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody User user) throws Exception {

        return userService.registerUser(user);
    }

    @GetMapping("/feeds")
    public List<Post> getFeeds(@RequestParam(required = false, defaultValue = "0") String startFrom) throws Exception {

        return postService.getFeeds(SecurityUtil.getAuthorizedUserId(), Long.parseLong(startFrom));
    }

}
