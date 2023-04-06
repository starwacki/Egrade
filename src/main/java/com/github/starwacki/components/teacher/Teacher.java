package com.github.starwacki.components.teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
class Teacher {

    @Id
    private int id;
}
