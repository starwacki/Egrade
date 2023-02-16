package com.github.starwacki.student.mapper;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.student.dto.GradeDTO;
import com.github.starwacki.student.model.Grade;
import com.github.starwacki.student.model.Subject;

import java.time.LocalDate;

public class GradeMapper {


    public static GradeDTO mapGradeToGradeViewDTO(Grade grade) {
        return GradeDTO
                .builder()
                .degree(grade.getDegree())
                .weight(grade.getWeight())
                .description(grade.getDescription())
                .build();
    }

    public static Grade mapGradeDTOToGrade(GradeDTO gradeDTO, Student student) {
        return Grade.builder()
                .description(gradeDTO.description())
                .degree(gradeDTO.degree())
                .subject(gradeDTO.subject())
                .weight(gradeDTO.weight())
                .addedDate(LocalDate.now())
                .student(student)
                .build();
    }



}
