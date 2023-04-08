package com.github.starwacki.components.grades;

import com.github.starwacki.components.grades.dto.GradeRequestDTO;
import com.github.starwacki.components.grades.dto.GradeResponeDTO;

import java.time.LocalDate;

class GradeMapper {

    private GradeMapper() {
    }


    public static GradeRequestDTO mapGradeToGradeDTO(Grade grade) {
        return GradeRequestDTO
                .builder()
                .degree(grade.getGradeSymbolValue().getSymbol())
                .subject(grade.getGradeSubject().toString())
                .weight(grade.getWeight())
                .addedBy(grade.getAddedBy())
                .description(grade.getDescription())
                .build();
    }

    public static Grade mapGradeDTOToGrade(GradeRequestDTO gradeRequestDTO, GradeSymbolValue gradeSymbolValue) {
        return Grade
                .builder()
                .description(gradeRequestDTO.description())
                .gradeSymbolValue(gradeSymbolValue)
                .gradeSubject(GradeSubject.valueOf(gradeRequestDTO.subject()))
                .weight(gradeRequestDTO.weight())
                .addedBy(gradeRequestDTO.addedBy())
                .addedDate(LocalDate.now())
                .studentID(gradeRequestDTO.studentID())
                .build();
    }


    public static GradeResponeDTO mapGradeToGradeViewDTO(Grade grade) {
        return GradeResponeDTO
                .builder()
                .description(grade.getDescription())
                .weight(grade.getWeight())
                .addedBy(grade.getAddedBy())
                .addedDate(grade.getAddedDate())
                .degree(grade.getGradeSymbolValue().getSymbol())
                .build();
    }


}
