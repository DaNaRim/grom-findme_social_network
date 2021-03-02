package com.findme.validator.postValidator;

import java.util.List;

public class PostValidatorParams {

    private String message;
    private String taggedLocation;
    private List<Long> taggedUserIds;
    private Long userPagePostedId;
    private Long userPostedId;

    public String getMessage() {
        return message;
    }

    public String getTaggedLocation() {
        return taggedLocation;
    }

    public List<Long> getTaggedUsersIds() {
        return taggedUserIds;
    }

    public Long getUserPagePostedId() {
        return userPagePostedId;
    }

    public Long getUserPostedId() {
        return userPostedId;
    }

    public static class Builder {

        private final PostValidatorParams params;

        public Builder() {
            this.params = new PostValidatorParams();
        }

        public Builder withMessage(String message) {
            params.message = message;
            return this;
        }

        public Builder withTaggedLocation(String taggedLocation) {
            params.taggedLocation = taggedLocation;
            return this;
        }

        public Builder withTaggedUsersIds(List<Long> taggedUsersIds) {
            params.taggedUserIds = taggedUsersIds;
            return this;
        }

        public Builder withUserPagePostedId(Long userPagePostedId) {
            params.userPagePostedId = userPagePostedId;
            return this;
        }

        public Builder withUserPostedId(Long userPostedId) {
            params.userPostedId = userPostedId;
            return this;
        }

        public PostValidatorParams build() {
            return params;
        }
    }
}
