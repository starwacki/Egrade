package com.github.starwacki.common.model.school_class;
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




}
