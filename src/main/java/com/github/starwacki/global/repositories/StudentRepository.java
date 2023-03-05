package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    Optional<Student> findByUsername(String username);
    List<Student> getAllBySchoolClassId(int id);

    Student findStudentById(int id);

    List<Student> findAllBySchoolClassNameAndSchoolClassClassYear(String name, int year);
}
