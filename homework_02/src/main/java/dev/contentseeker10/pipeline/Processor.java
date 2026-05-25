package dev.contentseeker10.pipeline;

import dev.contentseeker10.packet.CommandType;
import dev.contentseeker10.packet.Message;
import dev.contentseeker10.packet.Payload;
import dev.contentseeker10.warehouse.WarehouseService;

import java.util.concurrent.BlockingQueue;

public class Processor implements Runnable {

    private BlockingQueue<Message> inputQueue;
    private BlockingQueue<Message> outputQueue;

    private static final String MSG_OK = "OK";
    private static final String MSG_FAIL = "FAIL";
    private static final String MSG_UNK = "UNKNOWN_COMMAND";

    public Processor(BlockingQueue<Message> inputQueue, BlockingQueue<Message> outputQueue) {
        this.inputQueue = inputQueue;
        this.outputQueue = outputQueue;
    }

    public void process(Message message) {
        Payload payload = message.getPayload();
        String[] parsedData = payload.getData().split(",");
        String data = switch (CommandType.fromCode(payload.getCmdType())) {
            case GET_AMOUNT -> {
                int amount = WarehouseService.getInstance().getProductAmount(Integer.parseInt(parsedData[0]));
                yield amount >= 0 ? String.valueOf(amount) : MSG_FAIL;
            }
            case WRITE_OFF
                    -> WarehouseService.getInstance().writeOffProduct(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1])) ? MSG_OK : MSG_FAIL;
            case WRITE_ON
                    -> WarehouseService.getInstance().writeOnProduct(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1])) ? MSG_OK : MSG_FAIL;
            case ADD_GROUP
                    -> WarehouseService.getInstance().addProductGroup(parsedData[0], parsedData[1]) ? MSG_OK : MSG_FAIL;
            case ADD_PRODUCT_TO_GROUP
                    -> WarehouseService.getInstance().addProductToGroup(Integer.parseInt(parsedData[0]), parsedData[1], parsedData[2], Double.parseDouble(parsedData[3])) ? MSG_OK : MSG_FAIL;
            case SET_PRICE
                    -> WarehouseService.getInstance().setProductPrice(Integer.parseInt(parsedData[0]), Double.parseDouble(parsedData[1])) ? MSG_OK : MSG_FAIL;
            default -> MSG_UNK;
        };
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
