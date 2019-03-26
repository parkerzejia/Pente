package multiClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import multiServer.Stone;

/**
 * This class extends JFrame, containing all the GUI components of Pente.
 */
public class PenteWindow extends JFrame {
	/**
	 * This variable represents a visual InfoBar, that displays relevant game
	 * information to the user.
	 */
	private InfoBar info;

	/**
	 * This variable represents a game pane. Contains the Stones.
	 */
	private PlayPane pane;
	/**
	 * this is the main container for the playing state. Contains a Board and
	 * InfoBar.
	 */
	private JPanel container;

	/**
	 * this is the main container for the starting state. Contains start menu
	 * stuff.
	 */
	private JPanel startPanel;

	/**
	 * the following group of Objects make up our menu bar.
	 */
	private JMenuBar gameMenuBar;
	private JMenu gameMenu;
	private JMenuItem newGame;
	private JMenuItem gameRules;
	private JMenuItem quit;

	/**
	 * this is a Tutorial popup in the form of a JDialog.
	 */
	private Tutorial tutorial_;

	/**
	 * The following 4 JButtons belong on the starting screen.
	 */
	private JButton btn13Grid;
	private JButton btnHowToPlay;
	// private JButton btn19Grid;
	private JButton btnQuit;

	/**
	 * used to load an image
	 */
	private ImageIcon dragonImg;

	/**
	 * used to ask the user whether they are sure they want to start a new game.
	 */
	private NewGameDialog ngDialog;
	private volatile lobbyPanel lobby_;

	/**
	 * Default constructor for this class. Constructs this GUI component by
	 * calling its init() method.
	 */
	public PenteWindow () {

		setSize(580,500);
		setResizable(true);
		setLocation(500,100);// put it in the middle
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the entire

		init();
		setVisible(true);// set visible
	}

	/*
	 * constructor for updated panel containing the lobby
	 */
	public PenteWindow ( lobbyPanel lobby ) {
		setSize(580,500);
		setResizable(true);
		setLocation(500,100);// put it in the middle
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // close the entire
		lobby_ = lobby;
		init();
		startPanel.setVisible(false);
		this.remove(startPanel);
		this.add(lobby_);
		setVisible(true);// set visible
	}

