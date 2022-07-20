package lesson1;

public class ThreadTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		Thread	t = new Thread(()->{
							while (!Thread.interrupted()) {
								try {
									Thread.sleep(2000);
									System.err.println("T "+Thread.currentThread().getName());
								} catch (InterruptedException exc) {
									
								}
							}
						});
		t.setName("ASSA");
		t.setDaemon(true);
		t.start();
		Thread.sleep(10000);
		t.interrupt();
		t.join();
	}

}
