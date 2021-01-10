package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    Post createPost(long actionUserId, Post post) throws BadRequestException, InternalServerException;

    Post updatePost(long actionUserId, Post post) throws BadRequestException, InternalServerException;

    void deletePost(long actionUserId, long id) throws BadRequestException, InternalServerException;

    List<Post> getPostsOnUserPage(long userId) throws InternalServerException;
}
