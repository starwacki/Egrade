package com.github.starwacki.student.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class StudentNotFoundException extends RuntimeException{

    private final static String STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";
    public StudentNotFoundException(int id) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }
}
