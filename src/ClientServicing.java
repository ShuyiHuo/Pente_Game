
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class represents a single clien't connection to the server
 * 
 * @author Sergio Perez
 * @author Zoe
 * @author Justin Lynch
 * @author Terrance Rose
 */
public class ClientServicing extends Thread implements Runnable {
	private static boolean shutdown_ = false;
	ServerSocket socket;
	Socket connection;
	volatile String name_;// name of client on this server thread
	// BufferedReader reader_;
	ObjectOutputStream writer_;

	/**
	 * Shutdown function for connection
	 */
	public void shutDown () {
		shutdown_ = true;
	}

	/**
	 * Constructor which takes a socket for connecting with Server
	 * 
	 * @param connect
	 *          socket to be connected.
	 */
	public ClientServicing ( Socket connect ) {

		connection = connect;

	}

	@Override
	public void run () {
		// TODO Auto-generated method stub

		try {

			shutdown_ = false;
			writer_ = new ObjectOutputStream((connection.getOutputStream()));

			ObjectInputStream reader =
			    new ObjectInputStream(connection.getInputStream());

			while ( shutdown_ == false ) {

				Object response;

				try {

					response = reader.readObject();

					if ( response instanceof String ) {
						messageRecieved((String) response);

					}
					if ( response instanceof GameState ) {
						System.out.println("Received gamestate from client.");
						for ( OnlineGame temp : Server.GamesInProgress_ ) {
							if ( ((GameState) response).sender_.equals(temp.getPlayers()[0])
							    || ((GameState) response).sender_
							        .equals(temp.getPlayers()[1]) ) {// found correct match

								temp.sendToAll(response);
							}
						}
					}

					if ( shutdown_ == true ) {

						socket.close();
						shutDown();

					}
				} catch ( ClassNotFoundException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} catch ( IOException e ) {
			System.out
			    .println(" this is where the error is I/O error: " + e.getMessage());
		}

	}

	/**
	 * returns name of player associated with this Server connection
	 * 
	 * @return name of specific player/client connected to server
	 */
	public String getPlayerName () {

		return name_;
	}

	/**
	 * Sends an object to the client.
	 * 
	 * @param message
	 *          - object to be sent.
	 */
	public void messageToClient ( Object message ) {
		try {
			writer_.reset();
			writer_.writeObject(message);
			writer_.flush();
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Sends the list of games in progress in the server, to the client.
	 */
	public void sendServerGameList () {
		try {
			String[] names;
			for ( OnlineGame temp : Server.GamesInProgress_ ) {
				names = temp.getPlayers();

				writer_.reset();
				writer_.writeObject(names);
				writer_.flush();
			}
		} catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method deals with incoming messages provided as a parameter. It
	 * responds to the client in a number of ways including start messages for
	 * beginning gameplay or observation of a game.
	 * 
	 * @param message
	 *          the message to be interpreted
	 */
	public void messageRecieved ( String message ) {

		if ( message != null ) {

			System.out.println("message received:" + message);
			if ( message.startsWith("observer") ) {

			}

			if ( message.startsWith("playername") ) {
				System.out.println("playername message: " + message);
				String[] temp = message.split(" ");

				// System.out.println(temp.length);
				name_ = temp[1];
				System.out.println("name attatched to this client: " + name_);

				String[] values = { temp[2], temp[3], temp[4], temp[5] };

				Server.addPlayer(name_,values);

				System.out.println("Values for new client " + temp[2]);
				if ( temp[2].equals("observing") ) {
					sendServerGameList();
				}

				try {
					writer_.writeObject(Server.players_);
					writer_.flush();
				} catch ( IOException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else if ( message.startsWith("start") ) {// for when user selects play
			                                         // with an opponent selected

				String[] msg = message.split(" ");

				String p1 = msg[1];
				String p2 = msg[2];

				OnlineGame newGame = new OnlineGame(p1,p2);

				Server.GamesInProgress_.add(newGame);

				newGame.getState().startGame_ = true;
				newGame.getState().p1name_ = p1;
				newGame.getState().p2name_ = p2;

				newGame.sendToAll(newGame.getState());
				sendServerGameList();

			} else if ( message.startsWith("disconnect") ) {
				String disconnectedPlayer = message.split(" ")[1];

				Server.removePlayer(disconnectedPlayer);
				Server.removeThread(disconnectedPlayer);

				shutdown_ = true;
			}

			else if ( message.startsWith("beginobserver") ) {
				System.out.println("this was called.");
				String[] obserInfo = message.split(" ");

				String p1 = obserInfo[2];
				String p2 = obserInfo[4];
				String observer = obserInfo[1];

				for ( OnlineGame runner : Server.GamesInProgress_ ) {
					if ( runner.getPlayers()[0].equals(p1)
					    || runner.getPlayers()[1].equals(p1)
					    || runner.getPlayers()[0].equals(p2)
					    || runner.getPlayers()[1].equals(p2) ) {
						runner.addObserver(observer);

						break;
					}
				}
			}
		} else {
			System.out.println("null message received. " + message);
		}

	}

	/**
	 * sets the name variable so this specific thread can be identified in the
	 * server's listed connections
	 * 
	 * @param name
	 *          used to identify this connection
	 */
	public void setPlayerName ( String name ) {
		name_ = name;
	}

}
