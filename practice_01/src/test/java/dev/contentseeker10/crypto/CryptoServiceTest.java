package dev.contentseeker10.crypto;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

class CryptoServiceTest {

    @Test
    void shouldEncryptData() {
        String originalMessage = "Secret payload with JSON: {\"id\": 1}";
        byte[] originalBytes = originalMessage.getBytes(StandardCharsets.UTF_8);

        byte[] encryptedBytes = CryptoService.encrypt(originalBytes);

        assertThat(encryptedBytes)
                .isNotEmpty()
                .isNotEqualTo(originalBytes);
    }

    @Test
    void shouldDecryptData() {
        String originalMessage = "Secret payload with JSON: {\"id\": 1}";
        byte[] originalBytes = originalMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = CryptoService.encrypt(originalBytes);

        byte[] decryptedBytes = CryptoService.decrypt(encryptedBytes);

        assertThat(decryptedBytes)
                .isEqualTo(originalBytes);

        String decryptedMessage = new String(decryptedBytes, StandardCharsets.UTF_8);
        assertThat(decryptedMessage)
                .isEqualTo(originalMessage);
    }

}