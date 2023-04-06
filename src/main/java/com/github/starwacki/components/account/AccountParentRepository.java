package com.github.starwacki.components.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountParentRepository extends JpaRepository<AccountParent,Integer> {

}
