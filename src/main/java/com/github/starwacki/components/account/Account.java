package com.github.starwacki.components.account;


import jakarta.persistence.*;
import lombok.*;

@Data
@MappedSuperclass
abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstname;

    private String lastname;

    @OneToOne
    private AccountDetails accountDetails;

    protected Account() {
    }

    protected Account(AccountDetails accountDetails, String firstname, String lastname) {
        this.accountDetails = accountDetails;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public abstract static class Builder implements AccountBuilder{
        protected AccountDetails accountDetails;

        protected String firstname;

        protected String lastname;

        public abstract Account build();

    }





}