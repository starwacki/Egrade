package com.github.starwacki.components.teacher.exceptions;

import org.hibernate.validator.constraints.Range;

public class SchoolClassNotFoundException extends RuntimeException {

    private static final String SCHOOL_CLASS_NOT_FOUND_EXCEPTION_MESSAGE = "Not found class with declared name and year: ";

    public SchoolClassNotFoundException (String className, @Range(min = 2020, max = 2040) int classYear) {
        super(SCHOOL_CLASS_NOT_FOUND_EXCEPTION_MESSAGE + className + " " + classYear);
    }

}
