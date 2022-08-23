package lesson11;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AsyncFileChannelTest {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		final Path	path = Paths.get(URI.create(AsyncFileChannelTest.class.getResource("./test.txt").toString()));
		
		try(final AsynchronousFileChannel 	fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ)) {
			final ByteBuffer 				buffer = ByteBuffer.allocate(10);
	
			System.err.println("Size="+fileChannel.size());
			
			final Future<Integer> 			read1 = fileChannel.read(buffer, 0);
			final int						len1 = read1.get();
			final String 					content1 = new String(buffer.array(), 0, len1);
			
			System.err.println("Content1="+content1);
			buffer.clear();
			
			fileChannel.read(buffer, 20, buffer, new CompletionHandler<Integer, ByteBuffer>() {
									        @Override
									        public void completed(final Integer readed, final ByteBuffer attachment) {
									    		System.err.println("Content2="+new String(attachment.array(), 0, readed));
									        }
									        
									        @Override
									        public void failed(Throwable exc, ByteBuffer attachment) {
									        	exc.printStackTrace();
									        }
									    });
			Thread.sleep(1000);
		}
	}

}
