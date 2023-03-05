package com.github.starwacki.components.auth.service;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.global.model.account.Account;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManage;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManage.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        Account user = (Account) findAccountByUsername(request.username())
                .orElseThrow();
        String jwtToken  = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private  Optional<?> findAccountByUsername(String username) {
        if (username.contains("STU"))
            return studentRepository.findByUsername(username);
        else if (username.contains("NAU"))
            return teacherRepository.findByUsername(username);
        else if (username.contains("RO"))
            return parentRepository.findByUsername(username);
        else
            return Optional.empty();
    }

}
