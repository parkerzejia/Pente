package multiServer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class holds the game board that the server uses to keep track of the status of 
 * the game.
 */
public class serverGamePane extends JPanel {
	
	private static int width = 0;
	
	/*
	 * constructor for serverGamePane
	 */
	public serverGamePane(boolean is13by13) {		
		if (is13by13) {
			width = 13;
		}
		else {
			width = 19;
		}
		setBackground(Stone.boardColor);
		GridLayout layout = new GridLayout(width,width);
		layout.setHgap(0);
		layout.setVgap(0);	
		setLayout(layout);
		
		JPanel holder = null;	
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				
				holder = new JPanel();
				holder.setLayout(new BorderLayout());
				Stone stone = new Stone();
				
				/**
				 * MIDDLE PIECE
				 */
				if (i==(width-1)/2 && j==(width-1)/2) {
					stone.setStatus(2);
				}
				stone.setName(i+","+j);
				holder.add(stone,BorderLayout.CENTER);
				add(holder);
			}
		}
		setVisible(true);
		revalidate();
		repaint();
				
	}
	
	/**
	 * Getter for width.
	 */
	public int getGridWidth() {
		return width;
	}
	
	
	/**
	 * Given two integers (x & y) that form a Point, returns the status at that point.
	 * @param x  :  x portion of the queried Point.
	 * @param y  : y portion of the queried Point.
	 * @return integer value indicating the status. Will only ever be between 0, 1, or 2, 
	 * 				 inclusive where 0 = unoccupied, 1 = white, and 2 = black.
	 * 
	 * NOTE: if a Stone is requested from a Point not on the Board, then -1 will be returned.
	 */
	public int getStatusAt(int x,int y) {
		
		if (x < 0 || y < 0 || x >= width || y >= width) {
			return -1;
		}
		
		int spot = (x*13) + y;
		JPanel holder = (JPanel)this.getComponent(spot);
		Stone s = (Stone) holder.getComponent(0);		
		
		int i = s.getStatus();
		return i;
	}
	
	
	/**
	 * Given a point specified by x & y, returns the Stone at that location.
	 * @param x  : x portion of the Point we are querying.
	 * @param y  : y portion of the Point we are querying.
	 * @return Stone at that location.
	 * 
	 * NOTE: the return value may be null if a Stone is requested from a non-existent location.
	 */
	public Stone getStoneAt(int x,int y) {

		if (x < 0 || y < 0 || x >= width || y >= width) {
			return null;
		}
		
		int spot = (x*width) + y;
		JPanel holder = (JPanel) this.getComponent(spot);		
		return (Stone) holder.getComponent(0);
	}
	
}
