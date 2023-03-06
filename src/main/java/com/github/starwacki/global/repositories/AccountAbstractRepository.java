package com.github.starwacki.global.repositories;

import com.github.starwacki.global.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface AccountAbstractRepository<T extends Account,ID> extends JpaRepository<T,ID> {

    <S extends Account> Optional<S> findByUsername(String username);

}
