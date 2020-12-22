package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RELATIONSHIP")
public class Relationship {

    private Long id;
    private User userFrom;
    private User userTo;
    private RelationshipStatus status;
    private Date dateModify;

    public Relationship() {
    }

    public Relationship(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public Relationship(User userFrom, User userTo, RelationshipStatus status) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "USER_FROM", nullable = false, updatable = false)
    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    @ManyToOne
    @JoinColumn(name = "USER_TO", nullable = false, updatable = false)
    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }

    @Column(name = "DATE_MODIFY", nullable = false)
    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }
}
