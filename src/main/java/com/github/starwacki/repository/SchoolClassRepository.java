package com.github.starwacki.repository;

import com.github.starwacki.model.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {

    Optional<SchoolClass> findByNameAndClassYear(String name, int classYear);

}
