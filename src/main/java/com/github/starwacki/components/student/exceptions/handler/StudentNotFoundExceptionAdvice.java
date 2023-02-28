package com.github.starwacki.components.student.exceptions.handler;

import com.github.starwacki.components.student.exceptions.exception.StudentNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class StudentNotFoundExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(StudentNotFoundExceptionAdvice.class);

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest) {

        String response = e.getMessage();
        logger.info(response);

        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, webRequest);
    }
}
