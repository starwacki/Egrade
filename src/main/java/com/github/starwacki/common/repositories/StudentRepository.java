package com.github.starwacki.common.repositories;

import com.github.starwacki.common.model.account.Student;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends AccountAbstractRepository<Student,Integer> {

    List<Student> findAllBySchoolClassNameAndSchoolClassClassYear(String name, int year);
}
