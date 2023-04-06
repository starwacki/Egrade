package com.github.starwacki.common.model.grades;

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
public class Grade {

    @Id
    private String studentUsername;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Subject subject;

    private int weight;

    private Degree degree;

    private boolean haveValue;

    private LocalDate addedDate;

    private String addedBy;


}
