package com.findme.controller.restController;

import com.findme.model.Post;
import com.findme.model.PostFilter;
import com.findme.service.PostService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) throws Exception {

        return postService.createPost(SecurityUtil.getAuthorizedUserId(), post);
    }

    @PutMapping("/update")
    public Post updatePost(@RequestBody Post post) throws Exception {

        return postService.updatePost(SecurityUtil.getAuthorizedUserId(), post);
    }

    @DeleteMapping("/delete")
    public void deletePost(@RequestParam String postId) throws Exception {

        postService.deletePost(SecurityUtil.getAuthorizedUserId(), Long.parseLong(postId));
    }

    @GetMapping("/getByFilter")
    public List<Post> getPostsOnUserPageByFilter(@RequestParam String userId,
                                                 @RequestBody PostFilter postFilter) throws Exception {

        return postService.getPostsOnUserPageByFilter(Long.parseLong(userId), postFilter);
    }

}
