package client;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Creates a new instance of the bottom panel of the client program. Contains the text box
 * and the send button.
 * 
 * @author Mason
 *
 */

public class ClientPanelBottom extends JPanel {

	// Class Objects
	private JTextField textbox;
	private JButton send;
	
	/**
	 * Constructor for new instances of ClientPanelBottom. Creates, formats and adds the
	 * text box and send button.
	 */
	
	public ClientPanelBottom() {
		
		textbox = new JTextField();
		send = new JButton("Send");
		
		textbox.setPreferredSize(new Dimension(200, 20));
		
		this.add(textbox);
		this.add(send);
	}
	
	// Getters
	
	public JButton getButton() {
		return send;
	}
	
	public JTextField getTextbox() {
		return textbox;
	}
}
