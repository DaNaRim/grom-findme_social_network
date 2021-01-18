package com.findme.model;

public class PostFilter {

    private boolean onlyUserPosts;
    private boolean onlyFriendsPosts;
    private Long userId; //onlyByUser
    private Long startFrom;

    public boolean isOnlyUserPosts() {
        return onlyUserPosts;
    }

    public void setOnlyUserPosts(boolean onlyUserPosts) {
        this.onlyUserPosts = onlyUserPosts;
    }

    public boolean isOnlyFriendsPosts() {
        return onlyFriendsPosts;
    }

    public void setOnlyFriendsPosts(boolean onlyFriendsPosts) {
        this.onlyFriendsPosts = onlyFriendsPosts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(Long startFrom) {
        this.startFrom = startFrom;
    }
}
