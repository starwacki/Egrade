package com.github.starwacki.model.account;


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

    public abstract static class Builder<T extends Account> {

        protected String username;

        protected String password;

        protected String firstname;

        protected String lastname;

        protected Role role;

        public Builder<T> username(String username) {
            this.username = username;
            return this;
        }

        public Builder<T> password(String password) {
            this.password = password;
            return this;
        }

        public Builder<T> firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder<T> lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder<T> role(Role role) {
            this.role = role;
            return this;
        }

        public abstract  T build();
    }





}
