package com.github.starwacki.components.student;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Table(name = "students")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Student {

    @Id
    private int id;

    private String firstname;

    private String lastname;

    @Setter
    private String schoolClassName;

    @Setter
    private int schoolClassYear;

    private String parentPhoneNumber;
}
