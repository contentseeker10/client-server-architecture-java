package dev.contentseeker10.packet;

import java.nio.ByteBuffer;

public class Encoder {

    public static final byte MAGIC = 0x13;

    public static byte[] encode(Packet packet) {
        ByteBuffer buffer = ByteBuffer.allocate(calcSize(packet.getLength()));

        buffer.put(MAGIC);
        buffer.put(packet.getSource());
        buffer.putLong(packet.getPacketId());
        buffer.putInt(packet.getLength());
        buffer.putShort(packet.getHeaderCrc16());

        buffer.putInt(packet.getMessage().getCmdType());
        buffer.putInt(packet.getMessage().getUserId());
        byte[] payload = packet.getMessage().getPayload().getBytes();
        buffer.put(payload);
        buffer.putShort(packet.getMessageCrc16());

        return buffer.array();
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
