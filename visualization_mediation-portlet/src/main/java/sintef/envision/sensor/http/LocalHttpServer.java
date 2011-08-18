package sintef.envision.sensor.http;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;


/**
 * HTTP server that accepts connections to the local host.
 * 
 * @author Thomas Everding
 *
 */
public class LocalHttpServer {
	
	private HttpServer server;
	
	private int port;
	
	
	/**
	 * 
	 * Constructor
	 *
	 * @param port the port for the server
	 */
	public LocalHttpServer(int port) {
		this.port = port;
		
	}


	/**
	 * initializes and starts the server
	 */
	public void initialize() {
		try {
			//creates a new server at localhost:port
			this.server = HttpServer.create(new InetSocketAddress(this.port), 0);
			
			//create a reader for incoming messages
			server.createContext("/", new HttpReader()); 
		
			server.setExecutor(null);//use default
			server.start();
		}
		catch (IOException e) {
			System.out.println("error while building server");
			//e.printStackTrace();
		}
	}
	
	public void stopserver()
	{
		if(server!=null)
			server.stop(0);
	}
//	
//	
//	/**
//	 * test main
//	 * @param args a
//	 */
//	public static void main(String[] args) {
//		new LocalHttpServer(8082).initialize();
//	}

}
