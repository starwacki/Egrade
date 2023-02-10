package com.github.starwacki.repository;

import com.github.starwacki.model.account.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {


    List<Student> getAllBySchoolClassId(int id);
}
