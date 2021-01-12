package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //required fields

    @Column(name = "FIRST_NAME", nullable = false, length = 30)
    private String firstName;

    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;

    @Column(name = "PHONE", unique = true, nullable = false, length = 15)
    private String phone;

    @Column(name = "MAIL", unique = true, nullable = false)
    private String mail;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    //optional fields

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "COUNTRY", length = 30)
    private String country; //TODO from existed date

    @Column(name = "CITY", length = 30)
    private String city;

    @Column(name = "SCHOOL", length = 30)
    private String school;

    @Column(name = "UNIVERSITY", length = 30)
    private String university;

    @Column(name = "RELIGION", length = 30)
    private String religion;

    //system fields

    @Column(name = "DATE_REGISTERED", insertable = false, updatable = false)
    private Date dateRegistered;

    @Column(name = "DATE_LAST_ACTIVE", nullable = false)
    private Date dateLastActive;

    //unrealized fields

    @OneToMany(mappedBy = "userFrom")
    private List<Message> messagesSent;

    @OneToMany(mappedBy = "userTo")
    private List<Message> messagesReceived;

//    private String[] interests;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }


    public Integer getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getSchool() {
        return school;
    }

    public String getUniversity() {
        return university;
    }

    public String getReligion() {
        return religion;
    }


    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = new Date(dateLastActive.getTime());
    }
}
