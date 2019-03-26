package multiServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class creates the server that overlooks all clients and related threads of the game
 * Creates a server socket from which clients may connect to. The creation of a lobby thread
 * for a traditional game is created and then started. All clients are updated once accepted
 * by the server
 *
 */
public class penteServer {

private static int PORT=6666;//the port number as set by the user
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main ( String[] args ) throws IOException {

		Socket client=null;
		lobbyThread traditional=new lobbyThread();//lobby thread for a traditional game
		traditional.start();//starts the traditional lobby
		try {
			ServerSocket server = new ServerSocket(PORT);
			while(true) {
				client=server.accept();
				System.out.println("client Accepted");//the server is always accepting new client connections
				traditional.updateClients(client);//updates the clients, including the one that just connected	
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}	
	}

}
