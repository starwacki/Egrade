package com.github.starwacki.components.auth.exceptions;

public class WrongAuthenticationException extends RuntimeException{

    private static final String WRONG_AUTHENTICATION_EXCEPTION_MESSAGE = "Wrong username or password";

    public WrongAuthenticationException() {
        super(WRONG_AUTHENTICATION_EXCEPTION_MESSAGE);
    }
}
