package lesson2;

public class ThreadTest {
	public static volatile int	value = 0;
	
	public static Object	obj = new Object();
	
	public static void main(String[] args) throws InterruptedException {
//		synchronized(ThreadTest.class) {
			final Thread[]	content = new Thread[20];
			
			for (int index = 0; index < content.length; index++) {
				content[index] = new Thread(()->{
					for (int i = 0; i < 1000; i++) {
						// try { monitorenter
						synchronized(obj) { 
							value++;
						} //} finally {monitorexit}
					}
					System.err.println(Thread.currentThread().getName());
				});
			}
			for (Thread t : content) {
				t.start();
			}
			System.err.println("Wait...");
			for (Thread t: content) {
				t.join();
			}
			System.err.println("Completed: "+value);
//		}
	}

}
