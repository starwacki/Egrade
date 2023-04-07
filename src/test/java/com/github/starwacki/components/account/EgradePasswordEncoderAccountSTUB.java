package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;

class EgradePasswordEncoderAccountSTUB implements EgradePasswordEncoder {
    @Override
    public String decode(String password) {
        return password;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
