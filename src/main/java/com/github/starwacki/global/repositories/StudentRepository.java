package com.github.starwacki.global.repositories;

import com.github.starwacki.components.account.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {


    List<Student> getAllBySchoolClassId(int id);

    Student findStudentById(int id);

    List<Student> findAllBySchoolClassNameAndSchoolClassClassYear(String name, int year);
}
