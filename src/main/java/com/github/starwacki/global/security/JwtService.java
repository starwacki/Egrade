package com.github.starwacki.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECURITY_KEY = "482B4D6250655368566D597133743677397A24432646294A404E635266546A57";

    public String extractUsername(String jwt) {
        return extractClaim(jwt,claims -> claims.getSubject());
    }

    public <T> T extractClaim(String jwt, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimResolver.apply(claims);
    }

    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails) {
       return Jwts
               .builder()
               .setClaims(extraClaims)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis()+getOneDayInMilliSeconds()))
               .signWith(getSignInKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    public boolean isTokenValid(String jwt,UserDetails userDetails) {
        final String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt,claims -> claims.getExpiration());
    }

    private long getOneDayInMilliSeconds() {
        return 1000*60*60*24;
    }

    private Claims extractAllClaims(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSignInKey() {
      byte[] keyBytes = Decoders.BASE64.decode(SECURITY_KEY);
      return Keys.hmacShaKeyFor(keyBytes);
    }
}
