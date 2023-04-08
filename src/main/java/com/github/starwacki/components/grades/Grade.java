package com.github.starwacki.components.grades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "grades")
class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int studentID;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private GradeSubject gradeSubject;

    private int weight;

    private GradeSymbolValue gradeSymbolValue;

    private boolean haveValue;

    private LocalDate addedDate;

    private String addedBy;


}
