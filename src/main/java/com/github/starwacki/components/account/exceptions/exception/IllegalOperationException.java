package com.github.starwacki.components.account.exceptions.exception;


import com.github.starwacki.components.account.model.Role;
import org.springframework.http.HttpMethod;

public class IllegalOperationException extends RuntimeException{

    private static final String ILLEGAL_OPERATION_EXCEPTION_MESSAGE = "Illegal operation type: ";

    public IllegalOperationException(HttpMethod httpMethod, Role role) {
        super(ILLEGAL_OPERATION_EXCEPTION_MESSAGE + httpMethod.name() + " for role: " + role);
    }

    public IllegalOperationException() {
        super();
    }
}
