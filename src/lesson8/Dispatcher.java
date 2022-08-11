package lesson8;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Dispatcher {
	static BlockingQueue<Object> queue = new ArrayBlockingQueue<>(10);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for (;;) {
			processGetRequest(1,5);
			eat();
			processFreeRequest(1, 5);
			think();
		}
		
		final Thread t = new Thread(()->{
							while (!Thread.interrupted()) {
								Object item = queue.take();
								// process item.
							}
						});
		t.start();
	}

	static void processGetRequest(int left, int right) throws InterruptedException {
		final GetREquest e = new GetREquest(left, right);
		queue.put(e);
		e.completed.await();
	}
	
	static void processFreeRequest(int left, int right) throws InterruptedException {
		queue.put(new FreeRquest(left, right));
	}
	
	
	private static class GetREquest {
		int	leftStickNumber;
		int rigthStickNumber;
		CountDownLatch completed = new CountDownLatch(1);
		
		public GetREquest(int leftStickNumber, int rigthStickNumber) {
			this.leftStickNumber = leftStickNumber;
			this.rigthStickNumber = rigthStickNumber;
		}
	}
	
	private static class FreeRquest {
		int	leftStickNumber;
		int rigthStickNumber;

		public FreeRquest(int leftStickNumber, int rigthStickNumber) {
			this.leftStickNumber = leftStickNumber;
			this.rigthStickNumber = rigthStickNumber;
		}
	}
}
