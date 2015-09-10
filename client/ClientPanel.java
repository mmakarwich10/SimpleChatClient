package client;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Creates a new instance of ClientPanel. Shows which port the client and server are
 * connected on.
 * 
 * @author Mason
 *
 */

public class ClientPanel extends JPanel {

	// Class Objects
	JLabel first;
	
	/**
	 * Constructor for instances of ClientPanel. Creates and adds a label to display the
	 * port number.
	 * 
	 * @param port
	 */
	
	public ClientPanel(int port) {
		first = new JLabel("Connected to server on port " + port);
		
		this.add(first, BorderLayout.LINE_START);
	}
	
}
