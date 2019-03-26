package LocalPlay;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

import multiClient.GamePenteLocal;
import multiClient.GamePenteLocalAI;
import multiClient.PenteClient;
import multiClient.lobbyPanel;

/**
 * This class is the main class for the Ultimate Pente project This class
 * specifically designs the initial JPanel that provides the user with options
 * to choose what rule set is played with, whether the user desires to play
 * locally or on a network, as well as the ability to decide between playing
 * with another human player or against the in house AI (which is quite
 * advanced) Components are also created in this class. JRadioButtons provide
 * the options to choose from which are kept in respective ButtonGroups to
 * prevent multiple options from being chosen at one time. Action listeners for
 * the play button take into account certain combinations, and creates instances
 * of classes that represent the local and networked versions of the game. It
 * also will create an instance of classes that represent Human vs. Human
 * gameplay as well as Human vs. AI gameplay. If the user attempts to play
 * against AI on the network, the user is given an alert asking to choose a new
 * combination of play options, and all JRadio buttons are reset. If an option
 * is left unchosen, an alert will also be sent to ensure all options are
 * accounted for when building the custom games. The help button opens the
 * default browser to a description of the game of Pente incase the user has
 * never played before
 */
public class NewPenteWindow extends JFrame {

	private String webURL = "https://www.pente.net/instructions.html"; // link to
	                                                                   // pente
	                                                                   // description

	private JLabel rulesetLB1_, playLB1_, opponentLB1_; // labels for JRadioButton
	                                                    // options
	private JButton playButton_, helpButton_; // play and help buttons

	/*
	 * This is the main method for the program which simply creates a new instance
	 * of PenteWindow
	 */
	public static void main ( String[] args ) {
		NewPenteWindow penteWindow = new NewPenteWindow();
	}

	/*
	 * Constructor for NewPenteWindow class
	 */
	public NewPenteWindow () {

		setSize(580,500);

		setResizable(false);
		setLocation(500,100);// put it in the middle
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		init();
		setVisible(true);// set visible
	}

