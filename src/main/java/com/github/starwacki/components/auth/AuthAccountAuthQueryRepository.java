package com.github.starwacki.components.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AuthAccountAuthQueryRepository extends JpaRepository<AuthAccountDetails,Integer> {

    Optional<AuthAccountDetails> findByUsername(String username);

}
