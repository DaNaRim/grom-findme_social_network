package com.findme.dao;

import com.findme.model.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDao {

    Post save(Post post);

    Post findById(long id);

    Post update(Post post);

    void delete(Post post);

    List<Post> findByUserPagePosted(long userId, long startFrom);

    List<Post> findByUserPostedAndUserPagePosted(long userPostedId, long userPagePostedId, long startFrom);

    List<Post> findByUserPagePostedOnlyFriends(long userId, long startFrom);

    List<Post> getFeedsByUser(long userId, long startFrom);

    boolean isPostMissing(long id);

    Long findUserPostedId(long postId);

    Long findUserPagePostedId(long postId);
}
