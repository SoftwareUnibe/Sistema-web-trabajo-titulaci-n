package com.unibe.titulation.security.entity;

import com.unibe.titulation.entities.Career;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    @NotNull @NotBlank
    private String name, lastName;
    @NotNull @NotBlank
    private String secondName, secondLastName;
    @NotNull
    private boolean verified;
    @NotNull @NotBlank
    @Column(unique = true)
    private String userName;
    @NotNull @NotBlank
    @Column(unique = true)
    private String email, ci;
    @NotNull @NotBlank
    private String password;
    private String tokenPassword;
    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_rol", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private Career career;
    private String degree;

    public User() {
    }

    public User(@NotNull String name, @NotNull String userName, @NotNull String email, @NotNull String lastName,
                @NotNull String secondName, @NotNull String secondLastName, @NotNull String ci, @NotNull String password,
                @NotNull boolean verified, String degree) {
        this.name = name;
        this.lastName = lastName;
        this.secondName = secondName;
        this.secondLastName = secondLastName;
        this.ci = ci;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.degree = degree;
        this.verified = verified;

    }

    public User(@NotNull String name, @NotNull String userName, @NotNull String email, @NotNull String lastName,
                @NotNull String secondName, @NotNull String secondLastName, @NotNull String ci, @NotNull String password,
                @NotNull boolean verified, Career career, String degree) {
        this.name = name;
        this.lastName = lastName;
        this.secondName = secondName;
        this.secondLastName = secondLastName;
        this.ci = ci;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.verified = verified;
        this.career = career;
        this.degree = degree;
    }

    
    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Career getCareer() {
        return career;
    }

    public void setCareer(Career career) {
        this.career = career;
    }

    public String getName() {
        return name;
    }

    public void setName(String nombre) {
        this.name = nombre;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String nombreUsuario) {
        this.userName = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTokenPassword() {
        return tokenPassword;
    }

    public void setTokenPassword(String tokenPassword) {
        this.tokenPassword = tokenPassword;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}