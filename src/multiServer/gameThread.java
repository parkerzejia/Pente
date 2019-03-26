package multiServer;

import java.awt.Point;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * This class contains and creates the necessary threads for holding clients
 * currently engaged in a game. This method provides an update to the lobby for
 * spectators. Contains the doTurn method which requires the x and y coordinate,
 * and handles the turnStatus, necessary validity checks and capture checks.
 * Methods related to sending and receiving information from the server are
 * located here, which also handles requests for resetting the game and sending
 * an illegal turn
 */
public class gameThread extends Thread {
	private String gameTitle;
	private Socket playerOne_, playerTwo_;// player one and player two's socket
	private ArrayList<Socket> viewers_;// the spectating audience
	private int whiteCaps_, blackCaps_;// black and white captures, needed to
	                                   // check winds
	private serverGamePane board_;// the board kept by the server to maintain an
	                              // understanding of the game
	private boolean turnStatus_;// whose turn is it
	private boolean winDetected_;// has a win occured

	public gameThread ( Socket p1, Socket p2, String name ) {// the constructor
	                                                         // for an instance
		// of gameThread
		// to be called once two players have decided to enter a match together
		gameTitle = name;
		playerOne_ = p1;
		playerTwo_ = p2;
		viewers_ = new ArrayList<Socket>();
		whiteCaps_ = 0;
		blackCaps_ = 0;
		board_ = new serverGamePane(true);
		turnStatus_ = false;
		winDetected_ = false;
	}

	/*
	 * getter method for the game title of an existing game
	 */
	public String getGameTitle () {
		return gameTitle;
	}

	public void updateList ( Socket spectator ) {// updates the list of
	                                             // spectators to allow a new
	                                             // player to start
		// watching the game
		viewers_.add(spectator);
	}

	private ArrayList<Point> doTurn ( int x, int y ) {// processes the turn
		// all checks and stuff

		int move;
		if ( turnStatus_ ) {
			move = 2;
		} else {
			move = 1;
		}

		/**
		 * VALIDATE MOVE CHECK.
		 */
		if ( !PenteUtil.validateMove(x,y,move,board_) ) {// && isTurn()==true &&
		                                                 // isSpectator()==false
			// send illegal move message
			return null;
		}

		board_.getStoneAt(x,y).setStatus(move);

		Object[] keys =
		    PenteUtil.getNonEmptyNeighbors(x,y,board_).keySet().toArray();

		/**
		 * CHECK CAPTURES, THEN PERFORM NECESSARY UPDATES TO CAPCOUNT AND BOARD.
		 */
		ArrayList<Point> caps = PenteUtil.checkCaptures(x,y,move,board_,keys);
		if ( move == 2 ) {
			blackCaps_ = blackCaps_ + (caps.size() / 2);
		} else {
			whiteCaps_ = whiteCaps_ + (caps.size() / 2);
		}

		/**
		 * CHECK WIN.
		 */
		caps.add(0,new Point(x,y));
		if ( !turnStatus_ ) {
			if ( PenteUtil.checkWin(x,y,whiteCaps_,board_) ) {

				winDetected_ = true;
				return caps;
			}
		} else {
			if ( PenteUtil.checkWin(x,y,blackCaps_,board_) ) {

				winDetected_ = true;
				return caps;
			}
		}

		return caps;
	}

	/*
	 * This method sends the changes to the board identified by the server to the
	 * provided client
	 * @param client - recipient of the changes
	 * @param points - an array list of points that are being added
	 */
	private void sendToClient ( Socket client, ArrayList<Point> points )
	    throws IOException {

		// the message has the following structure
		// 0/1/2/3(normal turn,illegal turn, playerOne wins, playerTwo
		// wins),points.size, points
		// captured, turnstatus,blackCaps_,whiteCaps_
		ObjectOutputStream output =
		    new ObjectOutputStream(client.getOutputStream());
		if ( winDetected_ ) {// normal turn
			if ( turnStatus_ ) {// player one wins
				output.writeInt(2);
			} else {// player two wins
				output.writeInt(3);
			}
		} else {
			output.writeInt(0);
		}
		output.writeInt(points.size());
		for ( int i = 0 ; i < points.size() ; i++ ) {
			output.writeObject(points.get(i));
		}

		output.writeBoolean(turnStatus_);
		System.out.println("Blacks: " + blackCaps_ + ", Whites: " + whiteCaps_);
		output.writeInt(blackCaps_);
		output.writeInt(whiteCaps_);
		output.flush();
	}

	/*
	 * This method sends the changes to the board that is identified by the server
	 * to both player 1 and player 2, a well as all relevant spectators
	 */
	private void sendTurn ( ArrayList<Point> points ) throws IOException {

		sendToClient(playerOne_,points);
		sendToClient(playerTwo_,points);
		for ( int i = 0 ; i < viewers_.size() ; i++ ) {
			sendToClient(viewers_.get(i),points);
		}
	}

