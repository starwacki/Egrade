package com.github.starwacki.components.grades.exceptions;

public class StudentNotFoundException extends RuntimeException{

    private static final String STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";
    private static final String GRADE_NOT_FOUND = " with grade id: ";
    public StudentNotFoundException(int studentId) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId);
    }

    public StudentNotFoundException(int studentId, int gradeID) {
        super(STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId + GRADE_NOT_FOUND + gradeID );
    }
}
