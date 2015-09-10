package server;

import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;

/**
 * Creates a new instance of a server JFrame, then adds in all of the necessary panels. Is
 * the GUI for the server.
 * 
 * @author Mason
 *
 */

public class ServerFrame extends JFrame {

	// Class Objects
	private ServerPanel panel;
	private ServerTextPanel panel2;
	
	/**
	 * Constructor for instances of ServerFrame. Creates the ServerPanel and
	 * ServerTextPanel, adds them in, sets them up within the frame as well as setting up
	 * the frame itself.
	 * 
	 * @param port
	 */
	
	public ServerFrame(int port) {
		panel = new ServerPanel(port);
		panel2 = new ServerTextPanel();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().add(panel, BorderLayout.NORTH);
		this.getContentPane().add(panel2, BorderLayout.WEST);
		this.setSize(400, 400);
		this.setVisible(true);
		
		panel2.getTextArea().setBackground(panel2.getBackground());
		panel2.getTextArea().setEditable(false);
		
	}
	
	// Getters
	
	public ServerTextPanel getTextPanel() {
		return panel2;
	}

}
