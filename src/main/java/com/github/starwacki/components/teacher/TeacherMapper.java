package com.github.starwacki.components.teacher;

import com.github.starwacki.components.teacher.dto.TeacherResponseDTO;
import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;

class TeacherMapper {

    private TeacherMapper() {

    }

    public static TeacherResponseDTO mapTeacherToTeacherResponseDTO(Teacher accountTeacher) {
        return TeacherResponseDTO
                .builder()
                .firstname(accountTeacher.getFirstname())
                .lastname(accountTeacher.getLastname())
                .phone(accountTeacher.getWorkPhone())
                .subject(accountTeacher.getSubject())
                .email(accountTeacher.getEmail())
                .build();
    }

    public static TeacherSchoolClassDTO mapTeacherSchoolClassToTeacherSchoolClassDTO(TeacherSchoolClass teacherSchoolClass) {
        return TeacherSchoolClassDTO
                .builder()
                .className(teacherSchoolClass.getClassName())
                .year(teacherSchoolClass.getClassYear())
                .build();
    }

    public static TeacherSchoolClass  mapTeacherSchoolClassDTOToTeacherSchoolClass(TeacherSchoolClassDTO teacherSchoolClassDTO) {
        return TeacherSchoolClass
                .builder()
                .className(teacherSchoolClassDTO.className())
                .classYear(teacherSchoolClassDTO.year())
                .build();
    }
}
