package com.github.starwacki.components.account;

interface AccountBuilder <T extends Account.Builder>  {

    T firstname(String firstname);
    T lastname(String lastname);
    T accountDetails(AccountDetails accountDetails);

}
