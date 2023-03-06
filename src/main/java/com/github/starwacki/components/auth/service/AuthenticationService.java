package com.github.starwacki.components.auth.service;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.global.model.account.Account;
import com.github.starwacki.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountOperationsService accountOperationsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticateByUsernameAndPassword(request);
        Account user = getUserAccount(request);
        String jwt  = jwtService.generateToken(user);
        return createAuthenticationResponse(jwt);
    }

    private Authentication authenticateByUsernameAndPassword(AuthenticationRequest request) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), passwordEncoder.encode(request.password())));
    }

    private Account getUserAccount(AuthenticationRequest request) {
        return accountOperationsService
                .findAccountByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));
    }

    private AuthenticationResponse createAuthenticationResponse(String jwt) {
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }


}
