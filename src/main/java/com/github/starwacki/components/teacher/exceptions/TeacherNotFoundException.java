package com.github.starwacki.components.teacher.exceptions;

public class TeacherNotFoundException extends RuntimeException{

    private static final String TEACHER_NOT_FOUND_EXCEPTION_MESSAGE = "Not found teacher with id: ";

    public TeacherNotFoundException(int id) {
        super(TEACHER_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }
}
