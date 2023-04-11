package com.github.starwacki.components.teacher.exceptions;

import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;

public class TeacherSchoolClassException extends RuntimeException {

    private static final String TEACHER_SCHOOL_CLASS_EXCEPTION_MESSAGE
            = "This teacher already has this class assigned!, school class: ";
    public TeacherSchoolClassException(TeacherSchoolClassDTO teacherSchoolClassDTO) {
        super(TEACHER_SCHOOL_CLASS_EXCEPTION_MESSAGE +teacherSchoolClassDTO.className() + " " + teacherSchoolClassDTO.year());
    }
}
