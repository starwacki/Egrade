package com.github.starwacki.common.repositories;

import com.github.starwacki.common.model.grades.Grade;
import com.github.starwacki.common.model.grades.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {

//    List<Grade> findAllByStudentIdAndSubject(int studentID, Subject subject);
//    Optional<Grade> findByStudentIdAndId(int studentID, int gradeId);


}
