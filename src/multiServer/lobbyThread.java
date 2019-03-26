package multiServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class handles the creation and communications regarding all aspects of
 * the lobby. It allows for the update of clients, both adding
 */
public class lobbyThread extends Thread {
	private volatile ArrayList<Socket> clients;
	private volatile ArrayList<gameThread> games;

	public lobbyThread () {
		clients = new ArrayList<Socket>();
		games = new ArrayList<gameThread>();
	}

	protected void updateClients ( Socket member ) throws IOException {
		clients.add(member);
		System.out.println("Updated");
		for ( int i = 0 ; i < clients.size() ; i++ ) {
			ObjectOutputStream output =
			    new ObjectOutputStream(clients.get(i).getOutputStream());
			output.writeInt(4);
			output.writeInt(clients.size() - 1);
			for ( int j = 0 ; j < clients.size() ; j++ ) {
				if ( j != i ) {

					output.writeInt(j);
				}
			}

			output.flush();
		}
	}

	/*
	 * Allows for the removal of clients from the lobby
	 */
	protected void updateClients () throws IOException {
		for ( int i = 0 ; i < clients.size() ; i++ ) {
			ObjectOutputStream output =
			    new ObjectOutputStream(clients.get(i).getOutputStream());
			output.writeInt(4);
			output.writeInt(clients.size() - 1);
			for ( int j = 0 ; j < clients.size() ; j++ ) {
				System.out.println(j);
				if ( j != i ) {

					output.writeInt(j);
				}
			}
			output.flush();
		}
	}

	/*
	 * This method removes a pair of clients when a game has been started.
	 * Those clients are then placed in a new game thread
	 */
	private void removeToGame ( Socket p1, Socket p2 ) throws IOException {
		String name =
		    "user: " + clients.indexOf(p1) + "v. " + " user:" + clients.indexOf(p2);
		clients.remove(p1);
		clients.remove(p2);
		updateClients();
		gameThread nGame = new gameThread(p1,p2,name);
		games.add(nGame);
		nGame.start();

	}

	/*
	 * removes a specified spectator from a specified game thread from the list of
	 * clients
	 */
	private void removeToGame ( Socket spectator, gameThread game ) {
		clients.remove(spectator);
		game.updateList(spectator);
	}

	/*
	 * continuously reads the input streams of all clients in the lobby,
	 * waiting for a request to be placed in a game
	 */
	private void readClients () throws IOException, ClassNotFoundException {
		for ( int i = 0 ; i < clients.size() ; i++ ) {
			if ( (clients.get(i).getInputStream()).available() > 0 ) {
				System.out.println("Checking client # " + i);
				ObjectInputStream read =
				    new ObjectInputStream((clients.get(i).getInputStream()));

				int header = read.readInt();
				System.out.println(header);// player or game
				if ( header == 2 ) {
					int other = (read.readInt());
					sendChallenge(clients.get(i),2);
					sendChallenge(clients.get(other),1);
					removeToGame(clients.get(other),clients.get(i));
				}
			}
		}
	}

	/*
	 * This method sends a challenge when a client has chosen an open game
	 * from the lobby.
	 * @param client - the client that is being challenged
	 */
	public void sendChallenge ( Socket client, int turn ) throws IOException {
		ObjectOutputStream output =
		    new ObjectOutputStream(client.getOutputStream());
		output.writeInt(5);
		output.writeInt(turn);
		output.flush();
	}

	/*
	 * general run method for the start of readClients();
	 */
	public void run () {
		while ( true ) {
			try {
				readClients();
			} catch ( IOException | ClassNotFoundException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
