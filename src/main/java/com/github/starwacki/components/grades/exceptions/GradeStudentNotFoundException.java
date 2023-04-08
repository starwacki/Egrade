package com.github.starwacki.components.grades.exceptions;

public class GradeStudentNotFoundException extends RuntimeException{

    private static final String GRADE_STUDENT_NOT_FOUND_EXCEPTION_MESSAGE = "Student not found id: ";
    public GradeStudentNotFoundException(int studentId) {
        super(GRADE_STUDENT_NOT_FOUND_EXCEPTION_MESSAGE + studentId);
    }

}
