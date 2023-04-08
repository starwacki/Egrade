package com.github.starwacki.components.grades.exceptions;

public class SubjectNotFoundException extends RuntimeException{

    private static final String SUBJECT_NOT_FOUND_EXCEPTION_MESSAGE = "Subject not found, id: ";

    public SubjectNotFoundException(int subjectId) {
        super(SUBJECT_NOT_FOUND_EXCEPTION_MESSAGE + subjectId);
    }
}
