package com.github.starwacki.components.account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {

    Teacher findTeacherById(int id);


}
