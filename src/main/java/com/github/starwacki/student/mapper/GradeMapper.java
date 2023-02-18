package com.github.starwacki.student.mapper;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.account.model.Teacher;
import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.dto.GradeViewDTO;
import com.github.starwacki.student.model.Grade;
import java.time.LocalDate;

public class GradeMapper {

    private GradeMapper() {
    }


    public static GradeDTO mapGradeToGradeDTO(Grade grade) {
        return GradeDTO
                .builder()
                .degree(grade.getDegree())
                .subject(grade.getSubject())
                .weight(grade.getWeight())
                .description(grade.getDescription())
                .build();
    }

    public static Grade mapGradeDTOToGrade(GradeDTO gradeDTO, Student student, Teacher teacher) {
        return Grade
                .builder()
                .description(gradeDTO.description())
                .degree(gradeDTO.degree())
                .subject(gradeDTO.subject())
                .weight(gradeDTO.weight())
                .addedBy(teacher)
                .addedDate(LocalDate.now())
                .student(student)
                .build();
    }

    public static GradeViewDTO mapGradeToGradeViewDTO(Grade grade) {
        return GradeViewDTO
                .builder()
                .description(grade.getDescription())
                .weight(grade.getWeight())
                .addedBy(grade.getAddedBy().getFirstname() + " " + grade.getAddedBy().getLastname())
                .addedDate(grade.getAddedDate())
                .degree(grade.getDegree())
                .build();
    }


}
