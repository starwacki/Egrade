package com.github.starwacki.components.account.model;


import jakarta.persistence.*;
import lombok.*;

@Data
@MappedSuperclass
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;

    private String password;

    private String firstname;

    private String lastname;

    @Enumerated(EnumType.STRING)
    private Role role;

    protected Account() {
    }

    protected Account(String username, String password, String firstname, String lastname, Role role) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public abstract static class Builder implements AccountBuilder{

        protected String username;

        protected String password;

        protected String firstname;

        protected String lastname;

        protected Role role;

        public abstract Account build();

    }





}
