package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends AccountAbstractRepository<Parent,Integer> {

}
