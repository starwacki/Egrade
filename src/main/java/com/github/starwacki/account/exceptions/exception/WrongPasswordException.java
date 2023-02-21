package com.github.starwacki.account.exceptions.exception;

public class WrongPasswordException extends RuntimeException{

    private static final String WRONG_PASSWORD_EXCEPTION_MESSAGE = "Password not same";
    public WrongPasswordException() {
        super(WRONG_PASSWORD_EXCEPTION_MESSAGE);
    }
}
