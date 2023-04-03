package com.github.starwacki.common.repositories;

import com.github.starwacki.common.model.account.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends AccountAbstractRepository<Admin,Integer> {


}
