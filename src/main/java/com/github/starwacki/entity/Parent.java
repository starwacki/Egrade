package com.github.starwacki.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "parents")
public class Parent extends Account {

    @NonNull
    private String phoneNumber;

    @OneToOne
    private Student student;



}
