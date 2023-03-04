package com.github.starwacki.components.teacher.mapper;

import com.github.starwacki.components.teacher.dto.TeacherDTO;
import com.github.starwacki.global.model.account.Teacher;

public class TeacherMapper {

    private TeacherMapper() {

    }

    public static TeacherDTO mapTeacherToTeacherDTO(Teacher teacher) {
        return TeacherDTO
                .builder()
                .firstname(teacher.getFirstname())
                .lastname(teacher.getLastname())
                .phone(teacher.getWorkPhone())
                .subject(teacher.getSubject().toString())
                .email(teacher.getEmail())
                .build();
    }
}
