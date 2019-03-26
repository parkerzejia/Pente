package multiClient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import multiServer.Stone;

/**
 * This class contains all necessary variables and methods for creating a local
 * version of the game, capable of being played by two human players. Methods
 * set turn status, reset the game, create necessary listeners for menus,
 * clicks... Controls the overall logic of the game. This includes necessary
 * references to PenteUtil which validates moves, checks for captures, wins,
 * combos. Contains action listeners menu items
 */

public class GamePenteLocal {
	/**
	 * This variable is a JFrame that holds the GUI.
	 */
	private PenteWindow gui;
	/**
	 * ClickListener handles click events in PenteWindow.
	 */
	private ClickListener pointer;
	/**
	 * This boolean value indicates whose turn it is. As a result, there is no
	 * need for any Player objects. 1 - black's turn. 0 - white's turn.
	 */
	private boolean turnStatus;
	private static boolean winDetected = false;

	/**
	 * This integer holds the number of white captures.
	 */
	private int whiteCaps;

	/**
	 * This integer holds the number of black captures.
	 */
	private int blackCaps;

	/**
	 * Default constructor for GamePente. Calls the init() method, which should do
	 * two things: -build and display the start menu. -prep the Board JPanel.
	 */
	public GamePenteLocal () {
		init();
	}

	protected void setTurnStatus ( boolean newStatus ) {
		turnStatus = newStatus;
	}

	/**
	 * @return boolean value, indicating whether or not it is black's turn.
	 */
	public boolean isBlacksTurn () {
		return turnStatus;
	}

	/**
	 * When called, this method effectively resets the state of the game. To do
	 * this: -cap counts must be reset -go through the entire board, setting each
	 * status to 0. -call Board.update()
	 */
	public void reset () {
		winDetected = false;
		whiteCaps = 0;
		blackCaps = 0;
		turnStatus = false;
		int bWidth = gui.getPane().getGridWidth();
		gui.setMessage(turnStatus,"New Game!",whiteCaps,blackCaps);

		for ( int i = 0 ; i < bWidth ; i++ ) {
			for ( int j = 0 ; j < bWidth ; j++ ) {

				if ( i == (bWidth - 1) / 2 && j == (bWidth - 1) / 2 ) {
					gui.getPane().getStoneAt(i,j).setStatus(2);
				} else {
					gui.getPane().getStoneAt(i,j).setStatus(0);
				}
			}
		}
		gui.getPane().revalidate();
		gui.getPane().repaint();

	}

	/**
	 * This method initializes needed click listeners, new windows and startup
	 * listeners
	 */
	public void init () {
		pointer = new ClickListener();
		gui = new PenteWindow();
		setupStartupListeners();

	}

	/**
	 * This method is used to set the listener for each of the JButtons in the
	 * start menu to be pointer.
	 */
	public void setupStartupListeners () {
		ArrayList<JButton> btns = gui.getStartMenuButtons();
		for ( int i = 0 ; i < btns.size() ; i++ ) {
			btns.get(i).addMouseListener(pointer);
		}
	}

	/**
	 * This method is used to set the listener for each of the JPanels on the
	 * PlayPane.
	 */
	public void addClickListener () {
		Stone[] tiles = gui.getTileBoard();
		for ( int x = 0 ; x < 169 ; x++ ) {
			Stone tile = tiles[x];
			tile.addMouseListener(pointer);
		}
	}

	/**
	 * This method is used to set the listener for each JMenuItem.
	 */
	public void setupMenuListeners () {
		ArrayList<JMenuItem> items = gui.getMenuItems();

		for ( int i = 0 ; i < items.size() ; i++ ) {
			items.get(i).addActionListener(pointer);
		}
	}

	public void addNewGameListener ( JButton button ) {
		button.addActionListener(pointer);
	}

	/**
	 * The ClickListener class is the MouseListener for the Board class. It
	 * listens for mouse clicks. Upon hearing one, it retrieves the location
	 * within the GridLayout of the Stone that was clicked, and passes that
	 * location to PenteUtil.
	 */
	public class ClickListener implements MouseListener, ActionListener {
		/**
		 * This will be the primary method that we implement for our MouseListener
		 * class. Within the body, we with make a method
		 */

		/*
		 * constructor for click listener
		 */
		public ClickListener () {
			super();
		}

