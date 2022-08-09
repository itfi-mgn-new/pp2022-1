package lesson7;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class QueueTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final BlockingQueue<String>	q = new ArrayBlockingQueue<>(100);	
		
		final Thread	t = new Thread(()-> {
							try{while (!Thread.interrupted()) {
									final String	s = q.take();
									System.err.println("S="+s);
								}
							} catch (InterruptedException e) {
								System.err.println("Stop...");
							}
						});
		t.start();
		
		q.put("assa");
		q.put("1111assa");
		System.err.println("Started");
		Thread.sleep(2000);
		t.interrupt();
	}

}
