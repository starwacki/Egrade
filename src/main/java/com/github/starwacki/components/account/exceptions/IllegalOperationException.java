package com.github.starwacki.components.account.exceptions;


import com.github.starwacki.common.model.account.Role;
import org.springframework.http.HttpMethod;

public class IllegalOperationException extends RuntimeException{

    private static final String ILLEGAL_OPERATION_EXCEPTION_MESSAGE = "Illegal operation type: ";

    public IllegalOperationException(HttpMethod httpMethod, Role role) {
        super(ILLEGAL_OPERATION_EXCEPTION_MESSAGE + httpMethod.name() + " for role: " + role);
    }

}
