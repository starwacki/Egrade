package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.global.model.school_class.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    Teacher findTeacherById(int id);


}
