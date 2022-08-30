package lesson13;

import java.io.IOException;

public class EchoTest {
	static Process server;
	static EchoClient client;

    public static void setup() throws IOException, InterruptedException {
        server = EchoServer.start();
        client = EchoClient.start();
    }

    public static void sendAndReceive() {
        String resp1 = client.sendMessage("hello");
        System.err.println("1: "+resp1);
        String resp2 = client.sendMessage("world");
        System.err.println("2: "+resp2);
    }

    public static void teardown() throws IOException {
        server.destroy();
        EchoClient.stop();
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	setup();
    	Thread.sleep(1000);
    	sendAndReceive();
    	teardown();
    }
}
