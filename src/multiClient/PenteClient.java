package multiClient;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import multiClient.GamePenteLocalAI.ClickListener;
import multiServer.Stone;

/**
 * This method creates the connection and handles the communication of the
 * clients in the game. Contains methods which are used to send updates to the
 * server after turns are made, when information needs to be sent and when
 * messages need to be checked. There are getters and setters for handling data
 * regarding the lobby, and also contains a method for sending a request to
 * reset the game. stores and displays information to users
 */
public class PenteClient {

	private int PORT = 6666;
	private Socket socket_;
	private readThread read;
	private boolean turnStatus;
	protected PenteWindow gui;
	protected lobbyPanel lobPanel;

	private int blackCaps; // black captures
	private int whiteCaps; // white captures
	private int yourTurn;
	private ClickListener pointer;
	private static boolean winDetected = false;

	/**
	 * constructor for the PenteClient class. Creates a socket from which the
	 * client can connect, as well as a read thread which allows the client to
	 * begin listening to the server
	 */
	public PenteClient () {
		try {

			socket_ = new Socket("localhost",PORT);
			init();
			read = new readThread(socket_,this);
			read.start();

		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * main method which creates a new instance of PenteClient
	 */
	public static void main ( String[] args ) {
		PenteClient client = new PenteClient();
	}

	/*
	 * setter for the turn status of the networked game
	 */
	protected void setTurnStatus ( boolean newStatus ) {
		turnStatus = newStatus;
	}

	/*
	 * This method acts as a switch to change turns
	 */
	protected boolean isTurn () {
		int move;
		if ( turnStatus ) {
			move = 2;
		} else {
			move = 1;
		}
		return move == yourTurn;
	}

	/*
	 * setter method for setting a players turn
	 */
	protected void setYourTurn ( int x ) {
		yourTurn = x;
	}

	/*
	 * container which holds necessary components of the PenteClient
	 */
	public void init () {
		pointer = new ClickListener();

		ArrayList<String> p = new ArrayList<String>();

		ArrayList<String> g = new ArrayList<String>();

		gui = new PenteWindow(new lobbyPanel(p,g,socket_));
		setupStartupListeners();

	}

	/*
	 * sets up start up listeners for menu buttons
	 */
	public void setupStartupListeners () {
		ArrayList<JButton> btns = gui.getStartMenuButtons();
		for ( int i = 0 ; i < btns.size() ; i++ ) {
			btns.get(i).addMouseListener(pointer);
		}
	}

	/*
	 * allows for the creation of additional click listeners
	 */
	public void addClickListener () {
		Stone[] tiles = gui.getTileBoard();
		for ( int x = 0 ; x < 169 ; x++ ) {
			Stone tile = tiles[x];
			tile.addMouseListener(pointer);
		}
	}

	/*
	 * sets up menu listeners
	 */
	public void setupMenuListeners () {
		ArrayList<JMenuItem> items = gui.getMenuItems();

		for ( int i = 0 ; i < items.size() ; i++ ) {
			items.get(i).addActionListener(pointer);
		}
	}

	/*
	 * adds a new game listener to a specified button
	 */
	public void addNewGameListener ( JButton button ) {
		button.addActionListener(pointer);
	}

	/*
	 * resets all score related aspects of the game, as well as the turn status
	 */
	public void reset () {
		winDetected = false;
		whiteCaps = 0;
		blackCaps = 0;
		turnStatus = false;
		int bWidth = gui.getPane().getGridWidth();
		gui.setMessage("New Game!",whiteCaps,blackCaps);

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

	/*
	 * this method allows information to be sent from the client to the server
	 */
	public void sendInfo ( String info ) throws IOException {
		ObjectOutputStream output =
		    new ObjectOutputStream(socket_.getOutputStream());
		output.writeInt(1);
		output.writeObject(info);
		output.flush();
	}

	/*
	 * creates necessary messages when achieving certain combos
	 */
	public void messageCheck ( int x, int y, int move ) {
		Object[] keys =
		    PenteUtil.getNonEmptyNeighbors(x,y,gui.getPane()).keySet().toArray();
		int comboCode = PenteUtil.checkCombos(x,y,gui.getPane(),keys);
		switch ( comboCode ) {
		case 1:

			gui.setInfoMessage("Player " + move + " has a tria!");
			break;
		case 2:

			gui.setInfoMessage("Player " + move + " has a tessera!");
			break;
		default:

			gui.setInfoMessage("Player " + move + " has gone.");
			break;
		}
	}

	/*
	 * getter method for the lobby
	 */
	public lobbyPanel getLobby () {
		return gui.getLobbyPanel();
	}

	/*
	 * setter method for the lobby
	 * @param lobby to be set
	 */
	public void setLobby ( lobbyPanel lobby ) {
		gui.setLobbyPanel(lobby);

	}

	/*
	 * getter method for the turn status
	 */
	public boolean getTurn () {
		return turnStatus;
	}

	/*
	 * sends a request for a new game from the client to the server
	 */
	public void sendNewGame () throws IOException {
		ObjectOutputStream output =
		    new ObjectOutputStream(socket_.getOutputStream());
		System.out.println("Sent reset");
		output.writeInt(0);
		output.flush();
	}

	public class ClickListener implements MouseListener, ActionListener {
		/**
		 * This will be the primary method that we implement for our MouseListener
		 * class. Within the body, we with make a method
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
			JPanel clickedPanel = (JPanel) arg0.getSource();

			String coordinateString = clickedPanel.getName();

			try {
				if ( isTurn() ) {
					sendInfo(coordinateString);
				} else {
					gui.setInfoMessage("Wait your Turn");
				}

			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// send the turn here

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

			/*
			 * this statement controls the JRadioButtons in lobbyPanel
			 */
			if ( arg0.getSource() instanceof JRadioButton ) {

			}

			if ( arg0.getSource() instanceof JButton ) {
				if ( arg0.getActionCommand().equals("New Game Button1") ) {
					JButton btn = (JButton) arg0.getSource();
					NewGameDialog dia = (NewGameDialog) btn.getTopLevelAncestor();
					dia.dispose();
					try {
						sendNewGame();
					} catch ( IOException e ) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// reset();
				}
				if ( arg0.getActionCommand().equals("New Game Button2") ) {
					JButton btn = (JButton) arg0.getSource();
					WinDialog dia = (WinDialog) btn.getTopLevelAncestor();
					dia.dispose();
					try {
						sendNewGame();
					} catch ( IOException e ) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// reset();
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
