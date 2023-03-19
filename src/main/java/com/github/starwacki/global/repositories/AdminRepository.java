package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends AccountAbstractRepository<Admin,Integer> {


}
