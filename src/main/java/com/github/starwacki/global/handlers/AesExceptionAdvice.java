package com.github.starwacki.global.handlers;

import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.student.exceptions.StudentNotFoundException;
import com.github.starwacki.components.student.exceptions.SubjectNotFoundException;
import com.github.starwacki.components.student.exceptions.TeacherAccountNotFoundException;
import com.github.starwacki.components.teacher.exceptions.SchoolClassNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
import com.github.starwacki.global.security.AES;
import com.github.starwacki.global.security.AESException;
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
public class AesExceptionAdvice extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AES.class);

    @ExceptionHandler(value = AESException.class)
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest) {

        String response = e.getMessage();
        logger.info(response);

        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, webRequest);
    }
}
