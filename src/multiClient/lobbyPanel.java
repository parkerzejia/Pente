package multiClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

/**
 * This method contains all necessary components needed for the creation of the
 * networks lobby
 */
public class lobbyPanel extends JPanel implements ActionListener {
	public JPanel lobbyPanelContent;
	public JLabel lobbyStatus;
	public JPanel players_;
	public JPanel games_;
	public JPanel lobbyContent_;
	public JPanel buttonPanelContent;

	protected PenteClient penteClient;

	private ButtonGroup lobbyButtons_;

	public Hashtable<String,JRadioButton> playerButtons_ =
	    new Hashtable<String,JRadioButton>();
	public Hashtable<String,JRadioButton> gameButtons_ =
	    new Hashtable<String,JRadioButton>();

	public JButton start_, pick_;
	public Socket client_;

	public lobbyPanel ( ArrayList<String> names, ArrayList<String> gameTitles,
	                    Socket client ) {
		init(names,gameTitles);
		client_ = client;
	}

	/*
	 * creation of the main components needed for the creation of the lobbyPanel
	 * @param names - list of names(clients) which represent open games
	 * @param gameTitles - a list of currently existing games (for spectation)
	 */
	public void init ( ArrayList<String> names, ArrayList<String> gameTitles ) {
		this.setBackground(new Color(190,233,228));

		this.setLayout(new GridBagLayout());

		lobbyPanelContent = new JPanel();
		lobbyPanelContent.setLayout(new BorderLayout());
		lobbyPanelContent.setBorder(new EmptyBorder(5,5,5,5));
		lobbyPanelContent.setBackground(Color.LIGHT_GRAY);

		lobbyStatus = new JLabel("Welcome to Lobby!");
		lobbyStatus.setFont(new Font("Tahoma",Font.PLAIN,20));
		lobbyStatus.setHorizontalAlignment(JLabel.CENTER);

		lobbyContent_ = new JPanel();
		lobbyContent_.setLayout(new GridLayout(1,2));
		lobbyContent_.setBorder(new EmptyBorder(5,5,5,5));
		lobbyContent_.setBackground(Color.BLACK);

		players_ = new JPanel();
		players_.setLayout(new GridLayout(names.size(),1));
		players_.setBackground(Color.BLUE);

		// panel to hold start and spectate buttons
		buttonPanelContent = new JPanel();
		buttonPanelContent.setLayout(new GridLayout(2,1));
		buttonPanelContent.setBackground(new Color(190,233,228));
		this.add(buttonPanelContent);

		start_ = new JButton("Start");
		start_.addActionListener(this);

		buttonPanelContent.add(start_);

		// generates a radio button for every waiting client (ID's)
		for ( int i = 0 ; i < names.size() ; i++ ) {
			JRadioButton player = new JRadioButton(names.get(i));
			playerButtons_.put(names.get(i),player);
			players_.add(player);
		}

		lobbyContent_.add(players_); // adds the list of players (ID's) to the lobby

		games_ = new JPanel();
		games_.setLayout(new GridLayout(gameTitles.size(),1));

		// generates radio buttons for every game currently being played
		for ( int i = 0 ; i < gameTitles.size() ; i++ ) {
			JRadioButton game = new JRadioButton(gameTitles.get(i));
			gameButtons_.put(gameTitles.get(i),game);
			games_.add(game);
		}

		lobbyContent_.add(games_); // adds spectatable games to the lobby

		lobbyPanelContent.add(lobbyContent_,BorderLayout.CENTER);
		lobbyPanelContent.add(lobbyStatus,BorderLayout.NORTH);

		add(lobbyPanelContent);
	}

	/*
	 * simple getter for the hastable of playerButtons_ for use in PenteClient
	 */
	public Hashtable<String,JRadioButton> getPlayerButtons () {
		return playerButtons_;
	}

	/*
	 * simple getter for the hastable of gameButtons_ for use in PenteClient
	 */
	public Hashtable<String,JRadioButton> getGameButtons () {
		return gameButtons_;
	}

	/*
	 * method for adding adding a players ID to the lobby
	 */
	public void addPlayer ( String player ) {
		playerButtons_.put(player,new JRadioButton(player));
		players_.add(playerButtons_.get(player));
		this.revalidate();
	}

	/*
	 * this method allows for another game ID being played to be added to the
	 * lobby
	 */
	public void addGame ( String game ) {
		gameButtons_.put(game,new JRadioButton(game));
		games_.add(playerButtons_.get(game));
		this.revalidate();
	}

	/*
	 * This method removes a specified players ID from the lobby
	 */
	public void removePlayer ( String player ) {
		players_.remove(playerButtons_.get(player));
		playerButtons_.remove(player);
		this.revalidate();
	}

	/*
	 * This method removes a specified game ID from the lobby
	 */
	public void removeGame ( String game ) {
		games_.remove(gameButtons_.get(game));
		gameButtons_.remove(game);
		this.revalidate();
	}

	/*
	 * This method is a getter for receiving the ID of a game for means of
	 * spectating
	 */
	public String getPick () {
		for ( int i = 0 ; i < playerButtons_.size() ; i++ ) {
			System.out.println((String) (playerButtons_.keySet().toArray())[i]);
			if ( (playerButtons_.get((playerButtons_.keySet().toArray())[i]))
			    .isSelected() ) {

				return (String) (playerButtons_.keySet().toArray())[i];
			}
		}
		for ( int i = 0 ; i < gameButtons_.size() ; i++ ) {
			if ( (gameButtons_.get((gameButtons_.keySet().toArray())[i]))
			    .isSelected() ) {
				return (String) (gameButtons_.keySet().toArray())[i];
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * This method controls the events of the lobby buttons.
	 * Handles messages sent when a spectator selects a game to watch
	 * Also handles when a player chooses another client to join (necessary
	 * streams are created for contact with server)
	 */
	@Override
	public void actionPerformed ( ActionEvent arg0 ) {
		System.out.println("Here");
		if ( arg0.getSource() instanceof JButton ) {
			JButton btn = (JButton) arg0.getSource();
			String chosen = getPick();
			int messageType;
			messageType = 2;
			try {
				ObjectOutputStream output =
				    new ObjectOutputStream(client_.getOutputStream());
				output.writeInt(messageType);
				output.writeInt(Integer.parseInt(chosen));
				output.flush();
				System.out.println("Sent command " + messageType + " for selection "
				    + chosen);
			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
