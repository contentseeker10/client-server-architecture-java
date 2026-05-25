package dev.contentseeker10;

import dev.contentseeker10.message.CommandType;
import dev.contentseeker10.message.Message;
import dev.contentseeker10.message.Payload;
import dev.contentseeker10.pipeline.Encriptor;
import dev.contentseeker10.warehouse.Product;
import dev.contentseeker10.warehouse.ProductGroup;
import dev.contentseeker10.warehouse.WarehouseService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ServerManagerTest {

    private static final int THREADS = 10;
    private static final int ACTIONS = 10;

    @BeforeEach
    void setUp() {
        WarehouseService.getInstance().getGroups().clear();
        WarehouseService.getInstance().getProducts().clear();

        Product.resetIdSequence();
        ProductGroup.resetIdSequence();

        WarehouseService.getInstance().addProductGroup("Grocery", "The food and staff to fill your stomach with lol.");
        WarehouseService.getInstance().addProductToGroup(1, "Buckwheat", "I eat buckwheat like every 2 weeks.", 50.0);
        WarehouseService.getInstance().writeOnProduct(1, 100);

        ServerManager.getInstance().start(0, 2, 4, 3, 5);
    }

    @AfterEach
    void tearDown() {
        ServerManager.getInstance().stop();
    }

    @Test
    void shouldWriteOffProductConcurrently() throws InterruptedException {
        Random random = new Random();
        AtomicInteger messageId = new AtomicInteger(0);

        CountDownLatch latch = new CountDownLatch(THREADS);

        for (int i = 0; i < THREADS; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < ACTIONS; j++) {
                        Payload payload = new Payload(CommandType.WRITE_OFF.getCode(), random.nextInt(100) + 1, "1,1");
                        Message msg = new Message((byte) 0x13, (byte) 1, messageId.incrementAndGet(), payload);
                        ServerManager.getInstance().getRawQueue().put(Encriptor.encript(msg));
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            }).start();
        }

        latch.await(5, TimeUnit.SECONDS);

        while (!ServerManager.getInstance().isPipelineEmpty()) {
            Thread.sleep(50);
        }

        int finalAmount = WarehouseService.getInstance().getProductAmount(1);
        assertThat(finalAmount).isEqualTo(0);
    }

}