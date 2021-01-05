package com.findme.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MESSAGE")
public class Message {

    private Long id;
    private String text;
    private Date dateSent;
    private Date dateRead;
    private User userFrom;
    private User userTo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "TEXT")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "DATE_SENT")
    public Date getDateSent() {
        return new Date(dateSent.getTime());
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = new Date(dateSent.getTime());
    }

    @Column(name = "DATE_READ")
    public Date getDateRead() {
        return new Date(dateRead.getTime());
    }

    public void setDateRead(Date dateRead) {
        this.dateRead = new Date(dateRead.getTime());
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
}
