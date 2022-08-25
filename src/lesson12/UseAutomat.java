package lesson12;

import java.io.File;
import java.io.IOException;

public class UseAutomat {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		final AsyncAuto	aa = new AsyncAuto(new File("c:/temp/source.txt"), new File("c:/temp/destination.txt"));
	
		aa.automat(AsyncAuto.TERM_START);
		
		Thread.sleep(5000);
		aa.waitCompletion();
		System.err.println("Completed");
	}

}
