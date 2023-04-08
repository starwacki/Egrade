package com.github.starwacki.common;

import com.github.starwacki.common.model.school_class.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {

    Optional<SchoolClass> findSchoolClassByNameAndAndClassYear(String name, int classYear);



}
