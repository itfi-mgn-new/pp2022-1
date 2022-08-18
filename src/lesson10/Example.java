package lesson10;

public class Example {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Thread	t1 = new Thread(()->{
						try {
							System.err.println("EX1: "+exchange("1"));
						}
						catch (InterruptedException exc) {
							
						}
					});
		Thread	t2 = new Thread(()->{
						try {
							System.err.println("EX2: "+exchange("2"));
						}
						catch (InterruptedException exc) {
							
						}
					});
		t1.start();
		t2.start();
	}

	public static Object exchange(Object send) throws InterruptedException {
		// TODO: 
	}
}
