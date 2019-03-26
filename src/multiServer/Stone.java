package multiServer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class holds every characteristic of a single pente stone. The stone is
 * required to be in 1 of 3 states: black, white, or unnoccupied
 */
public class Stone extends JPanel {

	public static final Color boardColor = new Color(175,133,40);

	/**
	 * enumeration representing the Tile status. 0 - unoccupied; 1 - white; 2 -
	 * black.
	 */
	private int status;

	/**
	 * Default constructor for Tile. Sets status to 0.
	 */
	public Stone () {

		super();
		setPreferredSize(new Dimension(40,40));
		setVisible(true);
		status = 0;
		repaint();
	}

	/**
	 * Getter for status.
	 */
	public int getStatus () {
		return status;
	}

	/**
	 * Setter for this Stone's status
	 * 
	 * @param status
	 *          : new integer value for this Stone. Must be between 0 & 2
	 *          inclusive.
	 */
	public void setStatus ( int value ) {
		status = value;
		repaint();
	}

	/**
	 * This gets called on repaint; paints this Stone according to its status.
	 */
	public void paintComponent ( Graphics g ) {

		int x = this.getX();
		int y = this.getY();
		int halfway = this.getWidth() / 2;

		switch ( status ) {
		case 1:
			g.setColor(boardColor);
			g.fillRect(x,y - 25,this.getWidth() + 25,this.getHeight() + 25);
			g.setColor(Color.BLACK);
			g.drawLine(0,halfway,this.getWidth() + 25,halfway);
			g.drawLine(halfway,0,halfway,this.getWidth());
			g.setColor(Color.WHITE);
			g.fillOval(x + 9,y + 9,22,22);
			break;

		case 2:
			g.setColor(boardColor);
			g.fillRect(x,y - 25,this.getWidth() + 25,this.getHeight() + 25);
			g.setColor(Color.BLACK);
			g.drawLine(0,halfway,this.getWidth() + 25,halfway);
			g.drawLine(halfway,0,halfway,this.getWidth());
			g.setColor(Color.BLACK);
			g.fillOval(x + 9,y + 9,22,22);
			break;

		default:
			g.setColor(boardColor);
			g.fillRect(x,y - 25,this.getWidth() + 25,this.getHeight() + 25);
			g.setColor(Color.BLACK);
			g.drawLine(0,halfway,this.getWidth() + 25,halfway);
			g.drawLine(halfway,0,halfway,this.getWidth());

			break;
		}
	}

}
