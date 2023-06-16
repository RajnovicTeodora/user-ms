package com.notbooking.userms.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.notbooking.userms.dto.NewUserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.InheritanceType.JOINED;

@Where(clause = "isDeleted=false")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = JOINED)
@Table(name = "system_user")
public abstract class User implements UserDetails{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", unique = false, nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "isDeleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "notificationsActive", nullable = false)
    private boolean notificationsActive;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;

    public User(NewUserDTO newUserDTO){
        this.name = newUserDTO.getName();
        this.surname = newUserDTO.getSurname();
        this.username = newUserDTO.getUsername();
        this.email = newUserDTO.getEmail();
        this.isDeleted = false;
        this.notificationsActive = true;
        this.address = new Address(newUserDTO.getCountry(), newUserDTO.getCity(), newUserDTO.getStreet(), newUserDTO.getStreetNum());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<Authority> authorities = new ArrayList<Authority>();
        authorities.add(new Authority("ROLE_USER"));

        if (!this.isDeleted) {
            authorities.add(new Authority("ROLE_" + this.role.getName()));
        }
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
