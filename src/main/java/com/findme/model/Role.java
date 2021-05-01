package com.findme.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "ROLE", schema = "public")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_role", unique = true, nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public String getAuthority() {
        return userRole.toString();
    }

}
