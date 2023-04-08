package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeDTO;
import com.github.starwacki.components.grades.dto.GradeViewDTO;

import java.time.LocalDate;

class GradeMapper {

    private GradeMapper() {
    }


    public static GradeDTO mapGradeToGradeDTO(Grade grade) {
        return GradeDTO
                .builder()
                .degree(grade.getGradeSymbolValue().getSymbol())
                .subject(grade.getGradeSubject().toString())
                .weight(grade.getWeight())
                .addedBy(grade.getAddedBy())
                .description(grade.getDescription())
                .build();
    }

    public static Grade mapGradeDTOToGrade(GradeDTO gradeDTO, GradeSymbolValue gradeSymbolValue) {
        return Grade
                .builder()
                .description(gradeDTO.description())
                .gradeSymbolValue(gradeSymbolValue)
                .gradeSubject(GradeSubject.valueOf(gradeDTO.subject()))
                .weight(gradeDTO.weight())
                .addedBy(gradeDTO.addedBy())
                .addedDate(LocalDate.now())
                .studentID(gradeDTO.studentID())
                .build();
    }


    public static GradeViewDTO mapGradeToGradeViewDTO(Grade grade) {
        return GradeViewDTO
                .builder()
                .description(grade.getDescription())
                .weight(grade.getWeight())
                .addedBy(grade.getAddedBy())
                .addedDate(grade.getAddedDate())
                .degree(grade.getGradeSymbolValue().getSymbol())
                .build();
    }


}
