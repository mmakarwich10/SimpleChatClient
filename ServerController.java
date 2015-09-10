package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;

/**
 * Creates a new instance of ServerController. Controls both the GUI for the server and the
 * data coming from the client.
 * 
 * @author Mason
 *
 */

public class ServerController {

	// Class Objects
	protected ServerFrame frame; // server GUI
	protected ServerSocket socket; // a socket for incoming data
	protected ArrayList<ClientWorker> list; // list of client workers
	protected ServerListener listener; // listener for incoming clients
	
	// Class Variables
	protected boolean shouldQuit = false; // indicates whether the server should quit or not.
	
	// Constants
	final int PORT = 60534;
	
	/**
	 * Constructor for new instances of ServerController. Creates a new ServerSocket and
	 * incoming client listener.
	 * 
	 */
	
	public ServerController() {
		
		/*
		 * Creates a new incoming client listener
		 */
		
		listener = new ServerListener();
		
		/*
		 * Creates a new server socket.
		 */
		
		try {
			socket = new ServerSocket(PORT);
		} catch(IOException e) {
			System.out.println("Could not listen on port " + PORT);
			System.exit(-1);
		}
		
		/*
		 * Creates the frame for the GUI
		 */
		
		frame = new ServerFrame(PORT);
	}
	
	/**
	 * Listens for incoming clients and determines when all clients have been terminated.
	 * 
	 */
	
	public void listen() {
		
		/*
		 * Creates a thread for the incoming client listener, so as to run it and the
		 * final terminating client listener at the same time.
		 */
		
		Thread connectionListenerThread = new Thread(listener);
		connectionListenerThread.start();
		
		/*
		 * Determines when all clients have been terminated.
		 */
		
		while(true) {
			
			/*
			 * If the last client has terminated, create a termination socket.
			 */
			
			if(shouldQuit) {
				
				/*
				 * This termination socket is created because, otherwise, the incoming
				 * client listener would run forever.
				 */
				
				try {
					Socket sock = new Socket("localhost", PORT);
					PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
					out.println("HELO <quit> ");
				} catch(UnknownHostException e) {
					System.out.println("Unknown Host localhost");
					System.exit(1);
				} catch(IOException f) {
					System.out.println("No I/O");
					System.exit(1);
				}
				System.exit(0);
			} 
			System.out.println(" ");
		}
	}
	
	/**
	 * Creates a new instance of ClientWorker. A separate thread that controls and keeps
	 * track of a client's data.
	 * 
	 * @author Mason
	 *
	 */
	
	public class ClientWorker implements Runnable {

		// Class Objects
		Socket client; // socket for outgoing data
		BufferedReader in; // input
		PrintWriter out; // output
		Thread thread; // separate thread
		
		// Class variables
		String clientUsername; // the username the client user chose
		
		/**
		 * Constructor for instances of ClientWorker. Creates a new thread for the client
		 * worker and an I/O stream. Then does its part of the handshake protocol.
		 * 
		 * @param client the client object the client worker is associated with
		 */
		
		public ClientWorker(Socket client) {
			
			/*
			 * Creates a new thread for the client worker.
			 */
			
			thread = new Thread(this);
			
			this.client = client;
			
			/*
			 * Establishes an I/O stream.
			 */
			
			try {
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				out = new PrintWriter(client.getOutputStream(), true);
			} catch(IOException e) {
				System.out.println("No I/O");
				System.exit(1);
			}
			
			/*
			 * Server's part of the handshake protocol.
			 */
			
			String line = "";
			
			/*
			 * Reads the incoming string.
			 */
			
			try {
				line = in.readLine();
			} catch(IOException h) {
				System.out.println("Read Failed");
				System.exit(-1);
			}
			
			/*
			 * Parsing the string.
			 */
			
			String[] input = line.split("<");
			String[] input2 = input[1].split(">");
			Pattern p = Pattern.compile("[a-z]+");
			Matcher m = p.matcher(input2[0]);
			
			/*
			 * If the string follows the protocol, send a confirmation back.
			 */
			
			if(m.find() && input[0].equals("HELO ") && input2[1].equals(" ")) {
				frame.getTextPanel().getTextArea().setText(frame.getTextPanel().getTextArea().getText() + line + "\n\n");
				out.println("HOLA <" + input2[0] + "> \n\n");
				clientUsername = input2[0];
				
			/*
			 * If not, inform both the server and the client that there was an error.
			 */
				
			} else {
				System.out.println("ERROR BAD HANDSHAKE");
				out.println("ERROR BAD HANDSHAKE\n\n");
				frame.getTextPanel().getTextArea().setText("ERROR BAD HANDSHAKE\n\n");
			}
		}
		
