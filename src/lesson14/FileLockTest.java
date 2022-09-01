package lesson14;

import java.io.File;
import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;

public class FileLockTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final File	f = new File("c:/temp/lock.txt");
		
		try(final FileChannel	fc = FileChannel.open(f.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)) {
			
			try(final FileLock lock = fc.lock(0, 2, false)) {
				// TODO:
				System.err.println("Modification");
			}
			
			try(final FileLock lock = fc.lock(0, 2, true)) {
				// TODO:
				System.err.println("Read-only");
			}
		}
	}

}
