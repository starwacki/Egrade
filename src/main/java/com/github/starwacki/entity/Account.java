package com.github.starwacki.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@MappedSuperclass
public abstract class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;

    private String password;

    @NonNull
    private String firstname;

    @NonNull
    private String lastname;

    @Enumerated(EnumType.STRING)
    private Role role;


}
