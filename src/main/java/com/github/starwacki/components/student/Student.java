package com.github.starwacki.components.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "students")
class Student {

    @Id
    private int id;
    private String firstname;
    private String lastname;
    private String schoolClassName;
    private int schoolClassYear;
}
