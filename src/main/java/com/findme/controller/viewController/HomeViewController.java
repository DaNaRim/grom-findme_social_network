package com.findme.controller.viewController;

import com.findme.model.Post;
import com.findme.service.PostService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeViewController {

    private final PostService postService;

    @Autowired
    public HomeViewController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/feeds")
    public String getFeeds(@RequestParam(required = false, defaultValue = "0") String startFrom,
                           Model model) throws Exception {

        List<Post> posts = postService.getFeeds(SecurityUtil.getAuthorizedUserId(), Long.parseLong(startFrom));

        model.addAttribute("posts", posts);
        return "feeds";
    }
}
