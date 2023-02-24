package com.github.starwacki.components.student.mapper;

import com.github.starwacki.components.account.model.Student;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import com.github.starwacki.components.student.dto.SubjectDTO;

import java.util.List;

public class StudentMapper {

    private StudentMapper() {
    }

    public static StudentDTO mapStudentToStudentDTO(Student student) {
        return StudentDTO.builder()
                .id(student.getId())
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .year(student.getSchoolClass().getClassYear())
                .className(student.getSchoolClass().getName())
                .parentPhone(student.getParent().getPhoneNumber())
                .build();
    }

    public static StudentGradesDTO mapStudentToStudentGradeDTO(Student student, List<SubjectDTO> grades) {
        return StudentGradesDTO.builder()
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .className(student.getSchoolClass().getName())
                .year(student.getSchoolClass().getClassYear())
                .subjectGrades(grades)
                .build();
    }
}
