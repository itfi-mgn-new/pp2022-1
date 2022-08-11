package lesson8;

public class DijkstraTest {
	public static final Object	stick1 = new Object();
	public static final Object	stick2 = new Object();
	public static final Object	stick3 = new Object();
	public static final Object	stick4 = new Object();
	public static final Object	stick5 = new Object();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final Thread	t1 = new Thread(()->{
							for (;;) {
								synchronized(stick1) {
									synchronized(stick2) {
										eat();
									}
								}
								think();
							}
						});
		t1.setName("A");
		t1.start();
		
		final Thread	t2 = new Thread(()->{
							for (;;) {
								synchronized(stick2) {
									synchronized(stick3) {
										eat();
									}
								}
								think();
							}
						});
		t2.setName("B");
		t2.start();
		
		final Thread	t3 = new Thread(()->{
							for (;;) {
								synchronized(stick3) {
									synchronized(stick4) {
										eat();
									}
								}
								think();
							}
						});
		t3.setName("C");
		t3.start();
		
		final Thread	t4 = new Thread(()->{
							for (;;) {
								synchronized(stick4) {
									synchronized(stick5) {
										eat();
									}
								}
								think();
							}
						});
		t4.setName("D");
		t4.start();

		final Thread	t5 = new Thread(()->{
					for (;;) {
						synchronized(stick1) {
							synchronized(stick5) {
								eat();
							}
						}
						think();
					}
				});
		t5.setName("E");
		t5.start();
	}

	static void eat() {
		System.err.println(Thread.currentThread().getName()+" eat");
//		try {
//			Thread.sleep(1000);
//		} catch (InterruptedException e) {
//		}
	}

	static void think() {
		System.err.println(Thread.currentThread().getName()+" think");
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//		}
	}

}
