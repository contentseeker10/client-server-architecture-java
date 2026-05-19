package dev.contentseeker10.packet;

import java.nio.charset.StandardCharsets;

public class Packet {

    private final byte magic;
    private final byte source;
    private final long packetId;
    private final int length;
    private final Message message;

    public Packet(byte magic, byte source, long packetId, Message message) {
        this.magic = magic;
        this.source = source;
        this.packetId = packetId;
        this.length = message.getPayload().getBytes(StandardCharsets.UTF_8).length;
        this.message = message;
    }

    public byte getMagic() {
        return magic;
    }

    public byte getSource() {
        return source;
    }

    public long getPacketId() {
        return packetId;
    }

    public int getLength() {
        return length;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "magic=" + magic +
                "source=" + source +
                ", packetId=" + packetId +
                ", length=" + length +
                ", message=" + message +
                '}';
    }
}

class Message {

    private final int cmdType;
    private final int userId;

    private final String payload;

    public Message(int cmdType, int userId, String payload) {
        this.cmdType = cmdType;
        this.userId = userId;
        this.payload = payload;
    }

    public int getCmdType() {
        return cmdType;
    }

    public int getUserId() {
        return userId;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "cmdType=" + cmdType +
                ", userId=" + userId +
                ", payload=" + payload +
                '}';
    }
}