package com.github.starwacki.components.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface StudentRepository extends JpaRepository<Student,Integer> {

    List<Student> findAllBySchoolClassNameAndSchoolClassYear(String schoolClassName, int schoolClassYear);

}