	/**
	 * This method initializes the visual aspects of this class.
	 */
	public void init () {// Creates the JFrame, and starts the game

		ngDialog = new NewGameDialog();
		ngDialog.setVisible(false);
		tutorial_ = new Tutorial();
		tutorial_.setVisible(false);

		startPanel = new JPanel();
		startPanel.setName("start");
		startPanel.setBorder(new EmptyBorder(5,5,5,5));
		startPanel.setLayout(new BorderLayout());
		startPanel.setSize(new Dimension(200,200));

		JPanel panel = new JPanel();
		panel.setBackground(Color.black);
		startPanel.add(panel,BorderLayout.NORTH);

		JTextPane txtpn = new JTextPane();
		txtpn.setForeground(Color.white);
		txtpn.setBackground(Color.black);
		txtpn.setFont(new Font("Tahoma",Font.PLAIN,22));
		txtpn.setEditable(false);
		txtpn.setText("Welcome to Pente!");
		txtpn.setVisible(true);
		panel.add(txtpn);

		JPanel btnPanel = new JPanel();

		JPanel imgPanel = new JPanel();
		dragonImg = new ImageIcon("images/Dragon.png");
		Image image = dragonImg.getImage(); // transform it
		Image newImg = image.getScaledInstance(375,375,java.awt.Image.SCALE_SMOOTH);
		dragonImg = new ImageIcon(newImg);

		JLabel imageLabel = new JLabel("",dragonImg,JLabel.CENTER);
		// imageLabel.setHorizontalAlignment(FlowLayout.CENTER);
		imgPanel.add(imageLabel);
		imgPanel.setBackground(Color.BLACK);

		btnPanel.setBackground(Color.BLACK);
		startPanel.add(imgPanel,BorderLayout.CENTER);
		startPanel.add(btnPanel,BorderLayout.SOUTH);

		// Formats the 13 x 13 button
		btn13Grid = new JButton("Play Game");
		btn13Grid.setFont(new Font("Tahoma",Font.PLAIN,20));
		btn13Grid.setOpaque(false);
		btn13Grid.setContentAreaFilled(false);
		btn13Grid.setBorderPainted(false);
		btn13Grid.setForeground(Color.WHITE);
		btn13Grid.setSize(new Dimension(60,40));

		btn13Grid.addMouseListener(new MouseAdapter() {
			public void mouseClicked ( MouseEvent e ) {

			}

			// Sets the background of the button to gray when the button
			// is hovered over
			public void mouseEntered ( java.awt.event.MouseEvent evt ) {
				btn13Grid.setBackground(Color.DARK_GRAY);
				btn13Grid.setOpaque(true);
				btn13Grid.setContentAreaFilled(true);
			}

			public void mouseExited ( java.awt.event.MouseEvent evt ) {
				btn13Grid.setOpaque(false);
				btn13Grid.setContentAreaFilled(false);
				btn13Grid.setBorderPainted(false);
				btn13Grid.setBackground(Color.black);
			}
		});
		btnPanel.add(btn13Grid);

		// Formats the How to Play button
		btnHowToPlay = new JButton("How to Play");
		btnHowToPlay.setFont(new Font("Tahoma",Font.PLAIN,20));
		btnHowToPlay.setOpaque(false);
		btnHowToPlay.setContentAreaFilled(false);
		btnHowToPlay.setBorderPainted(false);
		btnHowToPlay.setForeground(Color.WHITE);
		btnHowToPlay.setSize(new Dimension(60,40));

		btnHowToPlay.addMouseListener(new MouseAdapter() {
			public void mouseClicked ( MouseEvent e ) {

			}

			// Sets the background of the button to gray when the button
			// is hovered over
			public void mouseEntered ( java.awt.event.MouseEvent evt ) {
				btnHowToPlay.setBackground(Color.DARK_GRAY);
				btnHowToPlay.setOpaque(true);
				btnHowToPlay.setContentAreaFilled(true);
			}

			public void mouseExited ( java.awt.event.MouseEvent evt ) {
				btnHowToPlay.setOpaque(false);
				btnHowToPlay.setContentAreaFilled(false);
				btnHowToPlay.setBorderPainted(false);
				btnHowToPlay.setBackground(Color.black);
			}
		});

		btnPanel.add(btnHowToPlay);

		// Formats the Quit button
		btnQuit = new JButton("Quit");
		btnQuit.setFont(new Font("Tahoma",Font.PLAIN,20));
		btnQuit.setOpaque(false);
		btnQuit.setContentAreaFilled(false);
		btnQuit.setBorderPainted(false);
		btnQuit.setForeground(Color.WHITE);
		btnQuit.setSize(new Dimension(60,40));

		btnQuit.addMouseListener(new MouseAdapter() {

			// Sets the background of the button to gray when the button
			// is hovered over
			public void mouseEntered ( java.awt.event.MouseEvent evt ) {
				btnQuit.setBackground(Color.DARK_GRAY);
				btnQuit.setOpaque(true);
				btnQuit.setContentAreaFilled(true);
			}

			public void mouseExited ( java.awt.event.MouseEvent evt ) {
				btnQuit.setOpaque(false);
				btnQuit.setContentAreaFilled(false);
				btnQuit.setBorderPainted(false);
				btnQuit.setBackground(Color.black);
			}
		});
		btnPanel.add(btnQuit);
		this.getContentPane().add(startPanel);
	}

	/*
	 * method that triggers a tutorial popup
	 */
	public void triggerTutorial () {
		tutorial_.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		tutorial_.setVisible(true);
		validate();
	}

	/*
	 * getter method for retrieving the lobbyPanel
	 */
	public lobbyPanel getLobbyPanel () {
		return lobby_;
	}

	/*
	 * removes the lobby upon the start of a game
	 */
	public void startGame () {
		this.remove(lobby_);
	}

	/*
	 * setter method for the lobby panel
	 * @param lobby is the needed lobby panel
	 */
	public void setLobbyPanel ( lobbyPanel lobby ) {
		setVisible(false);
		remove(lobby_);
		lobby_ = lobby;
		add(lobby);
		this.revalidate();
		setVisible(true);
	}

