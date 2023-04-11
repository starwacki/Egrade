package com.github.starwacki.components.schoolclass;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
interface SchoolClassRepository extends JpaRepository<SchoolClass,Integer> {

    Optional<SchoolClass> findSchoolClassByNameAndAndClassYear(String name, int classYear);



}
