package lesson12;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CountDownLatch;

import lesson12.Automat.Rule;

public class AsyncAuto {
	public static final int	TERM_START = 0;
	public static final int	TERM_EOF = 1;
	public static final int	TERM_READED = 2;
	public static final int	TERM_WRITTEN = 3;
	
	private final Automat.Rule[]	RULES = {
		new Automat.Rule(0, TERM_START, 1, ()->startRead()),	
		new Automat.Rule(1, TERM_EOF, 2, ()->complete()),	
		new Automat.Rule(1, TERM_READED, 3, ()->startWrite()),	
		new Automat.Rule(3, TERM_WRITTEN, 1, ()->{increment(); startRead();}) ,	
	};
	
	final ByteBuffer 				buffer = ByteBuffer.allocate(1);
	final AsynchronousFileChannel	in;
	final AsynchronousFileChannel	out;
	final CountDownLatch			latch = new CountDownLatch(1);
	int								from = 0, to = 0;
	int								state = 0;
	
	public AsyncAuto(final File inFile, final File outFile) throws IOException {
		in = AsynchronousFileChannel.open(inFile.getAbsoluteFile().toPath(), StandardOpenOption.READ);
		out = AsynchronousFileChannel.open(outFile.getAbsoluteFile().toPath(), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
	}

	public void automat(final int terminal) {
		for (Rule item : RULES) {
			if (item.state == state && item.terminal == terminal) {
				state = item.newState;
				item.action.run();
				return;
			}
		}
	}
	
	public void waitCompletion() throws InterruptedException {
		latch.await();
	}
	
	private void complete() {
		try{
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}

	private void startRead() {
		buffer.clear();
		in.read(buffer, from, buffer, new CompletionHandler<Integer, ByteBuffer>() {
	        @Override
	        public void completed(final Integer readed, final ByteBuffer attachment) {
	        	if (readed == -1) {
	        		automat(TERM_EOF);
	        	}
	        	else {
	        		automat(TERM_READED);
	        	}
	        }
	        
	        @Override
	        public void failed(Throwable exc, ByteBuffer attachment) {
	        	exc.printStackTrace();
	        }
	    });		
	}
	
	private void startWrite() {
		buffer.flip();
		out.write(buffer, to, buffer, new CompletionHandler<Integer, ByteBuffer>() {
	        @Override
	        public void completed(final Integer readed, final ByteBuffer attachment) {
	       		automat(TERM_WRITTEN);
	        }
	        
	        @Override
	        public void failed(Throwable exc, ByteBuffer attachment) {
	        	exc.printStackTrace();
	        }
	    });		
	}
	
	private void increment() {
		from++;
		to++;
	}
}
