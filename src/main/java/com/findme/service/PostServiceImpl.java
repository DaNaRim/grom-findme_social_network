package com.findme.service;

import com.findme.dao.PostDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;

public class PostServiceImpl implements PostService {

    private final PostDao postDao;
    private final UserService userService;
    private final RelationshipService relationshipService;

    @Autowired
    public PostServiceImpl(PostDao postDao, UserService userService, RelationshipService relationshipService) {
        this.postDao = postDao;
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @Override
    public Post createPost(long actionUserId, Post post) throws BadRequestException, InternalServerException {

        validateCreatePost(actionUserId, post);

        User userPosted = new User();
        userPosted.setId(actionUserId);
        post.setUserPosted(userPosted);

        return postDao.save(post);
    }

    @Override
    public Post updatePost(long actionUserId, Post post) throws BadRequestException, InternalServerException {

        validateUpdatePost(actionUserId, post);

        return postDao.update(post);
    }

    @Override
    public void deletePost(long actionUserId, long id) throws BadRequestException, InternalServerException {

        validateDeletePost(actionUserId, id);

        postDao.delete(postDao.findById(id));
    }

    @Override
    public List<Post> getPostsOnUserPage(long userId) throws InternalServerException {

        return postDao.findByUserPagePosted(userId);
    }

    private void validatePostFields(Post post) throws BadRequestException, InternalServerException {

        if (post.getMessage() == null || post.getUserPagePosted() == null) {
            throw new BadRequestException("Message and userPagePosted are required fields");

        } else if (post.getMessage().length() > 200) {
            throw new BadRequestException("Message length must be < 200");

        } else if (post.getTaggedLocation() != null && post.getTaggedLocation().length() > 128) {
            throw new BadRequestException("TaggedLocation length must be < 200");

        } else if (!userService.isUserExists(post.getUserPagePosted().getId())) {

            throw new BadRequestException("userPagePosted id filed incorrect");

        } else if (post.getTaggedUsers() != null) {

            for (User user : post.getTaggedUsers()) {
                if (!userService.isUserExists(user.getId())) {
                    throw new BadRequestException("Tagged users ids filed incorrect");
                }
            }
        }
    }

    private void validateCreatePost(long userPosted, Post post) throws BadRequestException, InternalServerException {

        long userPagePostedId = post.getUserPagePosted().getId();

        validatePostFields(post);

        if (userPosted != userPagePostedId
                && !relationshipService.areUsersFriends(userPosted, userPagePostedId)) {

            throw new BadRequestException("You can create posts only on your page and your friends pages");
        }
    }

    private void validateUpdatePost(long actionUserId, Post post) throws BadRequestException, InternalServerException {

        validatePostFields(post);

        if (postDao.findById(post.getId()) == null) {
            throw new BadRequestException("Post id filed incorrect");

        } else if (actionUserId != post.getUserPosted().getId()) {
            throw new BadRequestException("You can`t update post that you don`t posted");
        }

        Calendar deletePostTimeLimit = Calendar.getInstance();
        deletePostTimeLimit.setTime(postDao.findDatePosted(post.getId()));
        deletePostTimeLimit.add(Calendar.MINUTE, 5);

        if (deletePostTimeLimit.before(Calendar.getInstance())) {
            throw new BadRequestException("You have only 5 minutes after posted for update");
        }
    }

    private void validateDeletePost(long actionUserId, long postId)
            throws BadRequestException, InternalServerException {

        if (postDao.findById(postId) == null) {
            throw new BadRequestException("Post id filed incorrect");

        } else if (actionUserId != postDao.findUserPostedId(postId)
                && actionUserId != postDao.findUserPagePostedId(postId)) {

            throw new BadRequestException("Can`t delete post because you don`t post it or it posted not in your page");
        }
    }
}
