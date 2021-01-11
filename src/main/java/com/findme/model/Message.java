package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATE_SENT")
    private Date dateSent;

    @Column(name = "DATE_READ")
    private Date dateRead;

    @ManyToOne
    @JoinColumn(name = "USER_FROM", nullable = false, updatable = false)
    private User userFrom;

    @ManyToOne
    @JoinColumn(name = "USER_TO", nullable = false, updatable = false)
    private User userTo;

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

    public Date getDateSent() {
        return new Date(dateSent.getTime());
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = new Date(dateSent.getTime());
    }

    public Date getDateRead() {
        return new Date(dateRead.getTime());
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = new Date(dateRead.getTime());
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
}
