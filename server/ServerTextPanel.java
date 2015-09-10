package server;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Creates an instance of the server panel that contains the text sent over from the client.
 * 
 * @author Mason
 *
 */

public class ServerTextPanel extends JPanel {

	// Class Objects
	private JTextArea text;
	
	/**
	 * Constructor for instances of ServerTextPanel. Creates an empty JLabel to prepare for
	 * incoming text from the client.
	 */
	
	public ServerTextPanel() {
		text = new JTextArea();
		
		this.add(text);
	}
	
	// Getters
	
	public JTextArea getTextArea() {
		return text;
	}
	
}
