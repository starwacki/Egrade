package com.github.starwacki.components.student.mapper;

import com.github.starwacki.components.account.AccountStudent;
import com.github.starwacki.components.student.dto.StudentDTO;
import com.github.starwacki.components.student.dto.StudentGradesDTO;
import com.github.starwacki.components.student.dto.SubjectDTO;
import java.util.List;

public class StudentMapper {

    private StudentMapper() {
    }


    //TODO: GET PARENT PHONE NUMBER!
    public static StudentDTO mapStudentToStudentDTO(AccountStudent accountStudent) {
        return StudentDTO.builder()
                .id(accountStudent.getId())
                .firstname(accountStudent.getFirstname())
                .lastname(accountStudent.getLastname())
                .year(accountStudent.getSchoolClass().getClassYear())
                .className(accountStudent.getSchoolClass().getName())
                .parentPhone("")
                .build();
    }

    public static StudentGradesDTO mapStudentToStudentGradeDTO(AccountStudent accountStudent, List<SubjectDTO> grades) {
        return StudentGradesDTO.builder()
                .firstname(accountStudent.getFirstname())
                .lastname(accountStudent.getLastname())
                .className(accountStudent.getSchoolClass().getName())
                .year(accountStudent.getSchoolClass().getClassYear())
                .subjectGrades(grades)
                .build();
    }
}
