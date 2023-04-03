package com.github.starwacki.common.handlers;

import com.github.starwacki.components.auth.exceptions.WrongAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class WrongAuthenticationExceptionAdvice extends ResponseEntityExceptionHandler {


    private static final Logger logger = LoggerFactory.getLogger(WrongAuthenticationExceptionAdvice.class);

    @ExceptionHandler({WrongAuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(WrongAuthenticationException exception) {

        logger.info(exception.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }
}
