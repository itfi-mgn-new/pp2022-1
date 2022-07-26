package lesson3;

public class DistributeTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final int[]		content = new int[100_000_000];
		
		for (int index = 0; index < content.length; index++) {
			content[index] = index;
		}
		
		Thread	t1 = new Thread(()-> {
			for (int index = 0; index < content.length; index += 2) {
				content[index] = -content[index];
			}
		});
		Thread	t2 = new Thread(()-> {
			for (int index = 1; index < content.length; index += 2) {
				content[index] = -content[index];
			}
		});
		
		t1.start();
		t2.start();
//		for (int index = 0; index < content.length; index++) {
//			content[index] = -content[index];
//		}
		long	start = System.currentTimeMillis();
		System.err.print("Start...");
		t1.join();
		t2.join();
		System.err.println("Duration: "+(System.currentTimeMillis()-start));
	}

}