	/**
	 * This method sets up the playing surface for the game.
	 * 
	 * @param is13by13
	 *          - specifies whether or not the Board should be 13x13.
	 */
	public void setupGame ( boolean is13by13 ) {

		if ( !is13by13 ) {
			this.setLocation(300,40);
			this.setSize(750,900);
		} else {
			this.setSize(this.getWidth(),this.getHeight() + 160);
		}
		this.getContentPane().setBackground(Stone.boardColor);
		pane = new PlayPane(is13by13);
		info = new InfoBar();
		gameMenuBar = new JMenuBar();
		gameMenu = new JMenu("Menu");

		newGame = new JMenuItem("New Game");
		gameRules = new JMenuItem("Game Rules");
		quit = new JMenuItem("Quit");

		this.getContentPane().removeAll();

		// Builds the menu
		gameMenu.add(newGame);
		gameMenu.add(gameRules);
		gameMenu.add(quit);
		gameMenuBar.add(gameMenu);
		getRootPane().setJMenuBar(gameMenuBar);

		/**
		 * builds the container JPanel.
		 */
		container = new JPanel();
		container.setBackground(new Color(56,119,53));
		container.add(pane);
		container.add(info);
		container.setVisible(true);
		this.getContentPane().add(container);
		revalidate();
		repaint();

	}

	/*
	 * This method takes the new PlayPane board and uses it
	 * to replace the existing pane in the penteWindow
	 */
	public void setBoard ( PlayPane replacement ) {

		pane = replacement;
		pane.repaint();
		pane.revalidate();
		this.revalidate();
	}

	/**
	 * Returns all the Stones on PlayPane, in the form of a Stone[] object.
	 * @return an array of stones on the board
	 */
	public Stone[] getTileBoard () {

		Stone[] tiles;
		if ( pane.getGridWidth() == 13 ) {
			tiles = new Stone[169];
		} else {
			tiles = new Stone[361];
		}

		JPanel holder = null;

		for ( int i = 0 ; i < pane.getGridWidth() ; i++ ) {

			for ( int j = 0 ; j < pane.getGridWidth() ; j++ ) {
				holder = (JPanel) pane.getComponent((i * pane.getGridWidth()) + j);
				tiles[(i * pane.getGridWidth()) + j] = (Stone) holder.getComponent(0);
			}

		}
		return tiles;
	}

	/**
	 * Returns an ArrayList<JButton> containing all of the JButtons on the start
	 * menu. Used to set up ActionListeners.
	 */
	public ArrayList<JButton> getStartMenuButtons () {
		ArrayList<JButton> comps = new ArrayList<JButton>();
		comps.add(btn13Grid);
		// comps.add(btn19Grid);
		comps.add(btnHowToPlay);
		comps.add(btnQuit);
		return comps;
	}

	/**
	 * Returns an ArrayList<JMenuItem> containing all the JMenuItems in the menu.
	 * Used to set up ActionListeners.
	 */
	public ArrayList<JMenuItem> getMenuItems () {
		ArrayList<JMenuItem> comps = new ArrayList<JMenuItem>();
		comps.add(quit);
		comps.add(gameRules);
		comps.add(newGame);
		return comps;
	}

	/**
	 * Getter for Board.
	 */
	public PlayPane getPane () {
		return pane;
	}

	/**
	 * Setter for the message on InfoPanel; displays while playing.
	 * @param msg is the message being set
	 */
	public void setInfoMessage ( String msg ) {
		info.setMessage(msg);
	}

	/**
	 * Getter for startPanel.
	 */
	public JPanel getStartPanel () {
		return startPanel;
	}

	/**
	 * @param turnstatus
	 * @param message
	 * @param whiteCaps
	 * @param blackCaps
	 */
	public void setMessage ( String message, int whiteCaps, int blackCaps ) {

		info.setBlackCaps(blackCaps);
		info.setWhiteCaps(whiteCaps);
		info.setMessage(message);

	}

	/*
	 * @param turnstatus
	 * @param message
	 * @param whiteCaps
	 * @param blackCaps
	 */
	public void setMessage ( boolean turnstatus, String message, int whiteCaps,
	                         int blackCaps ) {

		info.setBlackCaps(blackCaps);
		info.setWhiteCaps(whiteCaps);
		info.setMessage(message);

	}

}
