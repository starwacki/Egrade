package com.github.starwacki.components.auth;


/**
 * This class is an implementation of the Spring Security PasswordEncoder interface
 * which encrypts passwords using a custom AES encryption algorithm.
 * The main purpose of this class is to provide a way to access passwords directly,
 * without using any of Spring's built-in PasswordEncoder implementations (Because
 *  spring encoders are one-way functions). This is useful in cases where access
 *  to passwords is required for certain functionalities
 */

class EgradePasswordEncoderImpl implements EgradePasswordEncoder {


    /**
     * This method encrypts the raw password using a custom AES encryption algorithm.
     *
     * @param rawPassword the raw password to be encrypted
     * @return the encrypted password as a string
     */

    @Override
    public String encode(CharSequence rawPassword) {
        return AuthenticationAESAlgorithm.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return rawPassword.toString().equals(encodedPassword);
    }


    @Override
    public String decode(String password) {
        return AuthenticationAESAlgorithm.decrypt(password);
    }
}
