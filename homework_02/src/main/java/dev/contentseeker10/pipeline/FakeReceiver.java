package dev.contentseeker10.pipeline;

import dev.contentseeker10.message.*;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

public class FakeReceiver implements Receiver, Runnable {

    private final BlockingQueue<byte[]> outputQueue;

    private final AtomicLong packetIdSequence = new AtomicLong(0);

    public FakeReceiver(BlockingQueue<byte[]> outputQueue) {
        this.outputQueue = outputQueue;
    }

    @Override
    public void receiveMessage() {
        CommandType[] types = CommandType.values();
        CommandType randomType = types[1 + random.nextInt(1, 6)];
        Payload payload = buildPayload(randomType);
        Message message = new Message(Encriptor.MAGIC, (byte) 1, packetIdSequence.incrementAndGet(), payload);
        byte[] encodedData = Encriptor.encript(message);
        try {
            outputQueue.put(encodedData);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Payload buildPayload(CommandType randomType) {
        int userId = 1;
        String payload = switch (randomType) {
            case GET_AMOUNT -> generateGetAmountPayload();
            case WRITE_OFF -> generateWriteOffPayload();
            case WRITE_ON -> generateWriteOnPayload();
            case ADD_GROUP -> generateAddGroupPayload();
            case ADD_PRODUCT_TO_GROUP -> generateAddProductToGroupPayload();
            case SET_PRICE -> generateSetPricePayload();
            default -> "null";
        };
        return new Payload(randomType.getCode(), userId, payload);
    }

    private String generateGetAmountPayload() {
        int id = random.nextInt(5) + 1;
        return String.valueOf(id);
    }

    private String generateWriteOffPayload() {
        int id = random.nextInt(5) + 1;
        int amount = random.nextInt(10) + 1;
        return id + "," + amount;
    }

    private String generateWriteOnPayload() {
        int id = random.nextInt(5) + 1;
        int amount = random.nextInt(10) + 1;
        return id + "," + amount;
    }

    private String generateAddGroupPayload() {
        String name = "ProductGroup_" + random.nextInt(100);
        String description = "some cool product group description for " + name;
        return name + "," + description;
    }

    private String generateAddProductToGroupPayload() {
        int id = random.nextInt(5) + 1;
        String name = "SomeProduct_" + random.nextInt(100);
        String description = "man this is really cool product named " + name;
        double price = 99.99;
        return id + "," + name + "," + description + "," + price;
    }

    private String generateSetPricePayload() {
        int id = random.nextInt(5) + 1;
        double price = 540.00;
        return id + "," + price;
    }


    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                receiveMessage();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final Random random = new Random();
}
