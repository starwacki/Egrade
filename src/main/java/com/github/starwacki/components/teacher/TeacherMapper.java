package com.github.starwacki.components.teacher.mapper;

import com.github.starwacki.components.teacher.TeacherDTO;
import com.github.starwacki.components.account.AccountTeacher;

public class TeacherMapper {

    private TeacherMapper() {

    }

    public static TeacherDTO mapTeacherToTeacherDTO(AccountTeacher accountTeacher) {
        return TeacherDTO
                .builder()
                .firstname(accountTeacher.getFirstname())
                .lastname(accountTeacher.getLastname())
                .phone(accountTeacher.getWorkPhone())
                .subject(accountTeacher.getSubject().toString())
                .email(accountTeacher.getEmail())
                .build();
    }
}
