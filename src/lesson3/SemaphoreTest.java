package lesson3;

import java.util.concurrent.Semaphore;

public class SemaphoreTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Thread[]	t = new Thread[10];
		final Semaphore	sema = new Semaphore(5);

		for (int index = 0; index < t.length; index++) {
			t[index] = new Thread(()->{
				try {
					for (int count = 0; count < 100; count++) {
						Thread.sleep((long)(100+100*Math.random()));
//						synchronized(x) {
						try{sema.acquire();
							Thread.sleep((long)(100+100*Math.random()));
						} finally {
							sema.release();
						}
//						}
					}
				} catch (InterruptedException exc) {
					
				}
			});		
		}
		for (Thread item : t) {
			item.start();
		}
		long	start = System.currentTimeMillis();
		System.err.println("Started...");
		for (Thread item : t) {
			item.join();
		}
		System.err.println("Duration: "+(System.currentTimeMillis()-start));
	}

}
