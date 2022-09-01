package lesson14;

import java.io.File;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.StandardOpenOption;

public class MapTest {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		final File	f = new File("c:/temp/lock.txt");
		
		try(final FileChannel	fc = FileChannel.open(f.toPath(), StandardOpenOption.READ, StandardOpenOption.WRITE)) {
		
			MappedByteBuffer map = fc.map(MapMode.READ_WRITE, 0, 10);
			map.put(20, (byte)'a');
		}
	}

}
