
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sergio Perez
 * @author Zoe
 * @author Justin Lynch
 * @author Terrance Rose
 */
public class Server {

	public static boolean shutdown_ = false; // is the thread shutdown
	public static Map<String,String[]> players_ = new HashMap<String,String[]>();// map
	                                                                             // of
	                                                                             // players
	                                                                             // to
	                                                                             // their
	                                                                             // values
	                                                                             // (status,
	                                                                             // gameType,
	                                                                             // rule
	                                                                             // set)
	public static List<ClientServicing> threads; // List of players connected to
	                                             // server
	public static ServerSocket socket_; // socket to connect on
	public static ArrayList<OnlineGame> GamesInProgress_ =
	    new ArrayList<OnlineGame>(); // List of games in progress

	/**
	 * shutdown a thread
	 */
	public void shutDown () {
		shutdown_ = true;
	}

	/**
	 * Message received from client
	 * 
	 * @param msg
	 * @return String
	 */

	public static String messageReceived ( String msg ) {
		if ( msg.startsWith("playername") ) {
			String[] name = msg.split(" ");

			return name[1];
		}
		return "no name returned";
	}

	/**
	 * Main server
	 * 
	 * @throws InterruptedException
	 */
	public static void main ( String[] args ) throws InterruptedException {

		threads = new ArrayList<>();

		// TODO create a ServerSocket on port 9000, listen for an incoming request,
		// pick+
		// a random knock knock joke, and tell the joke
		try {
			socket_ = new ServerSocket(9000);

			System.out.println("server listening on port " + socket_.getLocalPort());
			while ( true ) {
				Socket connection = socket_.accept();

				System.out.println("new client connected.");

				ClientServicing newClient = new ClientServicing(connection);

				threads.add(newClient);
				newClient.start();

			}
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// communication

	}

	/**
	 * Adds player to the player list which links their name with the game mode
	 * they want
	 * 
	 * @param name
	 *          name of the player
	 * @param values
	 *          - specs for the game mode they want to play
	 */
	public static void addPlayer ( String name, String[] values ) {

		System.out.println("new player added to server: " + name);
		players_.put(name,values);
		int i = players_.size() - 1;
		threads.get(i).setPlayerName(name);
		System.out.println("Name of client: " + threads.get(i).getPlayerName());

		for ( ClientServicing runner : threads ) {
			runner.messageToClient(players_);
		}
	}

	/**
	 * Removes player from player list and ends their thread connection
	 * 
	 * @param name
	 *          player to remove
	 */
	public static void removePlayer ( String name ) {
		System.out.println(name + " disconnected.");
		players_.remove(name);
	}

	/**
	 * Removes thread from server
	 * 
	 * @param name
	 *          of client to be removed
	 */
	public static void removeThread ( String name ) {
		for ( int i = 0 ; i < threads.size() ; i++ ) {
			if ( threads.get(i).getPlayerName().equals(name) ) {
				threads.remove(i);
			}
		}

		if ( threads.size() > 0 ) {
			for ( ClientServicing runner : threads ) {
				runner.messageToClient(players_);
			}
		}
	}

	/**
	 * @return the list of games currently in progress
	 */
	public static ArrayList<OnlineGame> getGames () {
		return GamesInProgress_;
	}

	/**
	 * Removes a game from the list of games in progress
	 * 
	 * @param players,
	 *          String [] to remove
	 */
	public static void removeGame ( String[] players ) {

	}

}
