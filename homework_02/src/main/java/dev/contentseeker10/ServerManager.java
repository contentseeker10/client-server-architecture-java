package dev.contentseeker10;

import dev.contentseeker10.packet.Message;

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

    public void start() {

    }

    private void startRecievers(int threads) {

    }

    private void startDecriptors(int threads) {

    }

    private void startProcessors(int threads) {

    }

    private void startEncriptors(int threads) {

    }

    private void startSenders(int threads) {

    }

}
