package com.github.starwacki.components.student.exceptions.exception;

public class StudentNotFoundException extends RuntimeException{

    private final static String STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";
    private final static String GRADE_NOT_FOUND = " with grade id: ";
    public StudentNotFoundException(int studentId) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId);
    }

    public StudentNotFoundException(int studentId, int gradeID) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId + GRADE_NOT_FOUND + gradeID );
    }
}
