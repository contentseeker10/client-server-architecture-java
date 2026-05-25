package dev.contentseeker10.pipeline;

public interface Sender {
    void sendMessage(byte[] mess, java.net.InetAddress target);
}
