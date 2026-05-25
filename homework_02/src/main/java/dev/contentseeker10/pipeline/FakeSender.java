package dev.contentseeker10.pipeline;

import dev.contentseeker10.packet.Decriptor;
import dev.contentseeker10.packet.Message;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public class FakeSender implements Sender, Runnable {

    private BlockingQueue<byte[]> sendQueue;

    public FakeSender(BlockingQueue<byte[]> sendQueue) {
        this.sendQueue = sendQueue;
    }

    @Override
    public void sendMessage(byte[] mess, InetAddress target) {
        Message response = Decriptor.decript(mess);
        System.out.println("[SENDER] Sent response for Message ID: "
                        + response.getMessageId()
                        + "Status: "
                        + response.getPayload().getData());
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                sendMessage(sendQueue.poll(), null);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
