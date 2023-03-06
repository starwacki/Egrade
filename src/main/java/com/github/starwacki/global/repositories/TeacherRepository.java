package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Teacher;
import com.github.starwacki.global.model.school_class.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends AccountAbstractRepository<Teacher,Integer> {

    Teacher findTeacherById(int id);


}
