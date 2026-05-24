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