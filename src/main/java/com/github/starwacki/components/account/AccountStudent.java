package com.github.starwacki.components.account;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "students")
class AccountStudent extends Account {

    private String schoolClassName;
    private int schoolClassYear;

    @OneToOne
            (cascade ={CascadeType.ALL})
    private AccountParent accountParent;

    @Override
    public String toString(){
        return super.toString();
    }

    protected AccountStudent() {
    }

    private AccountStudent(AccountDetails accountDetails, String firstname, String lastname, String schoolClassName, int schoolClassYear, AccountParent accountParent) {
        super(accountDetails, firstname, lastname);
        this.accountParent = accountParent;
        this.schoolClassYear = schoolClassYear;
        this.schoolClassName = schoolClassName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends  Account.Builder {

        private String schoolClassName;
        private int schoolClassYear;
        private AccountParent accountParent;

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

        public Builder schoolClassName(String schoolClassName) {
            this.schoolClassName = schoolClassName;
            return this;
        }

        public Builder schoolClassYear(int schoolClassYear) {
            this.schoolClassYear = schoolClassYear;
            return this;
        }

        public Builder parent(AccountParent accountParent) {
            this.accountParent = accountParent;
            return this;
        }

        @Override
        public AccountStudent build() {
            return new AccountStudent(accountDetails,firstname,lastname,schoolClassName,schoolClassYear, accountParent);
        }

    }

}
