package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String mail;
    private String password;

    private Integer age;
    private String country; //TODO from existed date
    private String city;
    private String school;
    private String university;
    private String religion;

    private Date dateRegistered;
    private Date dateLastActive;

    private List<Message> messagesSent;
    private List<Message> messagesReceived;
//    private String[] interests;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "LAST_NAME", nullable = false, length = 30)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "PHONE", unique = true, nullable = false, length = 15)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "MAIL", unique = true, nullable = false)
    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Column(name = "PASSWORD", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "AGE")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name = "COUNTRY", length = 30)
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "CITY", length = 30)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "SCHOOL", length = 30)
    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Column(name = "UNIVERSITY", length = 30)
    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Column(name = "RELIGION", length = 30)
    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    @Column(name = "DATE_REGISTERED", insertable = false)
    public Date getDateRegistered() {
        return new Date(dateRegistered.getTime());
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = new Date(dateRegistered.getTime());
    }

    @Column(name = "DATE_LAST_ACTIVE", nullable = false)
    public Date getDateLastActive() {
        return new Date(dateLastActive.getTime());
    }

    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = new Date(dateLastActive.getTime());
    }

    @OneToMany(mappedBy = "userFrom")
    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    @OneToMany(mappedBy = "userTo")
    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }
}
