package com.github.starwacki.components.teacher;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "classes")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class TeacherSchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Getter
    private String className;

    @Getter
    private int classYear;

    //Equals Don't compare objects id!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherSchoolClass that = (TeacherSchoolClass) o;
        return classYear == that.classYear && className.equals(that.className);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, classYear);
    }


}
