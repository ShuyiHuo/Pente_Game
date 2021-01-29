
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * This class represents a client which connects to a server and can send and
 * receive messages
 * 
 * @author Sergio Perez
 * @author Zoe
 * @author Justin Lynch
 * @author Terrance Rose
 */
public class Client {

	private Socket connection_; // Establishes a connection with the Server
	private ObjectOutputStream out_; // Each client will be able to send
	                                 // information to the server

	public static void main ( String[] args ) {

		GUI x = new GUI(); // will Create a Pente program (Pente Game)
	}

	/**
	 * @param server
	 *          will Obtain the information of the Server
	 * @param port
	 *          will hold the port number of the server This method will create a
	 *          client that in tunr will be connected to the Server
	 */
	public Client ( String server, int port ) {

		try {
			connection_ = new Socket(server,port);

			out_ = new ObjectOutputStream((connection_.getOutputStream()));
			System.out.println("Successfully connected to server!");
		} catch ( UnknownHostException e ) {

			e.printStackTrace();
			System.out.println("Was not able to connect to host server.");
		} catch ( IOException e ) {

			e.printStackTrace();
		}
	}

	/**
	 * this method sends a message to the host this client is connected to.
	 * 
	 * @param message
	 *          to be sent. Messages are formatted into an array of specific sizes
	 *          and orders which the server reads a specific way.
	 */
	public void sendMessage ( Object message ) {

		try {
			out_.reset();
			out_.writeObject(message);
			out_.flush();
		} catch ( IOException e ) {

			e.printStackTrace();
			System.out.println("Occured in Client.java");
		}

	}

	/**
	 * Returns this client's connection socket to the host.
	 * 
	 * @return a socket
	 */
	public Socket getSocket () {
		return connection_;
	}

}
