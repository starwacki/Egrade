package com.github.starwacki.repositories;

import com.github.starwacki.account.model.Student;
import com.github.starwacki.student.model.Grade;
import com.github.starwacki.student.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade,Integer> {

    List<Grade> findAllByStudentIdAndSubject(int studentID, Subject subject);

    List<Grade> findAllByStudentId(int studentId);
    Optional<Grade> findByStudentIdAndId(int studentID, int gradeId);


}
