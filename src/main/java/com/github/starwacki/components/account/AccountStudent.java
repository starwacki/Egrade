package com.github.starwacki.components.account;


import com.github.starwacki.common.model.school_class.SchoolClass;
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

    private Student(AccountDetails accountDetails, String firstname, String lastname,  SchoolClass schoolClass, Parent parent) {
        super(accountDetails, firstname, lastname);
        this.schoolClass = schoolClass;
        this.parent = parent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends  Account.Builder {

        private SchoolClass schoolClass;

        private Parent parent;

        @Override
        public Builder accountDetails(AccountDetails accountDetails) {
            this.accountDetails = accountDetails;
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
            return new Student(accountDetails,firstname,lastname,schoolClass,parent);
        }

    }

}
