package lesson7;

import java.util.concurrent.Exchanger;

public class ExchangerTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Exchanger<String>	ex = new Exchanger<>();
		
		Thread	t = new Thread(()-> {
			try {
				System.err.println(ex.exchange("assa"));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		t.start();
		
		System.out.println(ex.exchange("123457"));
	}
}
