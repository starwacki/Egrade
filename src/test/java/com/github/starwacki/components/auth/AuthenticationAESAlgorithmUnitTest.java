package com.github.starwacki.components.auth;

import com.github.starwacki.components.auth.exceptions.AESException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthenticationAESAlgorithmUnitTest {

    @Test
    @DisplayName("Test encrypt return char sentence another than given password")
    void encrypt_givenPassword_shouldReturnCharSentenceAnotherThanGivenPassword()  {
        //given
        String password = "password";

        //when
        String encryptionPassword = AuthenticationAESAlgorithm.encrypt(password);

        //then
        assertNotEquals(password,encryptionPassword);

    }

    @Test
    @DisplayName("Test decrypt return char sentence same like than given password")
    void decrypt_givenEncryptedPassword_shouldReturnCharSentenceAnotherThanGivenPassword()  {
        //given
        String password = "password";

        //when
        String encryptionPassword = AuthenticationAESAlgorithm.encrypt(password);
        String decryptionPassword = AuthenticationAESAlgorithm.decrypt(encryptionPassword);

        //then
        assertNotEquals(password,encryptionPassword);
        assertEquals(password,decryptionPassword);
    }

    @Test
    @DisplayName("Test decrypt decrypted char sentence throw exception")
    void decrypt_givenDecryptedPassword_shouldThrowAESException()  {
        //given
        String password = "password";

        //then
        assertThrows(AESException.class,() -> AuthenticationAESAlgorithm.decrypt(password));
    }

    @Test
    @DisplayName("Test encrypt null char sentence throw exception")
    void encrypt_givenNullPassword_shouldThrowAESException()  {
        //given
        String password = null;

        //then
        assertThrows(AESException.class,() -> AuthenticationAESAlgorithm.encrypt(password));
    }
}
