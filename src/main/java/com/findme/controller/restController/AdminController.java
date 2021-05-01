package com.findme.controller.restController;

import com.findme.service.PostService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final PostService postService;

    @Autowired
    public AdminController(PostService postService) {
        this.postService = postService;
    }

    @DeleteMapping("/deletePost")
    public void deletePost(@RequestParam String postId) throws Exception {

        postService.deletePost(SecurityUtil.getAuthorizedUserId(), Long.parseLong(postId));
    }
}
