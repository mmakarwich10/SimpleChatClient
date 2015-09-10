package client;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 * Creates an instance of a JFrame that serves as the GUI for the client program.
 * 
 * @author Mason
 *
 */

public class ClientFrame extends JFrame {

	// Class Objects
	private ClientPanel panel;
	private ClientPanelBottom panel2;
	private ClientTextPanel panel3;
	
	/**
	 * Constructor for instances of ClientFrame. Initializes three panels, adds them in, and
	 * sets up the frame.
	 * 
	 * @param port
	 */
	
	public ClientFrame(int port) {
		panel = new ClientPanel(port); // shows which port the client and server are connected on
		panel2 = new ClientPanelBottom(); // holds the text box and send button
		panel3 = new ClientTextPanel(); // holds the text sent back from the server
		
		this.getContentPane().add(panel, BorderLayout.NORTH);
		this.getContentPane().add(panel2, BorderLayout.SOUTH);
		this.getContentPane().add(panel3, BorderLayout.WEST);
		
		panel3.getTextArea().setBackground(this.getBackground());
		panel3.getTextArea().setEditable(false);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 400);
		this.setVisible(true);
	}
	
	// Getters
	
	public ClientPanelBottom getEntryButtonPanel() {
		return panel2;
	}
	
	public ClientTextPanel getTextPanel() {
		return panel3;
	}
}
