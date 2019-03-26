package multiClient;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JDialog;

/**
 * @author chase receives and processes information
 */
public class readThread extends Thread {

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	Socket socket_;//the client socket from which the thread will be reading
	PenteClient game_;//the client's gamePente instance

	/**
	 * creates instance of gameThread(Socket client, GamePente game)
	 **/
	public readThread ( Socket client, PenteClient pente ) {
		socket_ = client;
		game_ = pente;

	}

	@Override
	public void run () {//the run method, called on start that will always be receiving information from the server

		// TODO Auto-generated method stub
		while ( true ) {//a never-ending while lopp to keep the readThread going as long as the thread exists
			try {

				receiveInfo(socket_);//receives information from the server at the specified socket
			} catch ( ClassNotFoundException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void receiveInfo ( Socket socket ) throws ClassNotFoundException {//receives information from the server
		//at the specified socket. The information will be of the form dictated by the network protocol, with different
		//event on the server being handed down to the client via different headers that funnel the information
		//into the correct read organizations
		// TODO Auto-generated method stub
		try {
			ObjectInputStream input = new ObjectInputStream(socket_.getInputStream());//in inputStream for the client
			//is in the form of an objectInputStream that allows us to read and write integers easily
			int teller = input.readInt();//the teller is the header that allows information to be funnelled correctly
			System.out.println("received turn");

			if ( teller == 0 ) {//a standard update board message received
				int numStones = input.readInt();//the number of positions to be altered on the board
				Point add = (Point) input.readObject();//the first point returned from the server will the the stone that was added
				//all others will be removed
				
				for ( int i = 1 ; i < numStones ; i++ ) {//goes through all stones after the added stone, removing any stones
					Point removeStone = (Point) input.readObject();
					game_.gui.getPane().getStoneAt(removeStone.x,removeStone.y)
					    .setStatus(0);//alters the clients board to reflect the removed stones
					
				}
				int move;//the turn number in integer form for penteUtil methods
				boolean turner=input.readBoolean();//the turn status of the game
				if ( turner ) {//specifies the correct move given the turn status
					move = 2;
				} else {
					move = 1;
				}
				game_.gui.getPane().getStoneAt(add.x,add.y).setStatus(move);
				//adds the stone to the board
				game_.setTurnStatus(!game_.getTurn());
				//switches the turn status of the client's game
				int black=input.readInt();
				int white=input.readInt();
				//the black and white captures as held by the server are updated on the client
				game_.messageCheck(add.x,add.y,move);
				//checks if any combination was made
				if(numStones>1) {
					game_.gui.setMessage("Player " + move + " has captured "+ (numStones-1) +" stones!",white,black);
				}//if any captures were recorded, the player is altered
				game_.gui.revalidate();//the board is revalidated

			} else if ( teller == 1 ) { // represents an illegal move
				String illegalMoveMessage = "Illegal Move";

				// get captures and shit to clear stream
				game_.gui.setInfoMessage(illegalMoveMessage); // display the illegal move
				                                              // message
				input.reset();//resets the inputStream to clear any unneeded data
			} else if ( teller == 2 ) { // player 1 win
				String playerOneWin = "Player 1 Wins!";
				int numStones = input.readInt();//the number of positions to be altered on the board
				Point add = (Point) input.readObject();//the first point returned from the server will the the stone that was added
				//all others will be removed

				for ( int i = 1 ; i < numStones ; i++ ) {//goes through all stones after the added stone, removing any stones
					Point removeStone = (Point) input.readObject();
					game_.gui.getPane().getStoneAt(removeStone.x,removeStone.y)
					    .setStatus(0);//alters the clients board to reflect the removed stones
				}
				int move;//the turn number in integer form for penteUtil methods
				boolean turner=input.readBoolean();//the turn status of the game
				if ( turner ) {//specifies the correct move given the turn status
					move = 2;
				} else {
					move = 1;
				}
				game_.gui.getPane().getStoneAt(add.x,add.y).setStatus(move);
			//adds the stone to the board
				game_.setTurnStatus(turner);
				//sets the turnstatus to whatever the server records as the turnstatus
				int black=input.readInt();
				int white=input.readInt();
			//the black and white captures as held by the server are updated on the client
				game_.gui.setMessage(playerOneWin,black, white);
				//display the win message
				WinDialog winDialog = new WinDialog(1);
				winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				winDialog.setVisible(true);
				winDialog.validate();
				//display the win dialog
				game_.addNewGameListener(winDialog.getNewGameButton());
				game_.reset();
				//reset the game
			} else if ( teller == 3 ) { // player 2 win
				String playerTwoWin = "Player 2 Wins!";
				int numStones = input.readInt();//the number of positions to be altered on the board
				Point add = (Point) input.readObject();//the first point returned from the server will the the stone that was added
				//all others will be removed

				for ( int i = 1 ; i < numStones ; i++ ) {//goes through all stones after the added stone, removing any stones
					Point removeStone = (Point) input.readObject();
					game_.gui.getPane().getStoneAt(removeStone.x,removeStone.y)
					    .setStatus(0);//alters the clients board to reflect the removed stones
				}
				
				int move;//the turn number in integer form for penteUtil methods
				boolean turner=input.readBoolean();//the turn status of the game
				if ( turner ) {//specifies the correct move given the turn status
					move = 2;
				} else {
					move = 1;
				}
				game_.gui.getPane().getStoneAt(add.x,add.y).setStatus(move);//adds the stone to the board
				game_.setTurnStatus(turner);
			//display the win message
				int black=input.readInt();
				int white=input.readInt();
				game_.gui.setMessage(playerTwoWin,black, white);
				WinDialog winDialog = new WinDialog(2);
				winDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				winDialog.setVisible(true);
				winDialog.validate();
			//display the win dialog
				game_.addNewGameListener(winDialog.getNewGameButton());

			} else if ( teller == 4 ) {//a lobby update message to allow the user to
				//stay up to date on who is in the lobby or not
				int numPeople = input.readInt();//the number of people awaiting a game
				ArrayList<String>players=new ArrayList<String>();//an arrayList of all people in the
				//lobby
				for ( int i = 0 ; i < numPeople ; i++ ) {//reads exactly the number of people
					//int he lobby and displays them
					players.add(Integer.toString(input.readInt()));
				}
				ArrayList<String>games=new ArrayList<String>();
				//int numGames = input.readInt();
				//System.out.println("There are " + numGames + " games running now");
				/*for ( int i = 0 ; i < numGames ; i++ ) {
					games.add((String)input.readObject());
					System.out.println("Game #" + i + ":" + games.get(i));
				}*/
				game_.setLobby(new lobbyPanel(players,games,socket_));
				//sets the new lobby panel to reflect the changed state of the lobby to the user
			}
			else if(teller==5) {//challenged(auto accept)
				System.out.println("Challenged");
				int yourT=input.readInt();//the user is dictated their turnNumber, read here
				game_.setYourTurn(yourT);//the turn number read above is set 
				game_.gui.startGame();//the gui is started, removing the lobbypanel to make room for the PlayPane
				game_.gui.setupGame(true);//sets up the playPane, and adds it to the JFrame 
				game_.addClickListener();//adds click listener to the requisite JPanels that make up the board,
				//and all other event based swing components
				game_.setupMenuListeners();//sets up the menu
			}
			else if(teller==6) {//if a reset command has been given by the server due to the game being declared complete
				//the client will reset
				System.out.println("Reset received");
				game_.reset();
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}

}
