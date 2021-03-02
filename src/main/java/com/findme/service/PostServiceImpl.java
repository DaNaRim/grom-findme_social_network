package com.findme.service;

import com.findme.dao.PostDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Post;
import com.findme.model.PostFilter;
import com.findme.model.User;
import com.findme.validator.postValidator.MessageValidator;
import com.findme.validator.postValidator.PostValidator;
import com.findme.validator.postValidator.PostValidatorParams;
import com.findme.validator.postValidator.TaggedLocationValidator;
import com.findme.validator.postValidator.TaggedUsersValidator;
import com.findme.validator.postValidator.UserPagePostedValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
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
    public List<Post> getPostsOnUserPage(long userId, long startFrom) throws InternalServerException {

        return postDao.findByUserPagePosted(userId, startFrom);
    }

    @Override
    public List<Post> getPostsOnUserPageByFilter(long userPageId, PostFilter postFilter)
            throws NotFoundException, InternalServerException {

        long startFrom = postFilter.getStartFrom() == null ? 0 : postFilter.getStartFrom();
        List<Post> posts;

        if (postFilter.isOnlyUserPosts()) {
            posts = postDao.findByUserPostedAndUserPagePosted(userPageId, userPageId, startFrom);

        } else if (postFilter.isOnlyFriendsPosts()) {
            posts = postDao.findByUserPagePostedOnlyFriends(userPageId, startFrom);

        } else if (postFilter.getUserId() != null) {
            posts = postDao.findByUserPostedAndUserPagePosted(postFilter.getUserId(), userPageId, startFrom);

        } else {
            posts = postDao.findByUserPagePosted(userPageId, startFrom);
        }

        if (posts.isEmpty()) {
            throw new NotFoundException("Posts not found by this filter params");
        }

        return posts;
    }

    @Override
    public List<Post> getFeeds(long userId, long startFrom) throws NotFoundException, InternalServerException {

        List<Post> posts = postDao.getFeedsByUser(userId, startFrom);

        if (posts.isEmpty()) {
            throw new NotFoundException("No posts to show");
        }

        return posts;
    }

    private void validatePostFields(Post post) throws BadRequestException, InternalServerException {

        List<Long> taggedUserIds = post.getTaggedUsers().stream().map(User::getId).collect(Collectors.toList());

        PostValidator postValidator = new MessageValidator();
        postValidator.linkWith(new TaggedLocationValidator())
                .linkWith(new TaggedUsersValidator(userService))
                .linkWith(new UserPagePostedValidator(userService));

        PostValidatorParams params = new PostValidatorParams.Builder()
                .withMessage(post.getMessage())
                .withTaggedLocation(post.getTaggedLocation())
                .withTaggedUsersIds(taggedUserIds)
                .withUserPagePostedId(post.getUserPagePosted().getId())
                .withUserPostedId(post.getUserPosted().getId())
                .build();

        postValidator.check(params);
    }

    private void validateCreatePost(long userPosted, Post post) throws BadRequestException, InternalServerException {

        post.setUserPosted(new User(userPosted));

        validatePostFields(post);

        long userPagePostedId = post.getUserPagePosted().getId();

        if (userPosted != userPagePostedId
                && !relationshipService.areUsersFriends(userPosted, userPagePostedId)) {

            throw new BadRequestException("You can create posts only on your page and your friends pages");
        }
    }

    private void validateUpdatePost(long actionUserId, Post post) throws BadRequestException, InternalServerException {

        Long userPosted = postDao.findUserPostedId(post.getId());
        post.setUserPosted(new User(userPosted));

        validatePostFields(post);

        if (postDao.isPostMissing(post.getId())) {
            throw new BadRequestException("Post id filed incorrect");

        } else if (actionUserId != postDao.findUserPostedId(post.getId())) {
            throw new BadRequestException("You can`t update post that you don`t posted");
        }
    }

    private void validateDeletePost(long actionUserId, long postId)
            throws BadRequestException, InternalServerException {

        if (postDao.isPostMissing(postId)) {
            throw new BadRequestException("Post id filed incorrect");

        } else if (actionUserId != postDao.findUserPostedId(postId)
                && actionUserId != postDao.findUserPagePostedId(postId)) {

            throw new BadRequestException("Can`t delete post because you don`t post it or it posted not in your page");
        }
    }
}
