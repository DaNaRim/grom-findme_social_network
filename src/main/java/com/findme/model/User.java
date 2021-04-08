package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //required fields

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "phone", unique = true, nullable = false, length = 15)
    private String phone;

    @Column(name = "mail", unique = true, nullable = false)
    private String mail;

    @Column(name = "password", nullable = false)
    private String password;

    //optional fields

    @Column(name = "age", insertable = false)
    private Integer age;

    @Column(name = "country", insertable = false, length = 30)
    private String country; //TODO from existed date

    @Column(name = "city", insertable = false, length = 30)
    private String city;

    @Column(name = "school", insertable = false, length = 30)
    private String school;

    @Column(name = "university", insertable = false, length = 30)
    private String university;

    @Column(name = "religion", insertable = false, length = 30)
    private String religion;

    //system fields

    @Column(name = "date_registered", insertable = false, updatable = false)
    private Date dateRegistered;

    @Column(name = "date_last_active", insertable = false, nullable = false)
    private Date dateLastActive;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
