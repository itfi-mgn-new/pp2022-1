package lesson6;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class MultiPoolTest {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		final ExecutorService first = Executors.newFixedThreadPool(2);
		final ExecutorService second = Executors.newFixedThreadPool(5);
		final BlockingQueue<Future<Integer>> q = new ArrayBlockingQueue<>(1000);
		final Semaphore	sema = new Semaphore(1);
//		final CountDownLatch cdl = new CountDownLatch(100);
		
		int	count = 0;
		
		
		for (int index = 0; index< 100; index++) {
			final int temp = index;
		
			first.submit(()->{
				try{
					final int temp1 = step1(temp);
					
					q.put(second.submit(()->{
							try{final int	temp2 = step2(temp1);
							
								System.err.println("Val="+temp2);
								return temp2;
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								return 0;
							} finally {
								sema.release();
//								cdl.countDown();
							}
						})
					);
					return null;
				} catch (InterruptedException e) {
					e.printStackTrace();
					return null;
				}
			});
		}

		System.err.println("Start");
		sema.acquire(100);
//		cdl.await();
		System.err.println("End");
		
//		while(count < 100) {
//			final Future<Integer> f = q.poll();
//
//			if (f != null) {
//				System.err.println("REad: "+f.get());
//				count++;
//			}
//		}
		first.shutdown();
		second.shutdown();
	}

	static int step1(int value) throws InterruptedException {
		Thread.sleep(2000);
		return -value;
	}
	
	static int step2(int value) throws InterruptedException {
		Thread.sleep(5000);
		return 2*value;
	}
}