	/*
	 * This method recieves a turn from a given client in the form of a string
	 * @param client - client from which the turn is being received
	 */
	private String receiveTurn ( Socket client )
	    throws IOException, ClassNotFoundException, SocketException {

		ObjectInputStream input = new ObjectInputStream(client.getInputStream());
		int teller = input.readInt();
		if ( teller == 1 ) {
			return ((String) input.readObject());
		} else if ( teller == 0 ) {
			return null;
		}
		return null;// new game or something
	}

	/*
	 * This method completely resets the status of the game, 
	 * including any and all captures, the turn status, and
	 * and detected wins
	 */
	public void reset () {
		winDetected_ = false;
		whiteCaps_ = 0;
		blackCaps_ = 0;
		turnStatus_ = false;
		int bWidth = board_.getGridWidth();

		for ( int i = 0 ; i < bWidth ; i++ ) {
			for ( int j = 0 ; j < bWidth ; j++ ) {

				if ( i == (bWidth - 1) / 2 && j == (bWidth - 1) / 2 ) {
					board_.getStoneAt(i,j).setStatus(2);
				} else {
					board_.getStoneAt(i,j).setStatus(0);
				}
			}
		}
		board_.revalidate();
		board_.repaint();

	}

	/*
	 * This method sends a message to a given client in the result
	 * of an illegal turn being made
	 * 
	 * @param client - recipient of the message
	 */
	public void sendIllegalTurn ( Socket client ) throws IOException {
		ObjectOutputStream output =
		    new ObjectOutputStream(client.getOutputStream());
		output.writeInt(1);
		output.writeInt(0);
		output.writeBoolean(turnStatus_);
		System.out.println("Blacks: " + blackCaps_ + ", Whites: " + whiteCaps_);
		output.writeInt(blackCaps_);
		output.writeInt(whiteCaps_);
		output.flush();
	}

	/*
	 * sends a reset request to a specified client
	 * @param client - recipient of the message
	 */
	public void sendReset ( Socket client ) throws IOException {
		ObjectOutputStream output =
		    new ObjectOutputStream(client.getOutputStream());
		output.writeInt(6);
		output.flush();
	}

	/*
	 * this method resets all spectators
	 */
	public void resetAll () throws IOException {
		sendReset(playerOne_);
		sendReset(playerTwo_);
		for ( int i = 0 ; i < viewers_.size() ; i++ ) {
			sendReset(viewers_.get(i));
		}
	}

	public void run () {// one the Thread has been made for the game, it should be
	                    // processing playerOne_'s,
		// and playerTwo_'s moves until the game is done
		while ( !winDetected_ ) {// while game runs
			try {
				while ( !turnStatus_ ) {// keeps trying to process playerOne's move
				                        // until a
					// valid moves is sent
					if ( winDetected_ ) {
						break;
					}

					String coordinateString = receiveTurn(playerOne_);// player one move
					if ( coordinateString == null ) {
						reset();
						resetAll();
						break;
					}
					String[] xAndY = coordinateString.split(",");

					int x = Integer.parseInt(xAndY[0]);
					int y = Integer.parseInt(xAndY[1]);
					ArrayList<Point> changes = doTurn(x,y);// what captures and moves were
					                                       // made

					if ( changes == null ) {// made illegal move
						sendIllegalTurn(playerOne_);
						// send message to player one: illegal move

					} else {// move was valid
						sendTurn(changes);
						turnStatus_ = !turnStatus_;

						// send message to playerOne, playerTwo, and
						// viewers to change their boards
					}
				}
				while ( turnStatus_ ) {// keeps trying to do playerTwo_'s turn until a
				                       // valid
					// move is sent
					if ( winDetected_ ) {// if win detected break out to end the thread
						break;
					}
					String coordinateString = receiveTurn(playerTwo_);// player Two move
					if ( coordinateString == null ) {
						reset();
						resetAll();
						break;
					} // received
					String[] xAndY = coordinateString.split(",");
					int x = Integer.parseInt(xAndY[0]);
					int y = Integer.parseInt(xAndY[1]);
					ArrayList<Point> changes = doTurn(x,y);// what captures and moves were
					                                       // made
					if ( changes == null ) {// made illegal move
						sendIllegalTurn(playerTwo_);
						// send message to player one: illegal move

					} else {// move was valid
						sendTurn(changes);// send changes out to player one, player two, and
						                  // spectators
						turnStatus_ = !turnStatus_;

					}
				}
			} catch ( IOException | ClassNotFoundException e ) {

				e.printStackTrace();
			}

		}
		reset();
		// a player has one the game

	}
}
