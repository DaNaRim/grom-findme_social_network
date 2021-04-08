package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Relationship", schema = "public")
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_from", nullable = false, updatable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "user_to", nullable = false, updatable = false)
    private User userTo;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

    //system fields

    @Column(name = "action_user_id", nullable = false)
    private Long actionUserId;

    @Column(name = "date_modify", insertable = false, nullable = false)
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public RelationshipStatus getStatus() {
        return status;
    }

    public void setStatus(RelationshipStatus status) {
        this.status = status;
    }


    public void setActionUserId(Long actionUserId) {
        this.actionUserId = actionUserId;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = new Date(dateModify.getTime());
    }
}
