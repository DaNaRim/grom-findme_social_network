package com.findme.controller.restController;

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
                           @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        return postService.createPost(userId, post);
    }

    @PutMapping(path = "/update")
    public Post updatePost(@RequestBody Post post,
                           @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        return postService.updatePost(userId, post);
    }

    @DeleteMapping(path = "/delete")
    public void deletePost(@RequestParam String postId,
                           @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        postService.deletePost(userId, Long.parseLong(postId));
    }

    @GetMapping(path = "/getByFilter")
    public List<Post> getPostsOnUserPageByFilter(@RequestParam String userId,
                                                 @RequestBody PostFilter postFilter) throws Exception {

        return postService.getPostsOnUserPageByFilter(Long.parseLong(userId), postFilter);
    }

    @GetMapping(path = "/feed")
    public List<Post> getFeeds(@RequestParam(required = false, defaultValue = "0") String startFrom,
                               @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }
        return postService.getFeeds(userId, Long.parseLong(startFrom));
    }
}
