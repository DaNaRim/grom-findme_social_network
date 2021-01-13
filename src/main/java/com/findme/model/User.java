package com.findme.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users", schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(name = User.QUERY_FIND_BY_MAIL,
                query = "SELECT * FROM Users WHERE mail = :" + User.ATTRIBUTE_MAIL,
                resultClass = User.class),

        @NamedNativeQuery(name = User.QUERY_IS_EXISTS,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE id = :" + User.ATTRIBUTE_ID + ")"),

        @NamedNativeQuery(name = User.QUERY_ARE_PHONE_AND_MAIL_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + User.ATTRIBUTE_PHONE +
                        " OR mail = :" + User.ATTRIBUTE_MAIL + ")")
})
public class User {

    public static final String QUERY_FIND_BY_MAIL = "User.findByMail";
    public static final String QUERY_IS_EXISTS = "User.isExists";
    public static final String QUERY_ARE_PHONE_AND_MAIL_BUSY = "User.arePhoneAndMailBusy";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_PHONE = "phone";
    public static final String ATTRIBUTE_MAIL = "mail";

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

    @Column(name = "age")
    private Integer age;

    @Column(name = "country", length = 30)
    private String country; //TODO from existed date

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "school", length = 30)
    private String school;

    @Column(name = "university", length = 30)
    private String university;

    @Column(name = "religion", length = 30)
    private String religion;

    //system fields

    @Column(name = "date_registered", insertable = false, updatable = false)
    private Date dateRegistered;

    @Column(name = "date_last_active", nullable = false)
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
