package com.findme.controller.restController;

import com.findme.exception.BadRequestException;
import com.findme.exception.UnauthorizedException;
import com.findme.model.Post;
import com.findme.model.PostFilter;
import com.findme.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequestMapping(path = "/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(path = "/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post,
                           @SessionAttribute Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return postService.createPost(userId, post);
    }

    @PutMapping(path = "/update")
    public Post updatePost(@RequestBody Post post,
                           @SessionAttribute Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return postService.updatePost(userId, post);
    }

    @DeleteMapping(path = "/delete")
    public void deletePost(@RequestParam String postId,
                           @SessionAttribute Long userId) throws Exception {

        long postId1;
        try {
            postId1 = Long.parseLong(postId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Fields filed incorrect");
        }

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        postService.deletePost(userId, postId1);
    }

    @GetMapping(path = "/getByFilter")
    public List<Post> getPostsOnUserPageByFilter(@RequestParam String userId,
                                                 @RequestBody PostFilter postFilter) throws Exception {

        long userId1;
        try {
            userId1 = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Fields filed incorrect");
        }

        return postService.getPostsOnUserPageByFilter(userId1, postFilter);
    }

    @GetMapping(path = "/feed")
    public List<Post> getFeeds(@RequestParam String startFrom,
                               @SessionAttribute Long userId) throws Exception {

        long startFrom1;
        try {
            startFrom1 = Long.parseLong(startFrom);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Fields filed incorrect");
        }

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return postService.getFeeds(userId, startFrom1);
    }
}
