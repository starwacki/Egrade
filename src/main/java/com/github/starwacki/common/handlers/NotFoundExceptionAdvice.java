package com.github.starwacki.common.handlers;

import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.grades.exceptions.StudentNotFoundException;
import com.github.starwacki.components.grades.exceptions.SubjectNotFoundException;
import com.github.starwacki.components.student.exceptions.TeacherAccountNotFoundException;
import com.github.starwacki.components.teacher.exceptions.SchoolClassNotFoundException;
import com.github.starwacki.components.teacher.exceptions.TeacherNotFoundException;
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
public class NotFoundExceptionAdvice extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(NotFoundExceptionAdvice.class);

    @ExceptionHandler(value = {
            SchoolClassNotFoundException.class, TeacherNotFoundException.class, StudentNotFoundException.class,
            SubjectNotFoundException.class, TeacherAccountNotFoundException.class, AccountNotFoundException.class})
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest) {

        String response = e.getMessage();
        logger.info(response);

        return handleExceptionInternal(e, response, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, webRequest);
    }
}
