package client;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Creates a new instance of the text panel for the client program.
 * 
 * @author Mason
 *
 */

public class ClientTextPanel extends JPanel {

	// Class Objects
	JTextArea text;
	
	/**
	 * Constructor for new instances of ClientTextPanel. Creates and adds the JLabel for the
	 * incoming text from the server.
	 */
	
	public ClientTextPanel() {
		text = new JTextArea();
		
		this.add(text);
	}
	
	// Getters
	
	public JTextArea getTextArea() {
		return text;
	}
	
}
