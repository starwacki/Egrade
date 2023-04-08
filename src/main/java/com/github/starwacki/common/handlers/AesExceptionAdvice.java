package com.github.starwacki.common.handlers;

import com.github.starwacki.components.auth.exceptions.AESException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AesExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = AESException.class)
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest) {

        String response = e.getMessage();
        logger.info(response);

        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }
}
