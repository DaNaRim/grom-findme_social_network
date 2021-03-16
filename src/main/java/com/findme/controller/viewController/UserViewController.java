package com.findme.controller.viewController;

import com.findme.exception.BadRequestException;
import com.findme.model.Post;
import com.findme.model.Relationship;
import com.findme.model.User;
import com.findme.service.PostService;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserViewController {

    private final UserService userService;
    private final RelationshipService relationshipService;
    private final PostService postService;

    @Autowired
    public UserViewController(UserService userService, RelationshipService relationshipService, PostService postService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
        this.postService = postService;
    }

    @GetMapping(path = "/{userId}")
    public String profile(@PathVariable String userId, Model model, HttpSession session) throws Exception {
        Long actionUserId = (Long) session.getAttribute("userId");
        boolean isMyPage = false;

        long userId1;
        try {
            userId1 = Long.parseLong(userId);

        } catch (NumberFormatException e) {
            throw new BadRequestException("Id filed incorrect");
        }

        User user = userService.findById(userId1);
        List<Post> postsOnPage = postService.getPostsOnUserPage(userId1, 0);

        Relationship ourRelationship = null;
        if (actionUserId != null) {

            if (actionUserId == userId1) {
                isMyPage = true;
            } else {
                ourRelationship = relationshipService.getOurRelationshipToUser(
                        (long) session.getAttribute("userId"), userId1);
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("isMyPage", isMyPage);
        model.addAttribute("ourRelationship", ourRelationship);
        model.addAttribute("postsOnPage", postsOnPage);
        return "profile";
    }

    @GetMapping(path = "/registration")
    public String registrationForm() {
        return "registration";
    }


    @GetMapping(path = "/login")
    public String loginForm() {
        return "login";
    }
}
