package com.github.starwacki.components.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AccountTeacherRepository extends JpaRepository<AccountTeacher,Integer> {


}
