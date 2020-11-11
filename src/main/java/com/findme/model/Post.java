package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "POST")
public class Post {

    private Long id;
    private String message;
    private Date datePosted;
    private User userPosted;
    //TODO levels permissions
    //TODO comments

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Column(name = "DATE_POSTED")
    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    @ManyToOne
    @JoinColumn(name = "USER_POSTED", nullable = false, updatable = false)
    public User getUserPosted() {
        return userPosted;
    }

    public void setUserPosted(User userPosted) {
        this.userPosted = userPosted;
    }
}
