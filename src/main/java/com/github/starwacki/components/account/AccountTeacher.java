package com.github.starwacki.components.account;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "teachers")
class AccountTeacher extends Account {

    private String workPhone;
    private String email;
    private String subject;

    protected AccountTeacher() {

    }

    private AccountTeacher(AccountDetails accountDetails, String firstname, String lastname, String workPhone, String email, String subject) {
        super(accountDetails,firstname, lastname);
        this.workPhone = workPhone;
        this.email = email;
        this.subject = subject;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Account.Builder {

        private String workPhone;

        private String email;

        private String subject;


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

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        @Override
        public AccountTeacher build() {
            return new AccountTeacher(accountDetails,firstname,lastname,workPhone,email,subject);
        }

    }

}
