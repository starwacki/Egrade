package com.github.starwacki.student.model;
import com.github.starwacki.account.model.Student;
import com.github.starwacki.account.model.Teacher;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "classes")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String name;

    @NonNull
    private int classYear;


    @OneToMany(mappedBy = "schoolClass")
    private Set<Student> students;

    @ManyToMany(mappedBy = "classes")
    private Set<Teacher> teachers;

}