		/**
		 * This mouseClicked method contains most of the sequential checking. Makes
		 * several calls to PenteUtil to perform various checks. Also used for other
		 * types of ActionEvents with other types of components, such as JMenuItems
		 * and JButtons
		 */
		public void mouseClicked ( MouseEvent arg0 ) {

			if ( arg0.getSource() instanceof JButton ) {
				JButton btn = (JButton) arg0.getSource();
				if ( btn.getText() == "Quit" ) {
					QuitDialog quitPan = new QuitDialog();
					quitPan.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					quitPan.setVisible(true);
					quitPan.validate();
					return;
				}

				if ( btn.getText() == "How to Play" ) {
					gui.triggerTutorial();
					return;
				}

				if ( btn.getText() == "Play Game" ) {

					gui.setupGame(true);
					addClickListener();
					setupMenuListeners();
					return;
				}
			}

			if ( winDetected ) {
				return;
			}

			// System.out
			// .println("********************************************************************");
			// System.out.println("START OF AN ATTEMPTED MOVE");
			// System.out
			// .println("********************************************************************");

			JPanel clickedPanel = (JPanel) arg0.getSource();

			/**
			 * if things happen the way they should and the clicked JPanel is a Stone
			 * on the Board, then getName() should return something of the form: 1,2
			 * OR 4,2 where the first number is the x, and the 2nd is the y.
			 */
			String coordinateString = clickedPanel.getName();

			String[] xAndY = coordinateString.split(",");

			int x = Integer.parseInt(xAndY[0]);
			int y = Integer.parseInt(xAndY[1]);
			// System.out.println(x+","+y);

			int move;
			if ( turnStatus ) {
				move = 2;
			} else {
				move = 1;
			}

			/**
			 * VALIDATE MOVE CHECK.
			 */
			if ( !PenteUtil.validateMove(x,y,move,gui.getPane()) ) {
				gui.setInfoMessage("Illegal move!");
				return;
			}
			gui.getPane().getStoneAt(x,y).setStatus(move);

			Object[] keys =
			    PenteUtil.getNonEmptyNeighbors(x,y,gui.getPane()).keySet().toArray();

			/**
			 * CHECK CAPTURES, THEN PERFORM NECESSARY UPDATES TO CAPCOUNT AND BOARD.
			 */
			ArrayList<Point> caps =
			    PenteUtil.checkCaptures(x,y,move,gui.getPane(),keys);
			if ( move == 2 ) {
				blackCaps = blackCaps + (caps.size() / 2);

			} else {
				whiteCaps = whiteCaps + (caps.size() / 2);

			}

			/**
			 * CHECK WIN for both players.
			 */
			if ( !turnStatus ) {
				if ( PenteUtil.checkWin(x,y,whiteCaps,gui.getPane()) ) {
					gui.setInfoMessage("Player 2 wins!");
					winDetected = true;
					WinDialog winDialog = new WinDialog(2);
					winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					winDialog.setVisible(true);
					winDialog.validate();
					addNewGameListener(winDialog.getNewGameButton());
					return;
				}
			} else {
				if ( PenteUtil.checkWin(x,y,blackCaps,gui.getPane()) ) {
					gui.setInfoMessage("Player 1 wins!");
					winDetected = true;
					WinDialog winDialog = new WinDialog(1);
					winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					winDialog.setVisible(true);
					winDialog.validate();
					addNewGameListener(winDialog.getNewGameButton());
					return;
				}
			}

			/**
			 * FINALLY, CHECK COMBOS. (TRIA AND TESSERA)
			 */
			int comboCode = PenteUtil.checkCombos(x,y,gui.getPane(),keys);
			switch ( comboCode ) {
			case 1:

				gui.setMessage(turnStatus,"Player " + move + " has a tria!",whiteCaps,
				               blackCaps);
				break;

			case 2:

				gui.setMessage(turnStatus,"Player " + move + " has a tessera!",
				               whiteCaps,blackCaps);
				break;

			default:

				gui.setMessage(turnStatus,"Player " + move + " has gone.",whiteCaps,
				               blackCaps);
				break;
			}

			turnStatus = !turnStatus;

		}

		public void mouseEntered ( MouseEvent arg0 ) {

		}

		public void mouseExited ( MouseEvent arg0 ) {

		}

		public void mousePressed ( MouseEvent arg0 ) {
			// TODO Auto-generated method stub

		}

		public void mouseReleased ( MouseEvent arg0 ) {
			// TODO Auto-generated method stub

		}

		/**
		 * this method deals solely with the JMenuItems while playing, as well as
		 * the "New Game" button on NewGameDialog and the "New Game" button on the
		 * WinDialog.
		 */
		public void actionPerformed ( ActionEvent arg0 ) {

			if ( arg0.getSource() instanceof JButton ) {
				if ( arg0.getActionCommand().equals("New Game Button1") ) {
					JButton btn = (JButton) arg0.getSource();
					NewGameDialog dia = (NewGameDialog) btn.getTopLevelAncestor();
					dia.dispose();
					reset();
				}
				if ( arg0.getActionCommand().equals("New Game Button2") ) {
					JButton btn = (JButton) arg0.getSource();
					WinDialog dia = (WinDialog) btn.getTopLevelAncestor();
					dia.dispose();
					reset();
				}
			}

			if ( arg0.getSource() instanceof JMenuItem ) {
				JMenuItem item = (JMenuItem) arg0.getSource();

				if ( item.getText() == "Quit" ) {
					QuitDialog qdia = new QuitDialog();
					return;
				}
				if ( item.getText() == "New Game" ) {
					NewGameDialog ngdia = new NewGameDialog();
					// JPanel holder = (JPanel) ngdia.getContentPane().getComponent(0);
					ngdia.getRootPane().getDefaultButton().addActionListener(pointer);

					ngdia.setVisible(true);
					ngdia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					ngdia.validate();
					return;

				}
				if ( item.getText() == "Game Rules" ) {
					Tutorial tut = new Tutorial();
					tut.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					tut.setVisible(true);
					tut.validate();
					return;
				}
			}
		}
	}
}
