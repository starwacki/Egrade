package com.github.starwacki.components.auth.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
