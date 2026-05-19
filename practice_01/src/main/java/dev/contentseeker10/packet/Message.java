package dev.contentseeker10.packet;

public class Message {

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