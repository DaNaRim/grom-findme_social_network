package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE", schema = "public")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEXT", nullable = false, length = 140)
    private String text;

    @ManyToOne
    @JoinColumn(name = "USER_FROM", nullable = false, updatable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "USER_TO", nullable = false, updatable = false)
    private User userTo;

    @Column(name = "DATE_SENT", insertable = false, updatable = false)
    private Date dateSent;

    @Column(name = "DATE_READ")
    private Date dateRead;

    @Column(name = "DATE_EDITED")
    private Date dateEdited;

    public Message() {
    }

    public Message(String text, User userFrom, User userTo) {
        this.text = text;
        this.userFrom = userFrom;
        this.userTo = userTo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public Date getDateSent() {
        return dateSent == null ? null : new Date(dateSent.getTime());
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent == null ? null : new Date(dateSent.getTime());
    }

    public Date getDateRead() {
        return dateRead == null ? null : new Date(dateRead.getTime());
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = dateRead == null ? null : new Date(dateRead.getTime());
    }

    public Date getDateEdited() {
        return dateEdited == null ? null : new Date(dateEdited.getTime());
    }

    public void setDateEdited(Date dateEdited) {
        this.dateEdited = dateEdited == null ? null : new Date(dateEdited.getTime());
    }
}