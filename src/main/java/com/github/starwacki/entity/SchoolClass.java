package com.github.starwacki.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "classes")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    private String name;

    @OneToMany(mappedBy = "schoolClass")
    private Set<Student> students;

    @ManyToMany(mappedBy = "classes")
    private Set<Teacher> teachers;







}
