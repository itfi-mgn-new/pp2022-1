package lesson10;

public class NotifiWaitTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Object	x = new Object();
		Thread			t = new Thread(()->{
//								try {
//									Thread.sleep(1000);
//								} catch (InterruptedException e) {
//								}
								synchronized(x) {
									x.notify();
									//kjhjhj
								}
							});
		t.start();
		Thread.sleep(100);
		synchronized(x) {
			// }
			x.wait();
			// synchronized(x) {
		}
		System.err.println("exit");
		
	}

}
