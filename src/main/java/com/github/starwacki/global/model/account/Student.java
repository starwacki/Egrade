package com.github.starwacki.global.model.account;


import com.github.starwacki.global.model.school_class.SchoolClass;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "students")
public class Student extends Account {

    @ManyToOne
            (cascade = {CascadeType.PERSIST})
    @JoinColumn
            (name = "classes_id")
    private SchoolClass schoolClass;

    @OneToOne
            (cascade ={CascadeType.ALL})
    private Parent parent;

    @Override
    public String toString(){
        return super.toString();
    }

    protected Student() {
    }

    private Student(String username, String password, String firstname, String lastname, Role role,  SchoolClass schoolClass, Parent parent) {
        super(username, password, firstname, lastname, role);
        this.schoolClass = schoolClass;
        this.parent = parent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends  Account.Builder {

        private int diaryNumber;

        private SchoolClass schoolClass;

        private Parent parent;

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

        public Builder schoolClass(SchoolClass schoolClass) {
            this.schoolClass= schoolClass;
            return this;
        }

        public Builder parent(Parent parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public Student build() {
            return new Student(username,password,firstname,lastname,role,schoolClass,parent);
        }

    }

}
