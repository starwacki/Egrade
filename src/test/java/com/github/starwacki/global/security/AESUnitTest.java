package com.github.starwacki.global.security;

import com.github.starwacki.global.security.AES;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AESUnitTest {

    @Test
    @DisplayName("Test encrypt return char sentence another than given password")
    void encrypt_givenPassword_shouldReturnCharSentenceAnotherThanGivenPassword() throws Exception {
        //given
        String password = "password";

        //when
        String encryptionPassword = AES.encrypt(password);

        //then
        assertNotEquals(password,encryptionPassword);

    }

    @Test
    @DisplayName("Test decrypt return char sentence same like than given password")
    void decrypt_givenEncryptedPassword_shouldReturnCharSentenceAnotherThanGivenPassword() throws Exception {
        //given
        String password = "password";

        //when
        String encryptionPassword = AES.encrypt(password);
        String decryptionPassword = AES.decrypt(encryptionPassword);

        //then
        assertNotEquals(password,encryptionPassword);
        assertEquals(password,decryptionPassword);
    }
}
