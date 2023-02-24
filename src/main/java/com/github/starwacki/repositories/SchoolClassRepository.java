package com.github.starwacki.repositories;

import com.github.starwacki.components.student.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {

    Optional<SchoolClass> findByNameAndClassYear(String name, int classYear);

}
