package com.github.starwacki.components.account.exceptions;

public class AccountNotFoundException extends RuntimeException{

    private static final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "Account not found id: ";

    public AccountNotFoundException() {
        super();
    }

    public AccountNotFoundException(int id) {
        super(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }

}
