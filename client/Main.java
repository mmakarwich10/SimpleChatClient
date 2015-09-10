package client;

/**
 * Driver class for the client program.
 * 
 * @author Mason
 *
 */

public class Main {

	/**
	 * Creates a new ClientController object and listens to the server.
	 * 
	 * @param args
	 */
	
	public static void main(String[] args) {
		ClientController client = new ClientController();
		client.confirmHandshake();
		client.listen();

	}

}
