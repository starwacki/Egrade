package com.github.starwacki.components.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    Optional<Teacher> findTeacherById(int id);

}
