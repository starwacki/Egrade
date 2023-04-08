package com.github.starwacki.components.student;


import com.github.starwacki.components.student.dto.StudentDTO;

class StudentMapper {

    private StudentMapper() {
    }

    static StudentDTO mapStudentToStudentDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .year(student.getSchoolClassYear())
                .className(student.getSchoolClassName())
                .parentPhone(student.getParentPhoneNumber())
                .build();
    }


}
