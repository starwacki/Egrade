package com.github.starwacki.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student extends Account {


    @ManyToOne
    @JoinColumn(name = "classes_id")
    private SchoolClass schoolClass;

    @OneToOne
    private Parent parent;




}
