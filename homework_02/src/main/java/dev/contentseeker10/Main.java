package dev.contentseeker10;

public class Main {
    static void main() throws InterruptedException {
        ServerManager.getInstance().start(2, 2, 4, 3,  5);
        Thread.sleep(60_000);
        ServerManager.getInstance().stop();
    }
}
