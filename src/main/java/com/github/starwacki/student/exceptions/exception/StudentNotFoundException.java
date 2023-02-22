package com.github.starwacki.student.exceptions.exception;

public class StudentNotFoundException extends RuntimeException{

    private final static String STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";
    public StudentNotFoundException(int id) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }
}
