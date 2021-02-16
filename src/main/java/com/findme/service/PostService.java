package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Post;
import com.findme.model.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PostService {

    Post createPost(long actionUserId, Post post) throws BadRequestException, InternalServerException;

    Post updatePost(long actionUserId, Post post) throws BadRequestException, InternalServerException;

    void deletePost(long actionUserId, long id) throws BadRequestException, InternalServerException;

    List<Post> getPostsOnUserPage(long userId, long startFrom) throws InternalServerException;

    List<Post> getPostsOnUserPageByFilter(long userPageId, PostFilter postFilter)
            throws InternalServerException, NotFoundException;

    List<Post> getFeeds(long userId, long startFrom) throws InternalServerException, NotFoundException;
}
