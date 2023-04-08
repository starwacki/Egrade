package com.github.starwacki.components.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface EgradePasswordEncoder extends PasswordEncoder {

    String decode(String password);

}
