package dev.contentseeker10.pipeline;

import dev.contentseeker10.packet.CommandType;
import dev.contentseeker10.packet.Message;
import dev.contentseeker10.packet.Payload;
import dev.contentseeker10.warehouse.WarehouseService;

import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable {

    private BlockingQueue<Message> inputQueue;
    private BlockingQueue<Message> outputQueue;

    public Processor(BlockingQueue<Message> inputQueue, BlockingQueue<Message> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    public void process(Message message) {
        Payload payload = message.getPayload();
        String[] parsedData = payload.getData().split(",");
        int response = switch (CommandType.fromCode(payload.getCmdType())) {
            case GET_AMOUNT
                    -> WarehouseService.getProductAmount(Integer.parseInt(parsedData[0]));
            case WRITE_OFF
                    -> Boolean.hashCode(WarehouseService.writeOffProduct(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1])));
            case WRITE_ON
                    -> Boolean.hashCode(WarehouseService.writeOnProduct(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1])));
            case ADD_GROUP
                    -> Boolean.hashCode(WarehouseService.addProductGroup(parsedData[0], parsedData[1]));
            case ADD_PRODUCT_TO_GROUP
                    -> Boolean.hashCode(WarehouseService.addProductToGroup(Integer.parseInt(parsedData[0]),
                                                        parsedData[1],
                                                        parsedData[2],
                                                        Double.parseDouble(parsedData[3])));
            case SET_PRICE
                    -> Boolean.hashCode(WarehouseService.setProductPrice(Integer.parseInt(parsedData[0]), Double.parseDouble(parsedData[1])));
            default -> -1;
        };
        String data;
        if (response > 1) {
            data = String.valueOf(response);
        } else {
          data = response == 1 ? "OK" : "FAIL";
        }
        Payload responsePayload = new Payload(CommandType.RESPONSE.getCode(), 0, data);
        Message responseMessage = new Message((byte) 0x13, (byte) 2, message.getMessageId(), responsePayload);
        try {
            outputQueue.put(responseMessage);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Message request = inputQueue.take();
                process(request);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
