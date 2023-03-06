package com.github.starwacki.components.auth.service;

import com.github.starwacki.global.model.account.Account;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final AccountOperationsService accountOperationsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
           return accountOperationsService.findAccountByUsername(username)
                   .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
