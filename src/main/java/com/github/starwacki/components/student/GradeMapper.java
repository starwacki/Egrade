package com.github.starwacki.components.student;

import com.github.starwacki.components.student.dto.GradeDTO;
import com.github.starwacki.common.model.grades.Grade;

public class GradeMapper {

    private GradeMapper() {
    }


    public static GradeDTO mapGradeToGradeDTO(Grade grade) {
        return GradeDTO
                .builder()
                .degree(grade.getDegree().getSymbol())
                .subject(grade.getSubject())
                .weight(grade.getWeight())
                .description(grade.getDescription())
                .build();
    }

//    public static Grade mapGradeDTOToGrade(GradeDTO gradeDTO, Degree degree, Student accountStudent, String addedBy) {
//        return Grade
//                .builder()
//                .description(gradeDTO.description())
//                .degree(degree)
//                .subject(gradeDTO.subject())
//                .weight(gradeDTO.weight())
//                .addedBy(addedBy)
//                .addedDate(LocalDate.now())
//                .studentUsername(accountStudent)
//                .build();
//    }

//    public static GradeViewDTO mapGradeToGradeViewDTO(Grade grade) {
//        return GradeViewDTO
//                .builder()
//                .description(grade.getDescription())
//                .weight(grade.getWeight())
//                .addedBy(grade.getAddedBy().getFirstname() + " " + grade.getAddedBy().getLastname())
//                .addedDate(grade.getAddedDate())
//                .degree(grade.getDegree().getSymbol())
//                .build();
//    }


}
