package com.github.starwacki.components.teacher;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "teachers")
class Teacher {

    @Id
    private int id;

    private String firstname;

    private String lastname;

    private String email;

    private String subject;

    private String workPhone;

    @ManyToMany(
            cascade = {CascadeType.MERGE,CascadeType.PERSIST}
    )
    @Setter
    Set<TeacherSchoolClass> teacherSchoolClass;

}
