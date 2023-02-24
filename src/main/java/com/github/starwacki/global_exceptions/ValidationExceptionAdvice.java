package com.github.starwacki.global_exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ValidationExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String,String>> handleInvalidArgument(ConstraintViolationException exception) {
        Map<String,String> error = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            error.put(getFieldName(violation),violation.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        return violation.getPropertyPath().toString().split("\\.")[1];
    }


}
