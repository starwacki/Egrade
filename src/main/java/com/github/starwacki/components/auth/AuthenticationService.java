package com.github.starwacki.components.auth;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.components.auth.exceptions.WrongAuthenticationException;
import com.github.starwacki.common.model.account.Account;
import com.github.starwacki.common.security.JwtService;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AccountOperationsService accountOperationsService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticateByUsernameAndPassword(request);
            Account user = getUserAccount(request);
            String jwt  = jwtService.generateToken(generateExtraClaims(user),user);
            return createAuthenticationResponse(jwt);
        } catch (AuthenticationException e) {
            throw new WrongAuthenticationException();
        }
    }

    public Cookie generateJWTCookie(String jwt) {
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60*60*24*1000*7);
        cookie.setPath("/");
        return cookie;
    }

    private Map<String,Object> generateExtraClaims(Account account) {
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("id",account.getId());
        return claims;
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
