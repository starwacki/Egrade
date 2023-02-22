package com.github.starwacki.student.exceptions.exception;

public class TeacherNotFoundException extends RuntimeException{

    private static final String TEACHER_NOT_FOUND_EXCEPTION_MESSAGE = "Teacher not found id: ";

    public TeacherNotFoundException(int id) {
        super(TEACHER_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }

}
