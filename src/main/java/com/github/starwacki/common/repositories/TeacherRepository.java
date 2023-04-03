package com.github.starwacki.common.repositories;

import com.github.starwacki.common.model.account.Teacher;

public interface TeacherRepository extends AccountAbstractRepository<Teacher,Integer> {

    Teacher findTeacherById(int id);


}
