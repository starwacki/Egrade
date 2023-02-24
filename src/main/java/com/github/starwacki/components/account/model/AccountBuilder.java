package com.github.starwacki.components.account.model;

interface AccountBuilder <T extends Account.Builder>  {

    T username(String username);
    T password(String password);
    T firstname(String firstname);
    T lastname(String lastname);
    T role(Role role);

}
