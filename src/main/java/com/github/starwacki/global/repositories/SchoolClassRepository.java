package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.school_class.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {

    Optional<SchoolClass> findByNameAndClassYear(String name, int classYear);


}
