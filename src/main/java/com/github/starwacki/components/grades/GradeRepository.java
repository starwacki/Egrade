package com.github.starwacki.components.grades;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
interface GradeRepository extends JpaRepository<Grade,Integer> {

    Optional<List<Grade>> findAllByStudentID(int studentID);

    Optional<List<Grade>> findAllByStudentIDAndGradeSubject(int studentID, GradeSubject gradeSubject);

    Optional<Grade> findByStudentIDAndId(int studentID, int gradeID);

}
