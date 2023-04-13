package com.github.starwacki.components.auth;

import com.github.starwacki.components.auth.dto.AuthenticationRequest;
import com.github.starwacki.components.auth.dto.AuthenticationResponse;
import com.github.starwacki.components.auth.exceptions.WrongAuthenticationException;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationFacade {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthAccountAuthQueryRepository authAccountAuthQueryRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticateByUsernameAndPassword(request);
            AuthAccountDetails user = getUserAccountAuthDetails(request);
            String jwt  = jwtService.generateToken(generateExtraClaims(user),user);
            return createAuthenticationResponse(jwt,user.getAccountRole());
        } catch (AuthenticationException e) {
            throw new WrongAuthenticationException();
        }
    }

    public Cookie generateJWTCookie(String jwt) {
        Cookie cookie = new Cookie("egrade-jwt", jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(60*60*24*1000*7);
        cookie.setPath("/");
        return cookie;
    }

    private Map<String,Object> generateExtraClaims(AuthAccountDetails authDetails) {
        HashMap<String,Object> claims = new HashMap<>();
        claims.put("ROLE",authDetails.getAuthorities());
        return claims;
    }

    private Authentication authenticateByUsernameAndPassword(AuthenticationRequest request) {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(), AuthenticationAESAlgorithm.encrypt(request.password())));
    }

    private AuthAccountDetails getUserAccountAuthDetails(AuthenticationRequest request) {
        return   authAccountAuthQueryRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException(request.username()));
    }

    private AuthenticationResponse createAuthenticationResponse(String jwt,String accountRole) {
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .accountRole(accountRole)
                .build();
    }


}
