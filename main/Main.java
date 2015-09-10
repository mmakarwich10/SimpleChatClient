package server;

/**
 * Driver class for the server program.
 * 
 */

public class Main {
	
	/**
	 * Creates a new ServerController object and listens for a client creation or termination.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		ServerController server = new ServerController();
		server.listen();

	}

}
