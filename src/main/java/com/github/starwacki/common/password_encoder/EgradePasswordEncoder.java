package com.github.starwacki.common.password_encoder;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface EgradePasswordEncoder extends PasswordEncoder {

    String decode(String password);

}
