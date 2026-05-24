package dev.contentseeker10.packet;

import dev.contentseeker10.crypto.Crc16;
import dev.contentseeker10.crypto.CryptoService;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class Encoder {

    public static final byte MAGIC = 0x13;

    public static byte[] encode(Packet packet) {
        byte[] payload = packet.getMessage().getPayload().getBytes(StandardCharsets.UTF_8);
        ByteBuffer messageBuffer = ByteBuffer.allocate(payload.length + 8);
        messageBuffer.putInt(packet.getMessage().getCmdType());
        messageBuffer.putInt(packet.getMessage().getUserId());
        messageBuffer.put(payload);

        byte[] encryptedMessage = CryptoService.encrypt(messageBuffer.array());
        short messageCrc16 = Crc16.calculateSrc(encryptedMessage);

        ByteBuffer headerBuffer = ByteBuffer.allocate(1 + 1 + 8 + 4);
        headerBuffer.put(packet.getMagic());
        headerBuffer.put(packet.getSource());
        headerBuffer.putLong(packet.getPacketId());
        headerBuffer.putInt(encryptedMessage.length);
        short headerCrc16 = Crc16.calculateSrc(headerBuffer.array());

        ByteBuffer result = ByteBuffer.allocate(calcSize(encryptedMessage.length));
        result.put(headerBuffer.array());
        result.putShort(headerCrc16);
        result.put(encryptedMessage);
        result.putShort(messageCrc16);

        return result.array();
    }

    private static int calcSize(int messageLength) {
        byte magicSize = 1;
        byte srcSize = 1;
        byte packetIdSize = 8;
        byte lengthSize = 4;
        byte headerCrc16Size = 2;
        byte messageCrc16Size = 2;

        return magicSize + srcSize + packetIdSize
                + lengthSize + headerCrc16Size + messageCrc16Size
                + messageLength;
    }

}
