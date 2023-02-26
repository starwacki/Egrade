package com.github.starwacki.global_exceptions;

import com.github.starwacki.components.student.exceptions.handler.SubjectNotFoundExceptionAdvice;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ValidationViolationExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ValidationViolationExceptionAdvice.class);

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Map<String,String>> handleInvalidArgument(ConstraintViolationException exception) {

        Map<String, String> error = exception.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        violation -> getFieldName(violation),
                        violation -> "Invalid value: " + violation.getInvalidValue()
                ));
        logger.info(exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String getFieldName(ConstraintViolation<?> violation) {
        return violation.getPropertyPath().toString().split("\\.")[1];
    }


}