	/*
	 * This method puts together the components for NewPenteWindow Incorporates
	 * action listeners for both buttons which opens respective links and creates
	 * necessary instances strictly dependent on chosen game options
	 */
	public void init () {

		Boolean isEnabled = false;

		this.setTitle("Welcome To Ultimate Pente!");

		JPanel pane = new JPanel();
		pane.setBackground(new Color(190,233,228));
		GridBagLayout grid = new GridBagLayout();
		GridBagConstraints gridc = new GridBagConstraints();
		pane.setLayout(grid);

		JRadioButton tradit = new JRadioButton("Traditional");
		tradit.setFont((new Font("Monospaced",Font.PLAIN,18)));
		tradit.setBackground(new Color(190,233,228));

		// JRadioButton keryo = new JRadioButton("Keryo-Pente");
		// JRadioButton gomoku = new JRadioButton("Go-Moku");
		// JRadioButton connect6 = new JRadioButton("Connect 6");
		JRadioButton local = new JRadioButton("Local");
		local.setFont((new Font("Monospaced",Font.PLAIN,18)));
		local.setBackground(new Color(190,233,228));

		JRadioButton network = new JRadioButton("Network");
		network.setFont((new Font("Monospaced",Font.PLAIN,18)));
		network.setBackground(new Color(190,233,228));

		// radio buttons for opponents
		JRadioButton player = new JRadioButton("Human");
		player.setFont((new Font("Monospaced",Font.PLAIN,18)));
		player.setBackground(new Color(190,233,228));

		JRadioButton compAI = new JRadioButton("Computer");
		compAI.setFont((new Font("Monospaced",Font.PLAIN,18)));
		compAI.setBackground(new Color(190,233,228));

		ButtonGroup rulesetOpt = new ButtonGroup();
		ButtonGroup playOpt = new ButtonGroup();
		ButtonGroup opponentOpt = new ButtonGroup();

		rulesetOpt.add(tradit);
		// rulesetOpt.add(keryo);
		// rulesetOpt.add(gomoku);
		// rulesetOpt.add(connect6);
		playOpt.add(local);
		playOpt.add(network);

		opponentOpt.add(player);
		opponentOpt.add(compAI);

		rulesetLB1_ = new JLabel("Ruleset :");
		rulesetLB1_.setFont((new Font("Monospaced",Font.PLAIN,18)));

		playLB1_ = new JLabel("Play from :");
		playLB1_.setFont((new Font("Monospaced",Font.PLAIN,18)));

		opponentLB1_ = new JLabel("Play against :");
		opponentLB1_.setFont((new Font("Monospaced",Font.PLAIN,18)));

		playButton_ = new JButton("Play");
		playButton_.setFont((new Font("Monospaced",Font.PLAIN,18)));

		playButton_.addActionListener(new ActionListener() {
			public void actionPerformed ( ActionEvent e ) {
				// get value chosen from button group regarding network or local play
				System.out.println(((JButton) e.getSource()).getText());

				if ( !tradit.isSelected() ) {
					JOptionPane.showMessageDialog(null,"Please select a play style",
					                              "Ultimate Pente",
					                              JOptionPane.WARNING_MESSAGE);

					// clear button groups
					rulesetOpt.clearSelection();
					playOpt.clearSelection();
					opponentOpt.clearSelection();
				}

				if ( !local.isSelected() && !network.isSelected() ) {
					JOptionPane
					    .showMessageDialog(null,"Please select local or network play",
					                       "Ultimate Pente",JOptionPane.WARNING_MESSAGE);

					// clear button groups
					rulesetOpt.clearSelection();
					playOpt.clearSelection();
					opponentOpt.clearSelection();
				}

				// for choosing area of play
				if ( local.isSelected() && compAI.isSelected() ) { // if local and AI
				                                                   // are selected
					// create a new GamePenteLocal object
					GamePenteLocalAI compGame = new GamePenteLocalAI();
					System.out.println("Local Play Selected Against AI!"); // debugging

				} else if ( local.isSelected() && player.isSelected() ) { // if local
				                                                          // and human
				                                                          // player are
				                                                          // selected

					GamePenteLocal playerGame = new GamePenteLocal();
					System.out.println("Local Play Selected Against Another Player!"); // debugging

				} else if ( network.isSelected() && player.isSelected() ) { // if
				                                                            // network
				                                                            // and human
				                                                            // player
				                                                            // are
				                                                            // selected

					PenteClient netClient = new PenteClient(); // create new instance of
					                                           // network client

					// remove and repaint necessary panels for lobbyPanel
					pane.removeAll();
					pane.repaint();
					pane.revalidate();
					setVisible(false);
					System.out.println("Network Play Selected!"); // debugging

				} else if ( network.isSelected() && compAI.isSelected() ) { // if
				                                                            // network
				                                                            // and AI
				                                                            // are
				                                                            // selected

					// there is no AI over network, let user know and repaint
					// NewPenteWindow
					System.out.println("User Has Selected A Non Existent Game Combo");

					JOptionPane
					    .showMessageDialog(null,
					                       "AI not available over network play. Please " // send
					                                                                     // alert
					                           + "reselect game options",
					                       "Ultimate Pente",JOptionPane.WARNING_MESSAGE);

					// clear button groups
					rulesetOpt.clearSelection();
					playOpt.clearSelection();
					opponentOpt.clearSelection();
				}
			}
		});

		helpButton_ = new JButton("Help");
		helpButton_.setFont((new Font("Monospaced",Font.PLAIN,18)));

		// creates an action listener for help button which will open a url on
		// default browser
		helpButton_.addActionListener(new ActionListener() {
			public void actionPerformed ( ActionEvent e ) {
				openWebpage(webURL);
			}
		});

		// provide necessary padding to accomodate GridbagLayout
		gridc.anchor = GridBagConstraints.WEST;
		gridc.gridx = 0;
		gridc.gridy = 0;
		pane.add(rulesetLB1_,gridc);
		gridc.gridx = 1;
		pane.add(tradit,gridc);
		gridc.gridx = 2;
		gridc.gridx = 0;
		gridc.gridy = 1;
		pane.add(playLB1_,gridc);
		gridc.gridx = 1;
		pane.add(local,gridc);
		gridc.gridx = 2;
		pane.add(network,gridc);
		gridc.gridy = 3;
		gridc.gridx = 0;
		pane.add(opponentLB1_,gridc);
		gridc.gridx = 1;
		pane.add(player,gridc);
		gridc.gridx = 2;
		pane.add(compAI,gridc);
		gridc.anchor = GridBagConstraints.NORTH;
		gridc.insets = new Insets(20,0,0,40);
		gridc.gridx = 1;
		gridc.gridy = 4;
		pane.add(playButton_,gridc);
		gridc.gridx = 1;
		gridc.gridy = 5;
		pane.add(helpButton_,gridc);
		this.getContentPane().add(pane);

	}

	/**
	 * This method allows for a url to be accessed through the users default
	 * browser
	 * 
	 * @param urlString
	 *          - the url of the website being accessed
	 */
	public static void openWebpage ( String urlString ) {
		try {
			Desktop.getDesktop().browse(new URL(urlString).toURI());
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

}
