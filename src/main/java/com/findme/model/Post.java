package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Post", schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(name = Post.QUERY_FIND_BY_USER_PAGE_POSTED,
                query = "SELECT * FROM Post " +
                        "WHERE user_page_posted = :" + Post.ATTRIBUTE_USER_PAGE_POSTED_ID +
                        " ORDER BY date_posted",
                resultClass = Post.class),

        @NamedNativeQuery(name = Post.QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED,
                query = "SELECT * FROM Post " +
                        "WHERE user_posted = :" + Post.ATTRIBUTE_USER_POSTED_ID +
                        " AND user_page_posted = :" + Post.ATTRIBUTE_USER_PAGE_POSTED_ID +
                        " ORDER BY date_posted",
                resultClass = Post.class),

        @NamedNativeQuery(name = Post.QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS,
                query = "SELECT * FROM Post " +
                        "WHERE user_page_posted = :" + Post.ATTRIBUTE_USER_PAGE_POSTED_ID +
                        " AND user_posted != :" + Post.ATTRIBUTE_USER_PAGE_POSTED_ID +
                        " ORDER BY date_posted",
                resultClass = Post.class),


        @NamedNativeQuery(name = Post.QUERY_FIND_USER_POSTED_BY_ID,
                query = "SELECT user_posted FROM Post WHERE id = :" + Post.ATTRIBUTE_ID),

        @NamedNativeQuery(name = Post.QUERY_FIND_USER_PAGE_POSTED_BY_ID,
                query = "SELECT user_page_posted FROM Post WHERE id = :" + Post.ATTRIBUTE_ID)
})
public class Post {

    public static final String QUERY_FIND_BY_USER_PAGE_POSTED = "Post.findByUserPagePosted";
    public static final String QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED = "Post.findByUserPostedAndUserPagePosted";
    public static final String QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS = "Post.findByUserPagePostedOnlyFriends";

    public static final String QUERY_FIND_USER_POSTED_BY_ID = "Post.findUserPostedById";
    public static final String QUERY_FIND_USER_PAGE_POSTED_BY_ID = "Post.findUserPagePostedById";


    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_USER_PAGE_POSTED_ID = "userPagePosted";
    public static final String ATTRIBUTE_USER_POSTED_ID = "userPosted";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "tagged_location")
    private String taggedLocation;

    @ManyToMany
    @JoinTable(name = "Post_tagged_users",
            joinColumns = {@JoinColumn(name = "post_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tagged_user_id", nullable = false, updatable = false)})
    private List<User> taggedUsers;

    @ManyToOne
    @JoinColumn(name = "user_page_posted", nullable = false, updatable = false)
    private User userPagePosted;

    //system fields

    @ManyToOne
    @JoinColumn(name = "user_posted", nullable = false, updatable = false)
    private User userPosted;

    @Column(name = "date_posted", insertable = false, updatable = false)
    private Date datePosted;

    @Column(name = "date_updated", insertable = false)
    private Date dateUpdated;

    //TODO levels permissions
    //TODO comments

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTaggedLocation() {
        return taggedLocation;
    }

    public List<User> getTaggedUsers() {
        return taggedUsers;
    }

    public User getUserPagePosted() {
        return userPagePosted;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = new Date(dateUpdated.getTime());
    }


    public User getUserPosted() {
        return userPosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }
}
