package com.github.starwacki.student.mapper;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.StudentDTO;
import com.github.starwacki.student.dto.StudentGradesDTO;
import com.github.starwacki.student.model.Subject;

import java.util.List;
import java.util.Map;

public class StudentMapper {

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

    public static StudentGradesDTO mapStudentToStudentGradeDTO(Student student, Map<Subject, List<GradeDTO>> grades) {
        return StudentGradesDTO.builder()
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .className(student.getSchoolClass().getName())
                .year(student.getSchoolClass().getClassYear())
                .grades(grades)
                .build();
    }
}
