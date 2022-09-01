package lesson14;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class PassContent {
	private enum Terminals {
		TERM_CONNECT,
		TERM_READ_READY,
		TERM_READ_EMPTY,
		TERM_WRITE_COMPLETED;
	}

	@FunctionalInterface
	private interface Callback {
		void process(Selector sel, ServerSocketChannel server, SocketChannel ch) throws IOException;
	}

	static int						state = 0;
    static SocketChannel			first, second;
	
	private static final Rule[]		RULES = {
			new Rule(0, Terminals.TERM_CONNECT, 1, (sel,server,ch)->registerFirst(sel, server)),
			new Rule(1, Terminals.TERM_CONNECT, 2, (sel,server,ch)->registerSecond(sel, server)),
			new Rule(1, Terminals.TERM_READ_READY, 10, (sel,server,ch)->read(ch)),
			new Rule(2, Terminals.TERM_READ_READY, 3, (sel,server,ch)->{read(ch); write(second);}),
			new Rule(3, Terminals.TERM_READ_EMPTY, 2, (sel,server,ch)->{}),
			new Rule(3, Terminals.TERM_WRITE_COMPLETED, 2, (sel,server,ch)->{}),
			new Rule(3, Terminals.TERM_READ_READY, 4, (sel,server,ch)->read(ch)),
			new Rule(4, Terminals.TERM_READ_EMPTY, 4, (sel,server,ch)->{}),
			new Rule(4, Terminals.TERM_WRITE_COMPLETED, 3, (sel,server,ch)->write(ch)),
			new Rule(10, Terminals.TERM_CONNECT, 3, (sel,server,ch)->{registerSecond(sel, server); write(second);}),
		};
	
	static final CountDownLatch		latch = new CountDownLatch(2);
    static final ByteBuffer 		buffer = ByteBuffer.allocate(256);
	
	
	
	public static void main(String[] args) throws InterruptedException {
		final Thread	t1 = new Thread(()->runServer());
		final Thread	t2 = new Thread(()->runSource());
		final Thread	t3 = new Thread(()->runTarget());
		
		t1.setDaemon(true);
		t1.start();
		Thread.sleep(1000);
		t2.setDaemon(true);
		t2.start();
		Thread.sleep(1000);
		t3.setDaemon(true);
		t3.start();
		
		latch.await();
		System.err.println("Completed");
	}

	private static void runServer() {
        try(final Selector selector = Selector.open();
        	final ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
        
	        serverSocket.bind(new InetSocketAddress("localhost", 5454));
	        serverSocket.configureBlocking(false);
	        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
	
	        while (true) {
	            final int event = selector.select();
	
	            final Set<SelectionKey> selectedKeys = selector.selectedKeys();
	            
	            for (SelectionKey key : selectedKeys) {
	                if (key.isValid()) {
		                if (key.isAcceptable()) {
		                    automat(Terminals.TERM_CONNECT, selector, serverSocket, null);
		                }
		                else if (key.isReadable()) {
		                    automat(Terminals.TERM_READ_READY, selector, serverSocket, (SocketChannel)key.channel());
		                }
		                else if (key.isWritable()) {
		                    automat(Terminals.TERM_WRITE_COMPLETED, selector, serverSocket, (SocketChannel)key.channel());
		                }
	                }
	            }
	            selectedKeys.clear();
	        }
        } catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void automat(final Terminals terminal, final Selector selector, final ServerSocketChannel channel, final SocketChannel ch) {
		final int oldState = state;
		
		for (Rule item : RULES) {
			if (item.state == state && item.terminal == terminal) {
				state = item.newState;
				try{item.action.process(selector, channel, ch);
				} catch (IOException e) {
					e.printStackTrace();
				}
//				System.err.println("State="+oldState+",ternminal="+terminal+",newState="+state);
				return;
			}
		}
//		System.err.println("Failed State="+oldState+",ternminal="+terminal);
	}
	
	private static void runSource() {
        try(final SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454))) {
        	
        	client.configureBlocking(true);
			for (int index = 0; index < 10000; index++) {
				final String		msg = "Message "+index;
		        final ByteBuffer	buffer = ByteBuffer.wrap(msg.getBytes());
	            
		        client.write(buffer);
		        if (index % 1000 == 0) {
		        	Thread.sleep(100);
		        }
//		        System.err.println("Sent "+msg);
			}
        } catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} finally {
        	latch.countDown();
        }
	}

	private static void runTarget() {
        try(final SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 5454))) {

        	client.configureBlocking(true);
			for (int index = 0; index < 10000; index++) {
		        final ByteBuffer	buffer = ByteBuffer.allocate(256);
	            
		        client.read(buffer);
		        System.out.println("Recvd <"+new String(buffer.array(),0,buffer.position())+">");
			}
        } catch (IOException e) {
			e.printStackTrace();
		} finally {
        	latch.countDown();
        }
	}

	static void registerFirst(final Selector selector, final ServerSocketChannel channel) throws IOException {
        final SocketChannel client = channel.accept();
        
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        first = client;
        System.err.println("First registered");
	}
	
	static void registerSecond(final Selector selector, final ServerSocketChannel channel) throws IOException {
        final SocketChannel client = channel.accept();
        
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_WRITE);
        second = client;
        System.err.println("Second registered");
	}

	static void read(SocketChannel ch) throws IOException {
		ch.read(buffer);
//        System.err.println("Buffer read, size="+buffer.position()+", content=<"+new String(buffer.array(),0,buffer.position())+">");

        if (buffer.position() == 0) {
        	automat(Terminals.TERM_READ_EMPTY, null, null, ch);
//            System.err.println("Read empty...");
        }
	}

	
	static void write(SocketChannel ch) throws IOException {
        buffer.flip();
		second.write(buffer);
//        System.err.println("Buffer write, size="+buffer.limit());
		buffer.clear();
//        System.err.println("Transfer completed");
	}
	
	
	static class Rule {
		final int		state;
		final Terminals	terminal;
		final int		newState;
		final Callback 	action;

		public Rule(final int state, final Terminals terminal, final int newState, final Callback action) {
			this.state = state;
			this.terminal = terminal;
			this.newState = newState;
			this.action = action;
		}

		@Override
		public String toString() {
			return "Rule [state=" + state + ", terminal=" + terminal + ", newState=" + newState + "]";
		}
	}
}
