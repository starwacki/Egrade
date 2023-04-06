package com.github.starwacki.components.account.exceptions;


import org.springframework.http.HttpMethod;

public class IllegalOperationException extends RuntimeException{

    private static final String ILLEGAL_OPERATION_EXCEPTION_MESSAGE = "Illegal operation type: ";

    public IllegalOperationException(HttpMethod httpMethod,String role) {
        super(ILLEGAL_OPERATION_EXCEPTION_MESSAGE + httpMethod.name() + " for role: " + role);
    }

}
