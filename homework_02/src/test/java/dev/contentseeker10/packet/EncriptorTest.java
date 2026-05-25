package dev.contentseeker10.packet;

import dev.contentseeker10.crypto.Crc16;
import dev.contentseeker10.crypto.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class EncriptorTest {

    private Message testMessage;
    private final byte SOURCE = 5;
    private final long PACKET_ID = 987654321L;
    private final int CMD_TYPE = 10;
    private final int USER_ID = 999;
    private final String PAYLOAD = "{\"status\":\"ok\"}";

    @BeforeEach
    void setUp() {
        Payload payload = new Payload(CMD_TYPE, USER_ID, PAYLOAD);
        testMessage = new Message(Encriptor.MAGIC, SOURCE, PACKET_ID, payload);
    }

    @Test
    void shouldProduceCorrectArraySize() {
        byte[] result = Encriptor.encript(testMessage);

        int plainLen = 4 + 4 + PAYLOAD.getBytes(StandardCharsets.UTF_8).length;
        byte[] plainBytes = ByteBuffer.allocate(plainLen)
                .putInt(CMD_TYPE).putInt(USER_ID).put(PAYLOAD.getBytes(StandardCharsets.UTF_8))
                .array();
        int expectedEncryptedLength = CryptoService.encrypt(plainBytes).length;

        int expectedTotalSize = 16 + expectedEncryptedLength + 2;

        assertThat(result).hasSize(expectedTotalSize);
    }

    @Test
    void shouldEncriptHeaderFieldsCorrectly() {
        byte[] result = Encriptor.encript(testMessage);

        ByteBuffer buffer = ByteBuffer.wrap(result);

        assertThat(buffer.get()).isEqualTo(Encriptor.MAGIC);
        assertThat(buffer.get()).isEqualTo(SOURCE);
        assertThat(buffer.getLong()).isEqualTo(PACKET_ID);

        int encodedLength = buffer.getInt();
        assertThat(encodedLength).isGreaterThan(0);
    }

    @Test
    void shouldCalculateHeaderCrcCorrectly() {
        byte[] result = Encriptor.encript(testMessage);

        ByteBuffer buffer = ByteBuffer.wrap(result);
        buffer.position(14);
        short encodedHeaderCrc = buffer.getShort();

        byte[] headerBytes = Arrays.copyOfRange(result, 0, 14);
        short expectedHeaderCrc = Crc16.calculateSrc(headerBytes);

        assertThat(encodedHeaderCrc).isEqualTo(expectedHeaderCrc);
    }

    @Test
    void shouldEmbedMessageCorrectly() {
        byte[] result = Encriptor.encript(testMessage);
        ByteBuffer buffer = ByteBuffer.wrap(result);

        buffer.position(10);
        int length = buffer.getInt();

        buffer.position(16);

        byte[] encryptedMessageBytes = new byte[length];
        buffer.get(encryptedMessageBytes);

        byte[] decryptedMessage = CryptoService.decrypt(encryptedMessageBytes);
        ByteBuffer msgBuffer = ByteBuffer.wrap(decryptedMessage);

        assertThat(msgBuffer.getInt()).isEqualTo(CMD_TYPE);
        assertThat(msgBuffer.getInt()).isEqualTo(USER_ID);

        byte[] payloadBytes = new byte[decryptedMessage.length - 8];
        msgBuffer.get(payloadBytes);
        String decodedPayload = new String(payloadBytes, StandardCharsets.UTF_8);

        assertThat(decodedPayload).isEqualTo(PAYLOAD);
    }

    @Test
    void shouldAppendMessageCrcCorrectly() {
        byte[] result = Encriptor.encript(testMessage);
        ByteBuffer buffer = ByteBuffer.wrap(result);

        buffer.position(10);
        int length = buffer.getInt();

        buffer.position(16);
        byte[] encryptedMessageBytes = new byte[length];
        buffer.get(encryptedMessageBytes);

        short encodedMessageCrc = buffer.getShort();

        short expectedMessageCrc = Crc16.calculateSrc(encryptedMessageBytes);

        assertThat(encodedMessageCrc).isEqualTo(expectedMessageCrc);
    }
}