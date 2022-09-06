package lesson15;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class WatcherTest {

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		try(final WatchService 	watchService = FileSystems.getDefault().newWatchService()) {
			final Path 			path = Paths.get("c:/tmp");
	
			path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
	
			WatchKey 			key;
			
			while ((key = watchService.take()) != null) {
	           for (WatchEvent<?> event : key.pollEvents()) {
	        	   System.err.println("Type: "+event.kind().name()+", content="+event.context()+" as "+event.kind().getClass());
	           }
	           key.reset();
			}
		}
	}
}
