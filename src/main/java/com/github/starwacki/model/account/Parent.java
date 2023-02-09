package com.github.starwacki.model.account;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@Entity
@Table(name = "parents")
public class Parent extends Account {

    @NonNull
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



    public static class  Builder extends Account.Builder<Parent> {
        private String phoneNumber;
        private Student student;

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
