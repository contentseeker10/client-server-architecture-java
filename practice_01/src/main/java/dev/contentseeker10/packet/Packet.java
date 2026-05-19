package dev.contentseeker10.packet;

import dev.contentseeker10.crypto.Crc16;

import java.nio.ByteBuffer;

public class Packet {

    private byte magic;
    private byte source;
    private long packetId;
    private int length;
    private Message message;

    private final short headerCrc16;
    private final short messageCrc16;

    public Packet(byte magic, byte source, long packetId, int length, Message message) {
        this.magic = magic;
        this.source = source;
        this.packetId = packetId;
        this.length = length;
        this.message = message;

        ByteBuffer headerBuffer = ByteBuffer.allocate(1 + 1 + 8 + 4);
        headerBuffer.put(magic);
        headerBuffer.put(source);
        headerBuffer.putLong(packetId);
        headerBuffer.putInt(length);
        this.headerCrc16 = Crc16.calculateSrc(headerBuffer.array());

        ByteBuffer messageBuffer = ByteBuffer.allocate(16 + length);
        headerBuffer.putInt(message.getCmdType());
        headerBuffer.putInt(message.getUserId());
        byte[] payload = message.getPayload().getBytes();
        headerBuffer.put(payload);
        this.messageCrc16 = Crc16.calculateSrc(messageBuffer.array());
    }


    public byte getMagic() {
        return magic;
    }

    public void setMagic(byte magic) {
        this.magic = magic;
    }

    public byte getSource() {
        return source;
    }

    public void setSource(byte source) {
        this.source = source;
    }

    public long getPacketId() {
        return packetId;
    }

    public void setPacketId(long packetId) {
        this.packetId = packetId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public short getHeaderCrc16() {
        return headerCrc16;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public short getMessageCrc16() {
        return messageCrc16;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "magic=" + magic +
                "source=" + source +
                ", packetId=" + packetId +
                ", length=" + length +
                ", headerCrc16=" + headerCrc16 +
                ", message=" + message +
                ", messageCrc16=" + messageCrc16 +
                '}';
    }
}

class Message {

    private int cmdType;
    private int userId;

    private String payload;

    public Message(int cmdType, int userId, String payload) {
        this.cmdType = cmdType;
        this.userId = userId;
        this.payload = payload;
    }

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
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