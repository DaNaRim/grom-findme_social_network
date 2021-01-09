package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.NotFoundException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Post;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createPost(@ModelAttribute Post post, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            postService.createPost(actionUserId, post);

            return new ResponseEntity<>("Post created", HttpStatus.CREATED);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/update")
    public ResponseEntity<String> updatePost(@ModelAttribute Post post, HttpSession session) {
        try {
            Long actionUserId = (Long) session.getAttribute("userId");

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            postService.updatePost(actionUserId, post);

            return new ResponseEntity<>("Post updated", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deletePost(@RequestParam String postIdStr, HttpSession session) {
        try {
            long postId;
            Long actionUserId = (Long) session.getAttribute("userId");

            try {
                postId = Long.parseLong(postIdStr);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            if (actionUserId == null) {
                throw new UnauthorizedException("You must be authorized to do that");
            }

            postService.deletePost(actionUserId, postId);

            return new ResponseEntity<>("Post deleted", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/getByPage")
    public ResponseEntity<String> getPostsOnUserPage(@RequestParam String userIdStr, Model model) {
        try {
            long userId;

            try {
                userId = Long.parseLong(userIdStr);
            } catch (NumberFormatException e) {
                throw new BadRequestException("Fields filed incorrect");
            }

            List<Post> posts = postService.getPostsOnUserPage(userId);

            model.addAttribute("posts", posts);

            return new ResponseEntity<>("Post created", HttpStatus.OK);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
