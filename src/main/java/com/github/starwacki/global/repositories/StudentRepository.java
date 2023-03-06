package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends AccountAbstractRepository<Student,Integer> {

    List<Student> findAllBySchoolClassNameAndSchoolClassClassYear(String name, int year);
}
