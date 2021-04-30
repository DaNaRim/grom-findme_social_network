package com.findme.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Users", schema = "public")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //required fields

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false, length = 30)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", unique = true, nullable = false, length = 15)
    private String phone;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    //optional fields

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "country", length = 30)
    private String country; //TODO from existed date

    @Column(name = "city", length = 30)
    private String city;

    @Column(name = "school", length = 30)
    private String school;

    @Column(name = "university", length = 30)
    private String university;

    //system fields

    @Column(name = "date_registered", insertable = false, updatable = false)
    private Date dateRegistered;

    @Column(name = "date_last_active", insertable = false, nullable = false)
    private Date dateLastActive;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE",
            joinColumns = {@JoinColumn(name = "USER_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", nullable = false, updatable = false)})
    private Set<Role> roles;


    public User() {
    }

    public User(Long id) {
        this.id = id;
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Date getDateOfBirth() {
        return dateOfBirth == null ? null : new Date(dateOfBirth.getTime());
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setDateLastActive(Date dateLastActive) {
        this.dateLastActive = dateLastActive == null ? null : new Date(dateLastActive.getTime());
    }
}
