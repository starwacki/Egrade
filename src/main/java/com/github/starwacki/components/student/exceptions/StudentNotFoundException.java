package com.github.starwacki.components.student.exceptions;

public class StudentNotFoundException extends RuntimeException{

    private static final String STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";

    public StudentNotFoundException(int studentId) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId);
    }
}
