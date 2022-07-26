package lesson3;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class LockTest {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		final Object					obj = new Object();
		final ReentrantReadWriteLock	rwlock = new ReentrantReadWriteLock();
		final Thread[]					t = new Thread[10];
		
		t[0] = new Thread(()->{
			WriteLock lock = rwlock.writeLock();
			try {
				for (int count = 0; count < 100; count++) {
					Thread.sleep((long)(100+100*Math.random()));
//					synchronized(obj) {
					try {lock.lock();
						Thread.sleep((long)(100+100*Math.random()));
					} finally {
						lock.unlock();
					}
//					}
				}
			} catch (InterruptedException exc) {

			}
		});
		for (int index = 1; index < t.length; index++) {
			t[index] = new Thread(()-> {
				ReadLock lock = rwlock.readLock();
				try {
					for (int count = 0; count < 100; count++) {
						Thread.sleep((long)(100+100*Math.random()));
//						synchronized(obj) {
						try{lock.lock();
							Thread.sleep((long)(100+100*Math.random()));
						} finally {
							lock.unlock();
						}
//						}
					}
				} catch (InterruptedException exc) {

				}
			});
		}
		for (Thread item : t) {
			item.start();
		}
		long	start = System.currentTimeMillis();
		System.err.println("Started...");
		for (Thread item : t) {
			item.join();
		}
		System.err.println("Duration: "+(System.currentTimeMillis()-start));
	}
}
