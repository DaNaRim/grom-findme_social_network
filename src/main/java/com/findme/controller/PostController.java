package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Post;
import com.findme.model.PostFilter;
import com.findme.service.PostService;
import com.findme.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping(path = "/create")
    public @ResponseBody
    ResponseEntity<Object> createPost(@RequestBody Post post, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Post newPost = postService.createPost(actionUserId, post);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            return new ResponseEntity<>(newPost, HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update")
    public @ResponseBody
    ResponseEntity<Object> updatePost(@RequestBody Post post, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            Post updatedPost = postService.updatePost(actionUserId, post);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete")
    public @ResponseBody
    ResponseEntity<String> deletePost(@RequestParam String postIdStr, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long postId;
            try {
                postId = Long.parseLong(postIdStr);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            postService.deletePost(actionUserId, postId);

            try {
                userService.updateDateLastActive(actionUserId);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            return new ResponseEntity<>("Post deleted", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getByFilter")
    public @ResponseBody
    ResponseEntity<Object> getPostsOnUserPageByFilter(@RequestParam String userIdStr,
                                                      @RequestBody PostFilter postFilter,
                                                      HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            long userId;

            try {
                userId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            List<Post> posts = postService.getPostsOnUserPageByFilter(userId, postFilter);

            if (actionUserId != null) {
                try {
                    userService.updateDateLastActive(actionUserId);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }

            return new ResponseEntity<>(posts, HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            System.err.println(sw.toString());
            return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
