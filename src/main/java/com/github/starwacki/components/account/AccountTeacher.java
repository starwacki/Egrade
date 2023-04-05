package com.github.starwacki.components.account;


import com.github.starwacki.common.model.grades.Subject;
import com.github.starwacki.common.model.school_class.SchoolClass;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Entity
@Table(name = "teachers")
public class Teacher extends Account {

    private String workPhone;

    private String email;

    @Enumerated(value = EnumType.STRING)
    private Subject subject;

    protected Teacher() {

    }

    private Teacher(AccountDetails accountDetails, String firstname, String lastname, String workPhone, String email,Subject subject) {
        super(accountDetails,firstname, lastname);
        this.workPhone = workPhone;
        this.email = email;
        this.subject = subject;
    }

    @ManyToMany
    @JoinTable(
            name = "teachers_classes",
            joinColumns = @JoinColumn(name = "teachers_id"),
            inverseJoinColumns = @JoinColumn(name = "classes_id")
    )
    private Set<SchoolClass> classes;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Account.Builder {

        private String workPhone;

        private String email;

        private Subject subject;


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

        public Builder workPhone(String workPhone) {
            this.workPhone = workPhone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder subject(Subject subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public Teacher build() {
            return new Teacher(accountDetails,firstname,lastname,workPhone,email,subject);
        }

    }

}
