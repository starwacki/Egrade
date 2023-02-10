package com.github.starwacki.model.account;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "parents")
public class Parent extends Account {

    private String phoneNumber;

    @OneToOne
    private Student student;


    private Parent(String username, String password, String firstname, String lastname, Role role,String phoneNumber, Student student) {
        super(username, password, firstname, lastname, role);
        this.phoneNumber = phoneNumber;
        this.student = student;
    }

    protected Parent() {
        super();
    }

    public static Builder  builder() {
        return new Builder();
    }



    public static class  Builder extends Account.Builder {
        private String phoneNumber;
        private Student student;


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

        public Builder student(Student student) {
            this.student = student;
            return this;
        }

        @Override
        public Parent build() {
            return new Parent(username,password,firstname,lastname,role,phoneNumber,student);
        }
    }

}
