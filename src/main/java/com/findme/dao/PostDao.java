package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostDao {

    Post save(Post post) throws InternalServerException;

    Post findById(long id) throws InternalServerException;

    Post update(Post post) throws InternalServerException;

    void delete(Post post) throws InternalServerException;

    List<Post> findByUserPagePosted(long userId) throws InternalServerException;

    Date findDatePosted(long postId) throws InternalServerException;

    Long findUserPostedId(long postId) throws InternalServerException;

    Long findUserPagePostedId(long postId) throws InternalServerException;
}
