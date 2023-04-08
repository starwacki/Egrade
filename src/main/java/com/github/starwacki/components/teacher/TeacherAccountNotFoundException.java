package com.github.starwacki.components.teacher;

public class TeacherAccountNotFoundException extends RuntimeException{

    private static final String TEACHER_NOT_FOUND_EXCEPTION_MESSAGE = "Teacher not found id: ";

    public TeacherAccountNotFoundException(int id) {
        super(TEACHER_NOT_FOUND_EXCEPTION_MESSAGE + id);
    }

}
