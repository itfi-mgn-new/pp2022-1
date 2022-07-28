package lesson4;

import java.util.Arrays;

public class MapReduceTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final double[]	content = new double[100_000];
		
		for (int index = 0; index < content.length; index++) {
			content[index] = Math.random();
		}
		final long	start = System.currentTimeMillis();
	
		System.err.println("Proc="+Runtime.getRuntime().availableProcessors());
		
		final Thread[]	t = new Thread[4];
		
		for (int index = 0; index < t.length; index++) {
			final int	from = index * 25_000;
			final int	to = (index + 1) * 25_000;
			
			t[index] = new Thread(()->{
				bubbleSort(content, from, to);
			});
		}
		System.err.println("Started...");
		for (Thread item : t) {
			item.start();
		}
		for (Thread item : t) {
			item.join();
		}
	
//		Arrays.sort(content);
//		System.err.println("Started...");
//		bubbleSort(content, 0, content.length);
		System.err.println("Duration="+(System.currentTimeMillis() - start));
		
		
	}

	private static void bubbleSort(final double[] content, final int from, final int to) {
		boolean	needContinue = true;
		
		while (needContinue) {
			needContinue = false;
			for (int index = from; index < to-1; index++) {
				if (content[index+1] < content[index]) {
					double temp = content[index];
					content[index] = content[index+1];
					content[index+1] = temp;
					needContinue = true;
				}
			}
		}
	}
}
