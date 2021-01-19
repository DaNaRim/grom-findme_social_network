package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Post;
import com.findme.model.Relationship;
import com.findme.model.User;
import com.findme.service.PostService;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserController {

    private final UserService userService;
    private final RelationshipService relationshipService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService, PostService postService) {
        this.userService = userService;
        this.relationshipService = relationshipService;
        this.postService = postService;
    }

    @GetMapping(path = "/{userPageIdStr}")
    public String profile(@PathVariable String userPageIdStr, Model model, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");
            boolean isMyPage = false;

            long userPageId;
            try {
                userPageId = Long.parseLong(userPageIdStr);

            } catch (NumberFormatException e) {
                throw new BadRequestException("Id`s filed incorrect");
            }

            User user = userService.findById(userPageId);
            List<Post> postsOnPage = postService.getPostsOnUserPage(userPageId, 0);

            Relationship ourRelationship = null;
            if (actionUserId != null) {

                if (actionUserId == userPageId) {
                    isMyPage = true;
                } else {
                    ourRelationship = relationshipService.getOurRelationshipToUser(
                            (long) session.getAttribute("userId"), userPageId);
                }

                try {
                    userService.updateDateLastActive(actionUserId);
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    System.err.println(sw.toString());
                }
            }

            model.addAttribute("user", user);
            model.addAttribute("isMyPage", isMyPage);
            model.addAttribute("ourRelationship", ourRelationship);
            model.addAttribute("postsOnPage", postsOnPage);
            return "profile";
        } catch (NotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        } catch (BadRequestException e) {
            model.addAttribute("error", e.getMessage());
            return "400";
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            model.addAttribute("error", "Something went wrong");
            return "500";
        }
    }

    @GetMapping(path = "/registration")
    public String registrationForm() {
        return "registration";
    }

    @PostMapping(path = "/registration")
    public @ResponseBody
    ResponseEntity<Object> registerUser(@RequestBody User user, Model model) {
        try {
            User newUser = userService.registerUser(user);

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
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

            try {
                userService.updateDateLastActive(user.getId());
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.err.println(sw.toString());
            }

            return new ResponseEntity<>("Login success", HttpStatus.OK);
        } catch (BadRequestException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/logout")
    public @ResponseBody
    ResponseEntity<String> logout(HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You`re not log in");
            }

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                System.err.println(sw.toString());
            }

            session.removeAttribute("userId");

            return new ResponseEntity<>("Logout success", HttpStatus.OK);
        } catch (UnauthorizedException e) {

            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
