package com.github.starwacki.components.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "parents")
class AccountParent extends Account {

    private String phoneNumber;

    private AccountParent(AccountDetails accountDetails, String firstname, String lastname, String phoneNumber) {
        super(accountDetails, firstname, lastname);
        this.phoneNumber = phoneNumber;
    }

    protected AccountParent() {
        super();
    }

    public static Builder  builder() {
        return new Builder();
    }


    public static class  Builder extends Account.Builder {
        private String phoneNumber;

        @Override
        public Builder accountDetails(AccountDetails accountDetails) {
            this.accountDetails =accountDetails;
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

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        @Override
        public AccountParent build() {
            return new AccountParent(accountDetails,firstname,lastname,phoneNumber);
        }
    }

}
