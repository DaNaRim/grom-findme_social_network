package com.findme.service;

import com.findme.dao.PostDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Post;
import com.findme.model.PostFilter;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

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
    public List<Post> getPostsOnUserPage(long userId) throws InternalServerException {

        return postDao.findByUserPagePosted(userId);
    }

    @Override
    public List<Post> getPostsOnUserPageByFilter(long userPageId, PostFilter postFilter)
            throws NotFoundException, InternalServerException {

        List<Post> posts;

        if (postFilter.isOnlyUserPosts()) {
            posts = postDao.findByUserPostedAndUserPagePosted(userPageId, userPageId);

        } else if (postFilter.isOnlyFriendsPosts()) {
            posts = postDao.findByUserPagePostedOnlyFriends(userPageId);

        } else if (postFilter.getUserId() != null) {
            posts = postDao.findByUserPostedAndUserPagePosted(postFilter.getUserId(), userPageId);

        } else {
            posts = postDao.findByUserPagePosted(userPageId);
        }

        if (posts.isEmpty()) {
            throw new NotFoundException("Posts not found by this filter params");
        }

        return posts;
    }

    private void validatePostFields(Post post) throws BadRequestException, InternalServerException {

        Pattern urlPattern = Pattern.compile("^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$");

        if (post.getMessage() == null || post.getUserPagePosted() == null) {
            throw new BadRequestException("Message and userPagePosted are required fields");

        } else if (post.getMessage().length() > 200) {
            throw new BadRequestException("Message length must be < 200");

        } else if (post.getTaggedLocation() != null && post.getTaggedLocation().length() > 128) {
            throw new BadRequestException("TaggedLocation length must be < 128");

        } else if (userService.isUserMissing(post.getUserPagePosted().getId())) {
            throw new BadRequestException("userPagePosted id filed incorrect");

        } else if (post.getTaggedUsers() != null) {

            for (User user : post.getTaggedUsers()) {
                if (userService.isUserMissing(user.getId())) {
                    throw new BadRequestException("Tagged users ids filed incorrect");

                } else if (user.getId().equals(post.getUserPosted().getId())) {
                    throw new BadRequestException("You can`t tag yourself");
                }
            }
        } else { // checking for contains url
            for (String str : post.getMessage().split(" ")) {
                if (urlPattern.matcher(str).find()) {
                    throw new BadRequestException("Message can`t contain url");
                }
            }
        }
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
