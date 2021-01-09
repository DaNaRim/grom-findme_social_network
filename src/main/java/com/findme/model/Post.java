package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "POST")
public class Post {

    private Long id;
    private String message;
    private String taggedLocation;
    private List<User> taggedUsers;
    private Date datePosted;
    private Date dateUpdated;
    private User userPosted;
    private User userPagePosted;
    //TODO levels permissions
    //TODO comments

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "MESSAGE", nullable = false)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "TAGGED_LOCATION")
    public String getTaggedLocation() {
        return taggedLocation;
    }

    public void setTaggedLocation(String taggedLocation) {
        this.taggedLocation = taggedLocation;
    }

    @ManyToMany
    @JoinTable(name = "POST_TAGGED_USERS",
            joinColumns = {@JoinColumn(name = "POST_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "TAGGED_USER_ID", nullable = false, updatable = false)})
    public List<User> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(List<User> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    @Column(name = "DATE_POSTED", insertable = false)
    public Date getDatePosted() {
        return new Date(datePosted.getTime());
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = new Date(datePosted.getTime());
    }

    @Column(name = "DATE_UPDATED")
    public Date getDateUpdated() {
        return new Date(dateUpdated.getTime());
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = new Date(dateUpdated.getTime());
    }

    @ManyToOne
    @JoinColumn(name = "USER_POSTED", nullable = false, updatable = false)
    public User getUserPosted() {
        return userPosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }

    @ManyToOne
    @JoinColumn(name = "USER_PAGE_POSTED", nullable = false, updatable = false)
    public User getUserPagePosted() {
        return userPagePosted;
    }

    public void setUserPagePosted(User userPagePosted) {
        this.userPagePosted = userPagePosted;
    }
}
