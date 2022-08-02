package lesson5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class ThreadPoolTest {

	public static void main(String[] args) throws InterruptedException {
		final ExecutorService ex = Executors.newSingleThreadExecutor(
//								new ThreadFactory() {
//									
//									@Override
//									public Thread newThread(Runnable r) {
//										final Thread t = new Thread(r);
//	
//										t.setDaemon(true);
//										t.setName("sdsdsd");
//										return t;
//									}
//								}
							);
		
		Future	f = ex.submit((Callable)()->{
					System.out.println("00000");
					throw new NullPointerException();
		//			return "sss";
				});
		System.err.println("Started");
		Thread.sleep(2000);
		
		try {
			final Object result = f.get();
			
			System.err.println("REsult="+result);
		} catch (ExecutionException e) {
			e.getCause().printStackTrace();
	//		e.printStackTrace();
		}
		ex.shutdown();
	}

}
