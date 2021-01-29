package com.findme.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users", schema = "public")
@NamedNativeQueries({
        @NamedNativeQuery(name = User.QUERY_FIND_BY_MAIL,
                query = "SELECT * FROM Users WHERE mail = :" + User.ATTRIBUTE_MAIL,
                resultClass = User.class),

        @NamedNativeQuery(name = User.QUERY_IS_EXISTS,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE id = :" + User.ATTRIBUTE_ID + ")"),


        @NamedNativeQuery(name = User.QUERY_ARE_PHONE_AND_MAIL_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + User.ATTRIBUTE_PHONE
                        + " OR mail = :" + User.ATTRIBUTE_MAIL + ")"),

        @NamedNativeQuery(name = User.QUERY_IS_PHONE_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + User.ATTRIBUTE_PHONE + ")"),

        @NamedNativeQuery(name = User.QUERY_IS_MAIL_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE mail = :" + User.ATTRIBUTE_MAIL + ")"),

        @NamedNativeQuery(name = User.QUERY_FIND_PHONE,
                query = "SELECT phone FROM Users WHERE id = :" + User.ATTRIBUTE_ID),

        @NamedNativeQuery(name = User.QUERY_FIND_MAIL,
                query = "SELECT mail FROM Users WHERE id = :" + User.ATTRIBUTE_ID),
})
public class User {

    public static final String QUERY_FIND_BY_MAIL = "User.findByMail";
    public static final String QUERY_IS_EXISTS = "User.isExists";

    public static final String QUERY_ARE_PHONE_AND_MAIL_BUSY = "User.arePhoneAndMailBusy";
    public static final String QUERY_IS_PHONE_BUSY = "User.isPhoneBusy";
    public static final String QUERY_IS_MAIL_BUSY = "User.isMailBusy";
    public static final String QUERY_FIND_PHONE = "User.findPhone";
    public static final String QUERY_FIND_MAIL = "User.findMail";

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

    @Column(name = "date_last_active", insertable = false, nullable = false)
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

    public static String getIsUsersMissingQuery(List<User> users) {

        StringBuilder query = new StringBuilder();
        for (User user : users) {
            query.append("SELECT EXISTS(SELECT 1 FROM Users WHERE id = ").append(user.getId()).append(")");
            query.append(" INTERSECT ");
        }
        query.delete(query.lastIndexOf(" INTERSECT "), query.length());

        return query.toString();
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
