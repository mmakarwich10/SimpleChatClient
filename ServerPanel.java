package server;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Creates a new instance of ServerPanel. This panel shows the user which port the server is
 * connect to with the client.
 * 
 * @author Mason
 *
 */

public class ServerPanel extends JPanel {

	// Class Objects
	JLabel first;
	
	/**
	 * Constructor for instances of ServerPanel. Creates the label that shows the port.
	 * 
	 * @param port
	 */
	
	public ServerPanel(int port) {
		first = new JLabel("Connected to port " + port);
		this.add(first, BorderLayout.LINE_START);
	}
}
