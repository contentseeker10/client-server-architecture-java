package dev.contentseeker10;

import dev.contentseeker10.packet.Decriptor;
import dev.contentseeker10.packet.Encriptor;
import dev.contentseeker10.packet.Message;
import dev.contentseeker10.pipeline.FakeReceiver;
import dev.contentseeker10.pipeline.FakeSender;
import dev.contentseeker10.pipeline.Processor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerManager {

    private static final ServerManager INSTANCE = new ServerManager();

    private ServerManager() {}

    public static ServerManager getInstance() {
        return INSTANCE;
    }

    private final BlockingQueue<byte[]> rawQueue = new LinkedBlockingQueue<>(1000);
    private final BlockingQueue<Message> decodedQueue = new LinkedBlockingQueue<>(1000);
    private final BlockingQueue<Message> responseQueue = new LinkedBlockingQueue<>(1000);
    private final BlockingQueue<byte[]> sendQueue = new LinkedBlockingQueue<>(1000);

    private final List<Thread> runningThreads = new ArrayList<>();

    public void start(int receivers, int decriptors, int processors, int encriptors, int senders) {
        System.out.println("[SERVER] Starting server...");
        startRecievers(receivers);
        startDecriptors(decriptors);
        startProcessors(processors);
        startEncriptors(encriptors);
        startSenders(senders);
        System.out.println("[SERVER] Server started successfully.");
    }

    private void startRecievers(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(new FakeReceiver(rawQueue), "Receiver-Thread-" + i);
            t.start();
            runningThreads.add(t);
        }
    }

    private void startDecriptors(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(new Decriptor(rawQueue, decodedQueue), "Decriptor-Thread-" + i);
            t.start();
            runningThreads.add(t);
        }
    }

    private void startProcessors(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(new Processor(decodedQueue, responseQueue), "Processor-Thread-" + i);
            t.start();
            runningThreads.add(t);
        }
    }

    private void startEncriptors(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(new Encriptor(responseQueue, sendQueue), "Encriptor-Thread-" + i);
            t.start();
            runningThreads.add(t);
        }
    }

    private void startSenders(int threads) {
        for (int i = 0; i < threads; i++) {
            Thread t = new Thread(new FakeSender(sendQueue), "Sender-Thread-" + i);
            t.start();
            runningThreads.add(t);
        }
    }

    public void stop() {
        System.out.println("[SERVER] Stopping server...");
        for (Thread t : runningThreads) {
            t.interrupt();
        }
        for (Thread t : runningThreads) {
            try {
                t.join(1000);
            } catch (InterruptedException e) {
                System.err.println("[SERVER] Thread join interrupted.");
                Thread.currentThread().interrupt();
            }
        }
        runningThreads.clear();
        System.out.println("[SERVER] Server stopped.");
    }

}
