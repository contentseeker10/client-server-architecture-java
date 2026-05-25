package dev.contentseeker10.pipeline;

import dev.contentseeker10.message.Message;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public class FakeSender implements Sender, Runnable {

    private final BlockingQueue<byte[]> inputQueue;

    public FakeSender(BlockingQueue<byte[]> inputQueue) {
        this.inputQueue = inputQueue;
    }

    @Override
    public void sendMessage(byte[] mess, InetAddress target) {
        Message response = Decriptor.decript(mess);
        System.out.println("[SENDER] Sent response for Message ID: "
                        + response.getMessageId()
                        + " Status: "
                        + response.getPayload().getData());
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                byte[] rawData = inputQueue.take();
                sendMessage(rawData, null);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
