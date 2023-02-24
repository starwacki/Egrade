package com.github.starwacki.components.student.model;

import com.github.starwacki.components.account.model.Student;
import com.github.starwacki.components.account.model.Teacher;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    private Subject subject;

    private int weight;

    private double degree;

    private LocalDate addedDate;

    @OneToOne
    private Student student;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher addedBy;

}
