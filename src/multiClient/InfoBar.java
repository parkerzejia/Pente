package multiClient;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * This class, which will extend JPanel,
 * holds & displays relevant information pertaining to a game instance.
 * 
 * This class will definitely hold white and black capture counts. Could also hold a Timer.
 */
public class InfoBar extends JPanel{
	/**
	 */
	public JPanel infoBarContent;

	/**
	 * This JPanel represents the panel in the center of the InfoBar.
	 */
	public JPanel infoBar;
	
	/**
	 * This label display the status of the game.
	 */
	private JLabel gameStatus;
	
	/**
	 * These labels display the number of white and black captures.
	 */
	private JLabel whiteCapsLabel;
	private JLabel blackCapsLabel;

	/**
	 * Default constructor for this class. Calls the init() method to build.
	 */
	public InfoBar() {
		init();
	}

	/**
	 * builds this component, visually.
	 */
	public void init() {

		infoBarContent = new JPanel();
		infoBarContent.setLayout(new BorderLayout());
		infoBarContent.setBorder(new EmptyBorder(5,5,5,5));
		infoBarContent.setBackground(Color.LIGHT_GRAY);
		
		gameStatus = new JLabel("Welcome to Pente!");
		gameStatus.setFont(new Font("Tahoma",Font.PLAIN,20));
		gameStatus.setHorizontalAlignment(JLabel.CENTER);
		infoBarContent.add(gameStatus, BorderLayout.NORTH);

		infoBar = new JPanel();
		infoBarContent.add(infoBar, BorderLayout.CENTER);

		whiteCapsLabel = new JLabel("White Captures: 0");
		whiteCapsLabel.setFont(new Font("Tahoma",Font.PLAIN,18));
		whiteCapsLabel.setHorizontalAlignment(JLabel.LEFT);
		infoBar.add(whiteCapsLabel);
	
		blackCapsLabel = new JLabel("Black Captures: 0");
		blackCapsLabel.setFont(new Font("Tahoma",Font.PLAIN,18));
		blackCapsLabel.setHorizontalAlignment(JLabel.RIGHT);
		infoBar.add(blackCapsLabel);

		this.add(infoBarContent);
	}

	/**
	 * Setter for whiteCaps in the JLabel.
	 * @param wc - new value.
	 */
	public void setWhiteCaps(int wc) {
		String whiteCapsMessage = "White Captures: " + wc;		
		whiteCapsLabel.setText(whiteCapsMessage);
	}

	/**
	 * Setter for blackCaps in the JLabel.
	 * @param bc - new value.
	 */
	public void setBlackCaps(int bc) {
		String blackCapsMessage = "Black Captures: " + bc;		
		blackCapsLabel.setText(blackCapsMessage);
	}
	
	/**
	 * Setter for this InfoBar's message.
	 * @param message - to be displayed.
	 */
	public void setMessage(String message) {
		gameStatus.setText(message);
	}

}
