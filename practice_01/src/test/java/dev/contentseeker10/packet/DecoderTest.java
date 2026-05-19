package dev.contentseeker10.packet;

import dev.contentseeker10.crypto.Crc16;
import dev.contentseeker10.crypto.CryptoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class DecoderTest {

    private byte[] fullData;

    private final byte source = 1;
    private final long packetId = 12345L;
    private final int cmdType = 99;
    private final int userId = 1001;
    private final String payloadStr = "test_payload";

    @BeforeEach
    void setUp() {
        byte[] payloadBytes = payloadStr.getBytes(StandardCharsets.UTF_8);

        ByteBuffer decryptedBuf = ByteBuffer.allocate(8 + payloadBytes.length);
        decryptedBuf.putInt(cmdType);
        decryptedBuf.putInt(userId);
        decryptedBuf.put(payloadBytes);
        byte[] plaintextMessage = decryptedBuf.array();

        byte[] encryptedMessage = CryptoService.encrypt(plaintextMessage);

        int length = encryptedMessage.length;

        ByteBuffer headerBuf = ByteBuffer.allocate(14);
        headerBuf.put(Decoder.MAGIC);
        headerBuf.put(source);
        headerBuf.putLong(packetId);
        headerBuf.putInt(length);
        byte[] headerData = headerBuf.array();

        short headerCrc = Crc16.calculateSrc(headerData);
        short messageCrc = Crc16.calculateSrc(encryptedMessage);

        ByteBuffer packetBuf = ByteBuffer.allocate(14 + 2 + length + 2);
        packetBuf.put(headerData);
        packetBuf.putShort(headerCrc);
        packetBuf.put(encryptedMessage);
        packetBuf.putShort(messageCrc);
        fullData = packetBuf.array();
    }

    @Test
    void shouldValidDataReturnDecodedPacket() {
        Packet result = Decoder.decode(fullData);

        assertThat(result.getMagic()).isEqualTo(Decoder.MAGIC);
        assertThat(result.getSource()).isEqualTo(source);
        assertThat(result.getPacketId()).isEqualTo(packetId);
        assertThat(result.getMessage().getCmdType()).isEqualTo(cmdType);
        assertThat(result.getMessage().getUserId()).isEqualTo(userId);
        assertThat(result.getMessage().getPayload()).isEqualTo(payloadStr);
    }

    @Test
    void shouldInvalidMagicNumberThrowRuntimeException() {
        byte[] dataWithBadMagic = fullData.clone();
        dataWithBadMagic[0] = 0x00;

        assertThatThrownBy(() -> Decoder.decode(dataWithBadMagic))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong magic number. Unknown packet.");
    }

    @Test
    void shouldInvalidHeaderCrcThrowRuntimeException() {
        byte[] dataWithBadHeaderCrc = fullData.clone();
        dataWithBadHeaderCrc[14] = (byte) ~dataWithBadHeaderCrc[14];

        assertThatThrownBy(() -> Decoder.decode(dataWithBadHeaderCrc))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong Crc16. Packet is corrupted.");
    }

    @Test
    void shouldInvalidMessageCrcThrowRuntimeException() {
        byte[] dataWithBadMessageCrc = fullData.clone();
        dataWithBadMessageCrc[dataWithBadMessageCrc.length - 2] = (byte) ~dataWithBadMessageCrc[dataWithBadMessageCrc.length - 2];

        assertThatThrownBy(() -> Decoder.decode(dataWithBadMessageCrc))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Wrong Crc16. Packet is corrupted.");
    }
}