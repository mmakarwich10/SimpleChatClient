package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * Creates a new instance of ClientController. Controls both the GUI for the client and the
 * data going in from, and out to, the server.
 * 
 * @author Mason
 *
 */

public class ClientController {

	// Class Objects
	protected ClientFrame frame; // client GUI
	protected Socket socket; // connection to server
	protected BufferedReader in; // input
	protected PrintWriter out; // output
	protected ButtonListener hey; // listens for the button press.
	protected KeyListener key; // listens for the enter key
	
	// Class Variables
	protected String username;
	
	// Constants
	final int PORT = 60534;
	
	/**
	 * Constructor for instances of ClientController. Opens a socket to the server and 
	 * establishes an I/O stream. Creates a frame for the client. Initializes an action
	 * listener for the send button and a key listener. Grabs a user-inputed username, and
	 * begins the handshake protocol.
	 */
	
	public ClientController() {
		
		/*
		 * Opens a socket to the server and establishes an I/O stream.
		 */
		
		try {
			socket = new Socket("localhost", PORT);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch(UnknownHostException e) {
			System.out.println("Unknown Host localhost.");
			System.exit(1);
		} catch(IOException e) {
			System.out.println("No I/O");
			System.exit(1);
		}
		
		/*
		 * Creates a frame for the client.
		 */
		
		frame = new ClientFrame(PORT);
		
		/*
		 * Grabs a username from the user.
		 */
		
		username = (String) JOptionPane.showInputDialog(frame, "Enter a username with at least one lower-case letter.", "Enter a Username...", JOptionPane.PLAIN_MESSAGE, null, null, null);
		
		/*
		 * Begins the handshake protocol.
		 */
		
		out.println("HELO <" + username + "> \n\n");
		
		/*
		 * Initializes button and key listeners, then adds them to the frame.
		 */
		
		hey = new ButtonListener();
		key = new ConfirmListener();
		
		frame.getEntryButtonPanel().getButton().addActionListener(hey);
		frame.getEntryButtonPanel().getTextbox().addKeyListener(key);
		
	}
	
	/**
	 * Listens for data from the server.
	 */
	
	public void listen() {
		String line = "";
		
		/*
		 * Waits for input.
		 */
		
		while(true) {
			
			/*
			 * Attempts to read incoming data.
			 */
			
			try {
				line = in.readLine();
			} catch(IOException h) {
				System.out.println("Read Failed");
				System.exit(-1);
			}
			
			/*
			 * Determines if the input string is null or empty. If not, it tries to
			 * determine if the string is a termination keyword or a literal string.
			 */
			
			if(line != null && !line.equals("")) {
				
				String[] input = line.split("-");
				
				/*
				 * Determines if a sent message follows protocol.
				 */
				
				if(input[0].equals("ACK") && Integer.parseInt(input[1]) == input[2].length()) {
					
					/*
					 * If the string is a termination keyword, then the socket closes and the
					 * program terminates.
					 */
					
					if(input[2].equals("quit")) {
						try {
							socket.close();
							System.exit(0);
						} catch (IOException e) {
							System.out.println("Could not close socket");
							System.exit(-1);
						}
						
					/*
					 * Otherwise, print out the string on the client GUI.	
					 */
					
					} else {
						System.out.println(input[0] + " " + input[1] + "\n" + input[2] + "\n");
						frame.getTextPanel().getTextArea().setText(frame.getTextPanel().getTextArea().getText() + "\n<" + username + "> " + input[2]);
					}
					
				/*
				 * Determines if a message sent from another client follows protocol.
				 */
					
				} else if(input[0].equals("REMSG") && Integer.parseInt(input[1]) == input[3].length()) {
					System.out.println(input[0] + " " + input[1] + "\n" + input[2] + "\n" + input[3] + "\n");
					frame.getTextPanel().getTextArea().setText(frame.getTextPanel().getTextArea().getText() + "\n<" + input[2] + "> " + input[3]);
				
				/*
				 * Determines if another client's termination message follows protocol.
				 */
				
				} else if(input[0].equals("BYEBYE")) {
					System.out.println(input[0] + " " + input[1]);
					frame.getTextPanel().getTextArea().setText(frame.getTextPanel().getTextArea().getText() + "\n[<" + input[1] + "> has left the chatroom.]");
				
				/*
				 * if it doesn't follow protocol, notify both server and client.
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
	 * Last step of the handshake protocol. Check to make sure the second part of the 
	 * protocol worked correctly.
	 */
	
	public void confirmHandshake() {
		
		String line = "";
		boolean confirmed = false;
		
		/*
		 * Waits until input is received.
		 */
		
		while(confirmed == false) {
			
			try {
				line = in.readLine();
			} catch(IOException h) {
				System.out.println("Read Failed");
				System.exit(-1);
			}
			
			/*
			 * Determines if incoming string matches protocol.
			 */
			
			String[] input = line.split("<");
			String[] input2 = input[1].split(">");
			
			/*
			 * If so, notify the client.
			 */
			
			if(input2[0].equals(username) && input[0].equals("HOLA ") && input2[1].equals(" ")) {
				frame.getTextPanel().getTextArea().setText(line);
				confirmed = true;
			
			/*
			 * If not, notify both the client and the server.
			 */
			
			} else {
				System.out.println("ERROR BAD HANDSHAKE");
				out.println("ERROR BAD HANDSHAKE\n\n");
				frame.getTextPanel().getTextArea().setText("ERROR BAD HANDSHAKE\n\n");
			}
		}
	}
	
	/**
	 * Creates an instance of an action listener for a JButton.
	 * 
	 * @author Mason
	 *
	 */
	
	private class ButtonListener implements ActionListener {

		/**
		 * Listens for the send button and when clicked, sends the string over the socket
		 * to the server. Then resets the text box to blank.
		 */
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			if(arg0.getSource().equals(frame.getEntryButtonPanel().getButton())) {
				String text = frame.getEntryButtonPanel().getTextbox().getText();
				out.println("MSG-" + text.length() + "-" + text);
				frame.getEntryButtonPanel().getTextbox().setText("");
				frame.getEntryButtonPanel().getTextbox().setFocusable(true);
			}
			
		}
		
	}
	
	/**
	 * Creates an instance of a key listener for the enter key.
	 * 
	 * @author Mason
	 *
	 */
	
	private class ConfirmListener implements KeyListener {
		
		/**
		 * Waits for the enter key to be pressed, then does the same thing as the button.
		 */

		@Override
		public void keyPressed(KeyEvent arg0) {
			
			if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				String text = frame.getEntryButtonPanel().getTextbox().getText();
				out.println("MSG-" + text.length() + "-" + text);
				frame.getEntryButtonPanel().getTextbox().setText("");
				frame.getEntryButtonPanel().getTextbox().setFocusable(true);
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			
			
		}
		
	}
}
