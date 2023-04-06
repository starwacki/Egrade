package com.github.starwacki.components.auth;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class UserDetailServiceImpl implements UserDetailsService {

    private final AuthAccountAuthQueryRepository authAccountAuthQueryRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(authAccountAuthQueryRepository.findByUsername(username).toString());
           return  authAccountAuthQueryRepository.findByUsername(username)
                   .orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