		/**
		 * The "main" (driver) method of a client worker. Reads incoming messages from its
		 * designated client, determines if they are valid messages (i.e. they follow
		 * protocol), and if so, sends them to the other clients.
		 */
		
		@Override
		public void run() {
			String line = "";
			
			/*
			 * Creates an infinite loop that listens for client data.
			 */
			
			while(true) {
				
				/*
				 * Attempts to read in the incoming client data.
				 */
				
				try {
					line = in.readLine();
				} catch(IOException h) {
					System.out.println("Read Failed");
					System.exit(-1);
				}
				
				/*
				 * If successfully read, then the program checks to see if the string is null
				 * or an empty string. If not, it decides whether it is a literal text
				 * string, or a termination string.
				 */
				
				if(line != null && !line.equals("")) {
					
					String[] input = line.split("-");
					
					/*
					 * Check the string to make sure it's following protocol.
					 */
					
					if(input[0].equals("MSG") && Integer.parseInt(input[1]) == input[2].length()) {
						
						/*
						 * If the data is a termination string, then decide whether just the
						 * client should be closed, or both client and server (if the client
						 * is the last client existing).
						 */
						
						if(input[2].equals("quit")) {
							
							/*
							 * If it is the last client, terminate both client and server.
							 */
							
							if(list.size() == 1) {
								shouldQuit = true;
							}
							
							/*
							 * Otherwise, tell other clients that this client is leaving,
							 * remove it from the list and terminate the client.
							 */
							
							for(ClientWorker worker : list) {
								if(!worker.equals(this)) {
									worker.out.println("BYEBYE-" + clientUsername);
								}
							}
							
							list.remove(this);
							out.println("ACK-4-quit");
							
						/*
						 * Otherwise, print the string on the text panel of the server GUI and send the
						 * message to the other clients.
						 */
							
						} else {
							pasteText(input[0], input[1], input[2]);
							for(ClientWorker worker : list) {
								if(!worker.equals(this)) {
									worker.out.println("REMSG-" + input[1] + "-" + clientUsername + "-" + input[2]);
								}
							}
						}
						
					/*
					 * If the message does not follow protocol, inform both client and
					 * server.
					 */
						
					} else {
						System.out.println("ERROR BAD MESSAGE");
						out.println("ERROR BAD MESSAGE\n\n");
						frame.getTextPanel().getTextArea().setText("ERROR BAD MESSAGE\n\n");
					}
				}
			}	
		}
		
		/**
		 * Sends an acknowledgment back to the client that the message was received and
		 * prints the message on the server.
		 * 
		 * @param part1
		 * @param part2
		 * @param part3
		 */
		
		public synchronized void pasteText(String part1, String part2, String part3) {
			out.println("ACK-" + part2 + "-" + part3);
			frame.getTextPanel().getTextArea().setText(frame.getTextPanel().getTextArea().getText() + part1 + " " + part2 + "\n" + part3 + "\n\n");
		}
		
		/**
		 * Starts the thread.
		 */
		
		public void start() {
			thread.start();
		}
	}
	
	/**
	 * Creates a new instance of ServerListener. A listener for incoming clients.
	 * 
	 * @author Mason
	 *
	 */
	
	public class ServerListener implements Runnable {
		
		/**
		 * Constructor for instances of ServerListener. Creates a list of client workers.
		 */
		
		public ServerListener() {
			list = new ArrayList<ClientWorker>();
		}
		
		/**
		 * The "main" (driver) method of the ServerListener. Runs a check to see if all the
		 * clients have been terminated, and if not, waits for a client to accept. Then
		 * it creates a new client worker, assigning the accepting client to it. Then starts
		 * the thread. If all clients have been terminated, then terminate the thread.
		 */
		
		@Override
		public void run() {
			try {
				while (!shouldQuit) {
					ClientWorker user = new ClientWorker(socket.accept());
					list.add(user);
					user.start();
				}
			} catch(IOException f) {
				System.out.println("Accept Failed: " + PORT);
				System.exit(-1);
			}
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Could not close socket");
				System.exit(1);
			}
			System.exit(0);
		}
	}
}
