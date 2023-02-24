package com.github.starwacki.repositories;

import com.github.starwacki.components.student.model.Grade;
import com.github.starwacki.components.student.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {

    List<Grade> findAllByStudentIdAndSubject(int studentID, Subject subject);
    Optional<Grade> findByStudentIdAndId(int studentID, int gradeId);


}
