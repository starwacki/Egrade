package com.github.starwacki.global.model.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;


@Data
@Entity
@Table(name = "parents")
public class Parent extends Account {

    private String phoneNumber;

    private Parent(String username, String password, String firstname, String lastname, Role role,String phoneNumber) {
        super(username, password, firstname, lastname, role);
        this.phoneNumber = phoneNumber;
    }

    protected Parent() {
        super();
    }

    public static Builder  builder() {
        return new Builder();
    }


    public static class  Builder extends Account.Builder {
        private String phoneNumber;

        @Override
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        @Override
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Builder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        @Override
        public Builder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        @Override
        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Override
        public Parent build() {
            return new Parent(username,password,firstname,lastname,role,phoneNumber);
        }
    }

}
