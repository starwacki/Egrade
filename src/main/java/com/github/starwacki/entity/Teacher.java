package com.github.starwacki.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher extends Account {

    @NonNull
    private String workPhone;

    @NonNull
    private String email;


    @ManyToMany
    @JoinTable(
            name = "teachers_classes",
            joinColumns = @JoinColumn(name = "teachers_id"),
            inverseJoinColumns = @JoinColumn(name = "classes_id")
    )
    private Set<SchoolClass> classes;



}
