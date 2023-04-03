package com.github.starwacki.common.repositories;

import com.github.starwacki.common.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import java.util.Optional;

@NoRepositoryBean
public interface AccountAbstractRepository<T extends Account,ID> extends JpaRepository<T,ID> {

    <S extends Account> Optional<S> findByUsername(String username);

}
