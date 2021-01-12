package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MESSAGE", nullable = false)
    private String message;

    @Column(name = "TAGGED_LOCATION")
    private String taggedLocation;

    @ManyToMany
    @JoinTable(name = "POST_TAGGED_USERS",
            joinColumns = {@JoinColumn(name = "POST_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "TAGGED_USER_ID", nullable = false, updatable = false)})
    private List<User> taggedUsers;

    @ManyToOne
    @JoinColumn(name = "USER_PAGE_POSTED", nullable = false, updatable = false)
    private User userPagePosted;

    //system fields

    @ManyToOne
    @JoinColumn(name = "USER_POSTED", nullable = false, updatable = false)
    private User userPosted;

    @Column(name = "DATE_POSTED", insertable = false, updatable = false)
    private Date datePosted;

    @Column(name = "DATE_UPDATED", insertable = false)
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
