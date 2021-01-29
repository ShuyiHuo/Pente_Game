import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.LineBorder;

import java.awt.event.*;
import javax.swing.*;

/**
 * @author Sergio Perez
 * @author Qingyi Lu
 * @author Joshua Walker
 * @author Jacque Kane
 */

public class GUI extends JFrame {
	private JFrame mainFrame_ = new JFrame();
	private JFrame gui = new JFrame();
	private JFrame waitingRoom_ = new JFrame();
	private JFrame observeRoom_ = new JFrame();
	private String[] ListOfGames_ = new String[25];
	private String PlayerName_;
	private PenteClient connection_;
	private String[] PlayerNames_ = new String[50];
	private ObjectInputStream in_;
	private String playerInfo = "";
	private boolean observe_ = false;
	private GameState state_;
	

	// info to go into gamestate obj
	private static int numberOfConnection_ = 0;
	private static String ifCapture_;
	private static int numberOfCapture_ = 0;
	Grid grid_ = new Grid();
	static JTextField Player1Capture_; // will display player one's captures
	static JTextField Player2Capture_; // will display player two's captures
	static boolean win_ = false;
	static boolean winnerGomoku_;
	private static boolean gameInProgress_;
	private static Game game_ = new Game(); // will create a Pente game
	private static Board board_ = game_.getBoard(); // will create the Pente board
	static JTextField playerMode_ = new JTextField(" Current Player: 1 ");
	private int currPlayer_;

	private JPanel panel = new JPanel(new GridLayout(0,1));
	/// the pente board
	JPanel rightButtons_ = new JPanel(new GridLayout(9,1)); // the display info on
	                                                        // the right of the
	                                                        // window
	JPanel centerPanel_ = new JPanel(new BorderLayout()); // will hold the pente
	                                                      // board
	JPanel bottom_ = new JPanel(); // the bottom black line on the pente board
	JPanel top_ = new JPanel(); // the top black line on the pente board
	JPanel left_ = new JPanel(); // the left black line on the pente board
	JPanel right_ = new JPanel(); // the right black line on the pente board
	// player 1
	// mode
	JTextField capture_ = new JTextField(" Number of Captures "); // player 1 mode
	                                                              // capture

	static String p1name_ = "";
	static String p2name_ = "";
	JPanel mainMenu;
	int counter_ = 0;
	int counterSide_ = 0;
	String timeElapsed_ = "";
	JTextField time_;
	JButton newGame_ = new JButton(" New Game "); // will create a new pente game
	JButton rules_ = new JButton(" Rules "); // will display the rules for pente
	JButton menu = new JButton("Menu");
	JButton opponetStatus_ = new JButton("Opponet");
	int mouseX_ = 0; // will return x of the mouse
	int mouseY_ = 0; // will return the y of the
	boolean higlight_ = false; // will higlight_ yellow a vertex
	ArrayList<Double> vertexX_ = new ArrayList<Double>(); // will hold a set of
	// vertex Y
	ArrayList<Double> vertexY_ = new ArrayList<Double>(); // will hold a set of
	// vertex X

	boolean type;
	private ArrayList<GameThread> gamesList_ = new ArrayList<GameThread>();
	private boolean useComputer_ = false;
	private Computer comp_;
	private Map<String,String[]> players_;
	private int gameType_ = 0;
	private boolean disconnected_;
	private boolean startGame_;
	private boolean playOnline_;
	private int playerID_ = 0;// black or white 1 mean black, 2 means white 0 is
	                          // observer
	private String[] gameList_ = new String[20];

	String[] filler = { "no games" };
	private JComboBox<String[]> cBox;
	private JComboBox<String[]> observeBox_;

	/**
	 * @author tr4730
	 */
	private class PenteClient extends Client {

		String name_ = "";// name of user on this client
		// private PrintWriter out_;

		public PenteClient ( String hostName, int port ) {
			super(hostName,port);

		}

	}

	/**
	 * Thread that receives messages from the server
	 * 
	 * @author tr4730
	 */
	private class ReadThread extends Thread implements Runnable {

		@Override
		public synchronized void run () {
			try {

				this.wait(1000);
				in_ = new ObjectInputStream((connection_.getSocket().getInputStream()));

				try {

					while ( true ) {
						Object obj;
						obj = in_.readObject();

						if(observe_) {
						if ( obj instanceof String[] ) {// playerlist for observers

							System.out.println("new entry added to game list.");
							addToGameList((String[]) obj);
							if ( observeBox_ == null ) {
								System.out.println("observeBox is returning null?");
							} else {
								System.out.println("observeBox isn't the issue.");
							}
							DefaultComboBoxModel<String> model =
							    new DefaultComboBoxModel<String>(gameList_);
							observeBox_.setModel(new DefaultComboBoxModel(gameList_));
						}
						}

						if ( obj instanceof GameState ) {

							update((GameState) obj);
							repaint();
							// starting new online game
							if ( startGame_ ) {
								// if this condition is true, this player was challenged
								if ( observe_ == false && playerID_ == 0 ) {
									p1name_ = ((GameState) obj).p1name_;
									p2name_ = ((GameState) obj).p2name_;
									playerID_ = 2;// if observer, this stays at 0.
									grid_.repaint();
								}
								startOnlineGame(gameType_);
								startGame_ = false;
							} else {
								update((GameState) obj);
								grid_.repaint();
								repaint();
							}
						}

						if ( obj instanceof HashMap<?,?> ) {// only hashmap will be of
						                                    // playernames, so skip if
						                                    // observing

							System.out.println("HashMap Received from server.");
							Map<String,String[]> players = (HashMap<String,String[]>) obj;
							players_ = players;

							/*DefaultComboBoxModel<String> model =
							    new DefaultComboBoxModel<String>();*/
							if ( !observe_ ) {
								cBox.setModel(new DefaultComboBoxModel(getMatches(gameType_)));
							}
						}

					}
				} catch ( ClassNotFoundException e ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch ( InterruptedException e1 ) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}// end of ReadThread class

	/**
	 * 
	 * @param array
	 */
	public void addToGameList ( String[] array ) {

		String p1 = array[0];
		String p2 = array[1];
		String vs = (p1 + " vs " + p2);

		for ( int i = 0 ; i < gameList_.length ; i++ ) {
			if ( gameList_[i] == null ) {
				gameList_[i] = vs;
				break;
			}
		}
	}

	/**
	 * This method updates the gui's gameState to reflect gui's info, then sends
	 * it to the server. Should be called after player has finished their turn so
	 * server sends to other player and they can make their move.
	 */
	public void sendGameState () {

		GameState newState = new GameState();
		newState.sender_ = PlayerName_;
		connection_.sendMessage(newState);
		System.out.println("Sent gamestate to server. p1name_ is:" + PlayerName_);
	}

	/**
	 * Returns gui's game
	 * 
	 * @return the game object
	 */
	public static Game getGame () {
		return game_;
	}

	/**
	 * updates the state of the game
	 * 
	 * @param GameState,
	 *          the current state of the game
	 */
	public void update ( GameState state ) {

		startGame_ = state.startGame_;
		gameInProgress_ = state.inProgress_;

		if ( !startGame_ ) {// if game isn't just starting, these values are coming
		                    // from other player's gui and are not null,
		                    // hopefully...
			game_ = state.game_;
			board_ = state.game_.getBoard();
			// game_.getBoard()=
			currPlayer_ = state.game_.getCurrentPlayer();
			grid_.repaint();
		}

	}

	/**
	 * Returns list of players who match desired game variation
	 * 
	 * @param n
	 *          corresponds to choice of variation in gameSpecs method
	 * @return the name of players matching the desired version of game
	 */
	private String[] getMatches ( int n ) {
		String[] matches = new String[50];// holds name of players that match
		Map<String,String[]> temp = new HashMap<String,String[]>();
		temp = players_;
		if ( players_ == null ) {
			System.out.println("players list is empty.");
		}

		int i = 0;
		for ( Map.Entry<String,String[]> entry : temp.entrySet() ) {
			if ( Integer.parseInt(entry.getValue()[1]) == n ) {
				matches[i] = entry.getKey();
				i = i + 1;
			}
		}
		return matches;
	}

	/**
	 * Gets games in progress
	 * 
	 * @return String[] of games
	 */
	public String[] getListOfGames () {
		System.out.println("Server games " + Server.getGames().size());
		String[] returnStringArray = new String[25];
		System.out.println("in method");
		for ( int i = 0 ; i < Server.GamesInProgress_.size() ; i++ ) {
			System.out.println("in for");
			String[] stringArray = Server.GamesInProgress_.get(i).getPlayers();
			String toAdd = "" + stringArray[0] + " " + stringArray[1];
			System.out.println("add " + toAdd);
			returnStringArray[i] = toAdd;

		}
		return returnStringArray;
	}

	/**
	 * Prompts the user for the specifics of the game they want to play
	 */
	public void gameSpecs () {

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(3,1,5,5));
		Object[] options = { "Pente", "Gomoku", "Ala Carte", "cancel" };
		int n = JOptionPane
		    .showOptionDialog(panel,"Which rule do you want to choose?",
		                      "game variations",JOptionPane.DEFAULT_OPTION,
		                      JOptionPane.QUESTION_MESSAGE,null,options,options[2]);

		if ( n == 0 ) {
			mainFrame_.setVisible(false);
			winnerGomoku_ = false;
			type = false;
			numberOfConnection_ = 5;
			numberOfCapture_ = 5;
			String name = JOptionPane.showInputDialog("Enter your name.");
			PlayerName_ = name;
			playerInfo =
			    PlayerName_ + " " + "Waiting" + " " + "0" + " " + "." + " " + ".";// "0"
			                                                                      // means
			                                                                      // Pente
			                                                                      // was
			                                                                      // chosen

			gameType_ = 0;
			if ( playOnline_ == true ) {
				if ( !observe_ ) {
					JPanel comboboxPanel = new JPanel();
					comboboxPanel.setPreferredSize(new Dimension(300,300));
					comboboxPanel.setBackground(new Color(0x77dd77));
					// System.out.println("test" + PlayerNames_[0]);
					addName(PlayerName_);
					cBox = new JComboBox(PlayerNames_);
					cBox.setPreferredSize(new Dimension(300,50));

					comboboxPanel.add(cBox);
					waitingRoom_.add(comboboxPanel,BorderLayout.CENTER);
					int i = 0;
					if ( i == JOptionPane.OK_OPTION ) {
						waitingRoom_.setVisible(true);
					}

				}
			}

		} else if ( n == 1 ) {
			mainFrame_.setVisible(false);
			winnerGomoku_ = true;
			type = true;
			numberOfConnection_ = 5;
			String name = JOptionPane.showInputDialog("Enter your name.");
			PlayerName_ = name;
			playerInfo =
			    PlayerName_ + " " + "Waiting" + " " + "1" + " " + "." + " " + ".";
			gameType_ = 1;

			if ( !observe_ ) {

				JPanel comboboxPanel = new JPanel();
				comboboxPanel.setPreferredSize(new Dimension(300,300));
				comboboxPanel.setBackground(new Color(0x77dd77));
				addName(PlayerName_);
				cBox = new JComboBox(PlayerNames_);
				cBox.setPreferredSize(new Dimension(300,50));

				comboboxPanel.add(cBox);
				waitingRoom_.add(comboboxPanel,BorderLayout.CENTER);
				int i = 0;
				if ( i == JOptionPane.OK_OPTION ) {
					waitingRoom_.setVisible(true);
				}
			}

		} else if ( n == 2 ) {
			System.out.println("work");
			JTextField xField = new JTextField(5);
			JTextField yField = new JTextField(5);
			JTextField zField = new JTextField(5);

			JPanel myPanel1 = new JPanel();
			myPanel1.setLayout(new GridLayout(3,1,5,5));
			myPanel1.add(new JLabel("Number of connections to win"));
			myPanel1.add(xField);
			myPanel1.add(new JLabel("Do you want Captures? Enter yes or no."));
			myPanel1.add(yField);
			myPanel1.add(new JLabel("Number of captures to win"));
			myPanel1.add(zField);

			// this prompts the player for the names of the players
			int result = JOptionPane.showConfirmDialog(null,myPanel1,"Ala Carte",
			                                           JOptionPane.OK_CANCEL_OPTION);

			if ( result == JOptionPane.OK_OPTION ) {

				numberOfConnection_ = Integer.parseInt(xField.getText());

				ifCapture_ = yField.getText();

				if ( ifCapture_.equals("yes") ) {
					numberOfCapture_ = Integer.parseInt(zField.getText());
				} else if ( ifCapture_.equals("no") ) {
					numberOfCapture_ = 0;
				}
				String name = JOptionPane.showInputDialog("Enter your name.");
				PlayerName_ = name;
				playerInfo = PlayerName_ + " " + "Waiting" + " " + "2" + " "
				    + numberOfConnection_ + " " + numberOfCapture_;
				gameType_ = 2;

				if ( !observe_ ) {

					JPanel comboboxPanel = new JPanel();
					comboboxPanel.setPreferredSize(new Dimension(300,300));
					comboboxPanel.setBackground(new Color(0x77dd77));
					addName(PlayerName_);
					cBox = new JComboBox(PlayerNames_);
					cBox.setPreferredSize(new Dimension(300,50));

					comboboxPanel.add(cBox);
					waitingRoom_.add(comboboxPanel,BorderLayout.CENTER);
					int i = 0;
					if ( i == JOptionPane.OK_OPTION ) {
						waitingRoom_.setVisible(true);
					}
					System.out.println(numberOfConnection_ + "  " + ifCapture_ + "  "
					    + numberOfCapture_);
				}
			}
		}
	}

	/**
	 * Sends messages
	 * 
	 * @author tr4730
	 */
	private class CommunicationThread extends Thread implements Runnable {

		/*
		 * (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public synchronized void run () {

			// TODO Auto-generated method stub

			connection_ = new PenteClient("localhost",9000);

			if ( observe_ ) {
				System.out.println("this is called. line 479.");
				connection_.sendMessage("observer");
			} else {
				connection_.sendMessage("playername " + playerInfo);
			}
			// System.out.println(playerInfo);
			while ( disconnected_ == false ) {
				if ( disconnected_ ) {
					try {
						connection_.getSocket().close();
					} catch ( IOException e ) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	public void startOnlineGame ( int n ) {

		if ( n == 0 ) {
			mainFrame_.setVisible(false);
			gui.setVisible(true);
			winnerGomoku_ = false;
			type = false;
			numberOfConnection_ = 5;
			numberOfCapture_ = 5;
		} else if ( n == 1 ) {
			winnerGomoku_ = true;
			mainFrame_.setVisible(false);
			gui.setVisible(true);
			type = true;
			numberOfConnection_ = 5;
		} else if ( n == 2 ) {
			System.out.println("work");
			JTextField xField = new JTextField(5);
			JTextField yField = new JTextField(5);
			JTextField zField = new JTextField(5);

			JPanel myPanel1 = new JPanel();
			myPanel1.setLayout(new GridLayout(3,1,5,5));
			myPanel1.add(new JLabel("Number of connections to win"));
			myPanel1.add(xField);
			myPanel1.add(new JLabel("Do you want Captures? Enter yes or no."));
			myPanel1.add(yField);
			myPanel1.add(new JLabel("Number of captures to win"));
			myPanel1.add(zField);

			// this prompts the player for the names of the players
			int result = JOptionPane.showConfirmDialog(null,myPanel1,"Ala Carte",
			                                           JOptionPane.OK_CANCEL_OPTION);

			if ( result == JOptionPane.OK_OPTION ) {

				numberOfConnection_ = Integer.parseInt(xField.getText());

				ifCapture_ = yField.getText();

				if ( ifCapture_.equals("yes") ) {
					numberOfCapture_ = Integer.parseInt(zField.getText());
				} else if ( ifCapture_.equals("no") ) {
					numberOfCapture_ = 0;
				}

				System.out.println(numberOfConnection_ + "  " + ifCapture_ + "  "
				    + numberOfCapture_);
			}

			mainFrame_.setVisible(false);
			gui.setVisible(true);
			winnerGomoku_ = false;
			type = false;
		}

		opponetStatus_.setBorder(new LineBorder(Color.WHITE));
		opponetStatus_.setEnabled(true);
		opponetStatus_.setText("Online");

		rightButtons_.add(opponetStatus_);
	}

	/**
	 * The PenteGUI Constructor displays the window with a pente Board on the
	 * left. While an informative box is display on the right
	 */
	public GUI () {
		super("Pente");
		mainFrame_.setLayout(new BorderLayout());
		mainFrame_.setSize(600,700); // fix size so the Grid panel can have a width
		// of
		// 600 and a height of 600
		mainFrame_.setResizable(false);
		mainFrame_.setVisible(true);
		mainFrame_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		gui.setLayout(new BorderLayout());
		gui.setSize(1181,1046); // fix size so the Grid panel can have a width of
		// 800 and a height of 800
		gui.setResizable(false);
		gui.setVisible(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		vertexX_.add(50.0);
		int xAxis = 50;
		int yAxis = 50;

		for ( int i = 0 ; i < 18 ; i++ ) {
			vertexX_.add((double) xAxis + 900 / 18);
			xAxis = xAxis + 900 / 18;
		}

		vertexY_.add(50.0);

		for ( int i = 0 ; i < 18 ; i++ ) {
			vertexY_.add((double) yAxis + 900 / 18);
			yAxis = yAxis + 900 / 18;
		}
		// menu panels
		mainMenu = new JPanel();
		mainMenu.setLayout(new BoxLayout(mainMenu,BoxLayout.Y_AXIS));
		mainMenu.setVisible(true);
		mainMenu.setBackground(new Color(0x77dd77));
		mainFrame_.add(mainMenu);

		// adds the welcome to pente lable on main screen
		final JLabel welcome = new JLabel("WELCOME TO PENTE!");
		welcome.setAlignmentX(welcome.CENTER_ALIGNMENT);
		welcome.setFont(new Font("Serif",Font.PLAIN,30));

		// creates lil bord
		final JPanel miniBoard = new JPanel() {
			public void paintComponent ( Graphics g ) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0x8C5D18));
				g2.fillRect(210,0,170,170);
				g2.setColor(new Color(0x77dd77));
				g2.fillRect(220,10,150,150);
				g2.setColor(new Color(0x5FB15F));
				g2.setStroke(new BasicStroke(2));

				for ( int i = 0 ; i < 6 ; i++ ) {
					g2.drawLine(220,10 + i * 30,370,10 + i * 30);
					g2.drawLine(220 + i * 30,10,220 + i * 30,160);
				}

			}

		};

		miniBoard.setAlignmentX(miniBoard.CENTER_ALIGNMENT);
		miniBoard.setPreferredSize(new Dimension(30,100));
		miniBoard.setBackground(new Color(0x77dd77));

		// creates the buttons on the main menu
		final JPanel menuButtons = new JPanel();
		menuButtons.setLayout(new GridLayout(10,10));
		menuButtons.setPreferredSize(new Dimension(200,200));
		menuButtons.setBackground(new Color(0x77dd77));
		menuButtons.setAlignmentX(welcome.CENTER_ALIGNMENT);
		// menuButtons.setAlignmentX(playerName.CENTER_ALIGNMENT);
		menuButtons.setBorder(BorderFactory.createEmptyBorder(15,20,20,15));

		final JButton PlayOnline = new JButton("Play Online");
		PlayOnline.setBackground(new Color(0xD2B48C));
		final JButton PlayWithComputer = new JButton("Play With Computer");
		PlayWithComputer.setBackground(new Color(0xD2B48C));
		final JButton twoPersonGame = new JButton("Two Person Game ");
		twoPersonGame.setBackground(new Color(0xD2B48C));
		final JButton observeGame = new JButton("Observe Game");
		observeGame.setBackground(new Color(0xD2B48C));

		// menuButtons.add(Field);

		menuButtons.add(Box.createRigidArea(new Dimension(100,10)));
		menuButtons.add(PlayOnline);
		menuButtons.add(Box.createRigidArea(new Dimension(100,10)));
		menuButtons.add(PlayWithComputer);
		menuButtons.add(Box.createRigidArea(new Dimension(100,10)));
		menuButtons.add(twoPersonGame);
		menuButtons.add(Box.createRigidArea(new Dimension(100,10)));
		menuButtons.add(observeGame);

		mainMenu.add(Box.createRigidArea(new Dimension(100,80)));
		mainMenu.add(welcome);
		mainMenu.add(Box.createRigidArea(new Dimension(100,30)));
		mainMenu.add(miniBoard);
		// mainMenu.add(playerName);
		mainMenu.add(menuButtons);

		// create Frame for waiting room

		waitingRoom_.setLayout(new BorderLayout());
		waitingRoom_.setSize(600,600); // fix size so the Grid panel can have a
		// width of 600 and a height of 600
		waitingRoom_.setResizable(false);
		waitingRoom_.setVisible(false);
		waitingRoom_.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// room panel
		JPanel Room = new JPanel();
		Room.setLayout(new BoxLayout(Room,BoxLayout.Y_AXIS));
		Room.setVisible(true);
		Room.setBackground(new Color(0x77dd77));
		waitingRoom_.add(Room,BorderLayout.NORTH);

		// back button
		final JButton back = new JButton("Back");
		back.setBackground(new Color(0xD2B48C));
		back.setAlignmentX(back.BOTTOM_ALIGNMENT);

		final JLabel waitingList = new JLabel("WELCOME TO WAITING ROOM!");
		waitingList.setAlignmentX(waitingList.CENTER_ALIGNMENT);
		waitingList.setFont(new Font("Serif",Font.PLAIN,30));
		Room.add(Box.createRigidArea(new Dimension(100,20)));
		Room.add(waitingList);

		final JLabel PlayerlIST = new JLabel("Player Name List : ");
		PlayerlIST.setAlignmentX(PlayerlIST.CENTER_ALIGNMENT);
		PlayerlIST.setFont(new Font("Serif",Font.PLAIN,30));
		Room.add(Box.createRigidArea(new Dimension(100,20)));
		Room.add(PlayerlIST);

		// Play button
		final JButton play = new JButton("Play");
		play.setBackground(new Color(0xD2B48C));
		play.setAlignmentX(play.BOTTOM_ALIGNMENT);

		JPanel southerbuttons_ = new JPanel();
		southerbuttons_.setLayout(new GridLayout(2,1));

		southerbuttons_.add(play);
		southerbuttons_.add(back);
		waitingRoom_.add(southerbuttons_,BorderLayout.SOUTH);

		// observe panel
		JPanel observe = new JPanel();
		observe.setLayout(new BoxLayout(observe,BoxLayout.Y_AXIS));
		observe.setVisible(true);
		observe.setBackground(new Color(0x77dd77));
		observeRoom_.add(observe,BorderLayout.NORTH);

		// back button
		final JButton back1 = new JButton("Back");
		observe.setBackground(new Color(0xD2B48C));
		observe.setAlignmentX(back1.BOTTOM_ALIGNMENT);

		final JLabel waitingList1 = new JLabel("WELCOME TO OBSERVE ROOM!");
		waitingList1.setAlignmentX(waitingList1.CENTER_ALIGNMENT);
		waitingList1.setFont(new Font("Serif",Font.PLAIN,30));
		observe.add(Box.createRigidArea(new Dimension(100,20)));
		observe.add(waitingList1);

		final JLabel PlayerlIST1 = new JLabel("Game List : ");
		PlayerlIST1.setAlignmentX(PlayerlIST1.CENTER_ALIGNMENT);
		PlayerlIST1.setFont(new Font("Serif",Font.PLAIN,30));
		observe.add(Box.createRigidArea(new Dimension(100,20)));
		observe.add(PlayerlIST1);

		// Play button
		final JButton play1 = new JButton("Observe Game");
		play1.setBackground(new Color(0xD2B48C));
		play1.setAlignmentX(play1.BOTTOM_ALIGNMENT);

		JPanel southerbuttons_1 = new JPanel();
		southerbuttons_1.setLayout(new GridLayout(2,1));

		southerbuttons_1.add(play1);
		southerbuttons_1.add(back1);
		observeRoom_.add(southerbuttons_1,BorderLayout.SOUTH);

		back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				// TODO Auto-generated method stub
				connection_.sendMessage("disconnected" + " " + PlayerName_);
				disconnected_ = true;
				waitingRoom_.setVisible(false);
				mainFrame_.setVisible(true);

				opponetStatus_.setBorder(new LineBorder(Color.WHITE));
				opponetStatus_.setBackground(null);

				opponetStatus_.setEnabled(true);
			}

		});

		back1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				// TODO Auto-generated method stub
				System.out.println("This  'observer' was called at line 776");
				connection_.sendMessage("observer");
				connection_.sendMessage("disconnected" + " " + PlayerName_);
				disconnected_ = true;
				waitingRoom_.setVisible(false);
				mainFrame_.setVisible(true);

				opponetStatus_.setBorder(new LineBorder(Color.WHITE));
				opponetStatus_.setBackground(null);

				opponetStatus_.setEnabled(true);
			}

		});

		PlayOnline.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				// TODO Auto-generated method stub

				playOnline_ = true;
				gameSpecs();

				CommunicationThread connection = new CommunicationThread();
				ReadThread input = new ReadThread();

				connection.start();
				input.start();

				mainFrame_.setVisible(false);
				waitingRoom_.setVisible(true);
				playOnline_ = true;
				waitingRoom_.addWindowListener(new WindowAdapter() {

					public void windowClosing ( WindowEvent evt ) {
						dispose();
						connection_.sendMessage("disconnected" + " " + PlayerName_); // Send
						                                                             // a
						                                                             // disconnect
						                                                             // message
						// to
						// the hub.
						disconnected_ = true;
						playOnline_ = false;
						try {
							Thread.sleep(333); // Wait one-half second to allow the message
							                   // to
							                   // be sent.
						} catch ( InterruptedException e ) {}
						System.exit(0);
					}
				});

				// }
				// create the online signal status
				opponetStatus_.setBorder(new LineBorder(Color.WHITE));
				opponetStatus_.setEnabled(false);
				opponetStatus_.setBackground(Color.GREEN);

				rightButtons_.add(opponetStatus_);

			}
		});

		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent e ) {
				// TODO Auto-generated method stub
				String opponent = (String) cBox.getSelectedItem();

				if ( opponent.equals(PlayerName_) ) {
					System.out.println("Don't ever play yourself...");
				} else {
					System.out.println("you selected " + opponent + " as your opponent.");
					playerID_ = 1;// sets this player (challenger) as player 1
					connection_.sendMessage("start" + " " + PlayerName_ + " " + opponent);

				}
			}
		});

		play1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent e ) {
				// TODO Auto-generated method stub
				String gameplayers = (String) observeBox_.getSelectedItem();

				
					System.out.println("you selected " + gameplayers
					    + " as your game to observe.");
					
					System.out.println("String being sent to start observation is:    " + "startobserver " + PlayerName_ + " " + gameplayers);
					connection_.sendMessage("startobserver " + PlayerName_ + " " + gameplayers);
					mainFrame_.setVisible(false);
					gui.setVisible(true);
					winnerGomoku_ = false;
					type = false;
				
			}
		});
		PlayWithComputer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				// TODO Auto-generated method stub
				useComputer_ = true;

				comp_ = new Computer(game_.getBoard(),game_);
				JPanel myPanel = new JPanel();
				myPanel.setLayout(new GridLayout(3,1,5,5));
				Object[] options = { "Pente", "Gomoku", "Ala Carte", "cancle" };
				int n = JOptionPane
				    .showOptionDialog(myPanel,"Which rule do you want to choose?",
				                      "game variations",JOptionPane.DEFAULT_OPTION,
				                      JOptionPane.QUESTION_MESSAGE,null,options,
				                      options[2]);

				if ( n == 0 ) {
					mainFrame_.setVisible(false);
					gui.setVisible(true);
					winnerGomoku_ = false;
					type = false;
					numberOfConnection_ = 5;
					numberOfCapture_ = 5;
				} else if ( n == 1 ) {
					winnerGomoku_ = true;
					mainFrame_.setVisible(false);
					gui.setVisible(true);
					type = true;
					numberOfConnection_ = 5;
				} else if ( n == 2 ) {
					System.out.println("work");
					JTextField xField = new JTextField(5);
					JTextField yField = new JTextField(5);
					JTextField zField = new JTextField(5);

					JPanel myPanel1 = new JPanel();
					myPanel1.setLayout(new GridLayout(3,1,5,5));
					myPanel1.add(new JLabel("Number of connections to win"));
					myPanel1.add(xField);
					myPanel1.add(new JLabel("Do you want Captures? Enter yes or no."));
					myPanel1.add(yField);
					myPanel1.add(new JLabel("Number of captures to win"));
					myPanel1.add(zField);

					// this prompts the player for the names of the players
					int result =
					    JOptionPane.showConfirmDialog(null,myPanel1,"Ala Carte",
					                                  JOptionPane.OK_CANCEL_OPTION);

					if ( result == JOptionPane.OK_OPTION ) {

						numberOfConnection_ = Integer.parseInt(xField.getText());

						ifCapture_ = yField.getText();

						if ( ifCapture_.equals("yes") ) {
							numberOfCapture_ = Integer.parseInt(zField.getText());
						} else if ( ifCapture_.equals("no") ) {
							numberOfCapture_ = 0;
						}

						System.out.println(numberOfConnection_ + "  " + ifCapture_ + "  "
						    + numberOfCapture_);
					}

					mainFrame_.setVisible(false);
					gui.setVisible(true);
					winnerGomoku_ = false;
					type = false;
				}

				opponetStatus_.setBorder(new LineBorder(Color.WHITE));
				opponetStatus_.setEnabled(true);
				opponetStatus_.setText("Online");

				rightButtons_.add(opponetStatus_);

			}
		});

		opponetStatus_.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new GridLayout(3,1,5,5));
				Object[] options = { "Pente", "Gomoku", "Ala Carte", "cancle" };
				int n = JOptionPane
				    .showOptionDialog(myPanel,"Which rule do you want to choose?",
				                      "game variations",JOptionPane.DEFAULT_OPTION,
				                      JOptionPane.QUESTION_MESSAGE,null,options,
				                      options[2]);

				if ( n == 0 ) {
					mainFrame_.setVisible(false);
					waitingRoom_.setVisible(true);
					winnerGomoku_ = false;
					type = false;
					numberOfConnection_ = 5;
					numberOfCapture_ = 5;
				} else if ( n == 1 ) {
					winnerGomoku_ = true;
					mainFrame_.setVisible(false);
					waitingRoom_.setVisible(true);
					type = true;
					numberOfConnection_ = 5;
				} else if ( n == 2 ) {
					System.out.println("work");
					JTextField xField = new JTextField(5);
					JTextField yField = new JTextField(5);
					JTextField zField = new JTextField(5);

					JPanel myPanel1 = new JPanel();
					myPanel1.setLayout(new GridLayout(3,1,5,5));
					myPanel1.add(new JLabel("Number of connections to win"));
					myPanel1.add(xField);
					myPanel1.add(new JLabel("Do you want Captures? Enter yes or no."));
					myPanel1.add(yField);
					myPanel1.add(new JLabel("Number of captures to win"));
					myPanel1.add(zField);

					// this prompts the player for the names of the players
					int result =
					    JOptionPane.showConfirmDialog(null,myPanel1,"Ala Carte",
					                                  JOptionPane.OK_CANCEL_OPTION);

					if ( result == JOptionPane.OK_OPTION ) {

						numberOfConnection_ = Integer.parseInt(xField.getText());

						ifCapture_ = yField.getText();

						if ( ifCapture_.equals("yes") ) {
							numberOfCapture_ = Integer.parseInt(zField.getText());
						} else if ( ifCapture_.equals("no") ) {
							numberOfCapture_ = 0;
						}
						mainFrame_.setVisible(false);
						gui.setVisible(true);
						winnerGomoku_ = false;
						type = false;
						System.out.println(numberOfConnection_ + "  " + ifCapture_ + "  "
						    + numberOfCapture_);
					}
				}
				// ----------------------//
				// create the online signal status
				gui.setVisible(false);
			}

		});

		twoPersonGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				useComputer_ = false;
				JPanel myPanel = new JPanel();
				myPanel.setLayout(new GridLayout(3,1,5,5));
				Object[] options = { "Pente", "Gomoku", "Ala Carte", "cancle" };
				int n = JOptionPane
				    .showOptionDialog(myPanel,"Which rule do you want to choose?",
				                      "game variations",JOptionPane.DEFAULT_OPTION,
				                      JOptionPane.QUESTION_MESSAGE,null,options,
				                      options[2]);

				if ( n == 0 ) {
					mainFrame_.setVisible(false);
					gui.setVisible(true);
					winnerGomoku_ = false;
					type = false;
					numberOfConnection_ = 5;
					numberOfCapture_ = 5;
				} else if ( n == 1 ) {
					winnerGomoku_ = true;
					mainFrame_.setVisible(false);
					gui.setVisible(true);
					type = true;
					numberOfConnection_ = 5;
				} else if ( n == 2 ) {
					System.out.println("work");
					JTextField xField = new JTextField(5);
					JTextField yField = new JTextField(5);
					JTextField zField = new JTextField(5);

					JPanel myPanel1 = new JPanel();
					myPanel1.setLayout(new GridLayout(3,1,5,5));
					myPanel1.add(new JLabel("Number of connections to win"));
					myPanel1.add(xField);
					myPanel1.add(new JLabel("Do you want Captures? Enter yes or no."));
					myPanel1.add(yField);
					myPanel1.add(new JLabel("Number of captures to win"));
					myPanel1.add(zField);

					// this prompts the player for the names of the players
					int result =
					    JOptionPane.showConfirmDialog(null,myPanel1,"Ala Carte",
					                                  JOptionPane.OK_CANCEL_OPTION);

					if ( result == JOptionPane.OK_OPTION ) {

						numberOfConnection_ = Integer.parseInt(xField.getText());

						ifCapture_ = yField.getText();

						if ( ifCapture_.equals("yes") ) {
							numberOfCapture_ = Integer.parseInt(zField.getText());
						} else if ( ifCapture_.equals("no") ) {
							numberOfCapture_ = 0;
						}

						System.out.println(numberOfConnection_ + "  " + ifCapture_ + "  "
						    + numberOfCapture_);
					}

					mainFrame_.setVisible(false);
					gui.setVisible(true);
					winnerGomoku_ = false;
					type = false;
				}

				opponetStatus_.setBorder(new LineBorder(Color.WHITE));
				opponetStatus_.setEnabled(true);
				opponetStatus_.setText("Online");

				rightButtons_.add(opponetStatus_);

			}
		});

		observeGame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed ( ActionEvent arg0 ) {
				observe_ = true;
				gameSpecs();

				CommunicationThread connection = new CommunicationThread();
				ReadThread input = new ReadThread();

				connection.start();
				input.start();

				mainFrame_.setVisible(false);
				observeRoom_.setVisible(true);
				observeRoom_.addWindowListener(new WindowAdapter() {

					public void windowClosing ( WindowEvent evt ) {
						dispose();
						connection_.sendMessage("disconnected" + " " + PlayerName_); // Send
						                                                             // a
						                                                             // disconnect
						                                                             // message
						// to
						// the hub.
						disconnected_ = true;
						try {
							Thread.sleep(333); // Wait one-half second to allow the message
							                   // to
							                   // be sent.
						} catch ( InterruptedException e ) {}
						System.exit(0);
					}
				});

				JPanel comboboxPanel = new JPanel();
				comboboxPanel.setPreferredSize(new Dimension(300,300));
				comboboxPanel.setBackground(new Color(0x77dd77));
				if(observeBox_==null) {
				observeBox_ = new JComboBox(filler);
				}
				observeBox_.setPreferredSize(new Dimension(300,50));

				comboboxPanel.add(observeBox_);
				observeRoom_.add(comboboxPanel,BorderLayout.CENTER);
				int i = 0;
				if ( i == JOptionPane.OK_OPTION ) {
					observeRoom_.setVisible(true);
					observeRoom_.pack();

				}

			}

		});

		Player1Capture_ = new JTextField(" Player 1: " + game_.getCapture(1));
		Player1Capture_.setBackground(new Color(255,179,186));
		Player1Capture_.setEditable(false);
		Player1Capture_.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		Player2Capture_ = new JTextField(" Player 2: " + game_.getCapture(2));
		Player2Capture_.setBackground(new Color(255,179,186));
		Player2Capture_.setEditable(false);
		Player2Capture_.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		Player1Capture_.setHorizontalAlignment(JTextField.CENTER);
		Player2Capture_.setHorizontalAlignment(JTextField.CENTER);

		timeElapsed_ = "" + counter_;

		time_ = new JTextField(" Time Elapsed: " + timeElapsed_);
		time_.setHorizontalAlignment(JTextField.CENTER);
		time_.setBackground(new Color(255,255,186));

		bottom_.setBackground(Color.black);
		top_.setBackground(Color.black);
		left_.setBackground(Color.black);
		right_.setBackground(Color.black);

		centerPanel_.add(top_,BorderLayout.NORTH);
		centerPanel_.add(bottom_,BorderLayout.SOUTH);
		centerPanel_.add(left_,BorderLayout.WEST);
		centerPanel_.add(right_,BorderLayout.EAST);

		centerPanel_.add(grid_,BorderLayout.CENTER);

		gui.add(centerPanel_,BorderLayout.CENTER);

		playerMode_.setEditable(false);
		playerMode_.setHorizontalAlignment(JTextField.CENTER);

		capture_.setEditable(false);
		capture_.setHorizontalAlignment(JTextField.CENTER);
		capture_.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		capture_.setBackground(new Color(255,179,186));

		time_.setEditable(false);
		capture_.setHorizontalAlignment(JTextField.CENTER);

		rightButtons_.add(playerMode_);
		rightButtons_.add(capture_);
		rightButtons_.add(Player1Capture_);
		rightButtons_.add(Player2Capture_);
		rightButtons_.add(time_);

		newGame_.addActionListener(new Listener());
		newGame_.setBorder(new LineBorder(Color.WHITE));

		rightButtons_.add(newGame_);

		rules_.addActionListener(new Listener());
		rules_.setBorder(new LineBorder(Color.WHITE));
		rightButtons_.add(rules_);

		menu.addActionListener(new Listener());
		menu.setBorder(new LineBorder(Color.WHITE));
		rightButtons_.add(menu);

		gui.add(rightButtons_,BorderLayout.EAST);

		while ( true ) {
			if ( counter_ >= 60 ) {
				counter_ = 0;
				counterSide_++;
			}

			counter_++;

			timeElapsed_ = counterSide_ + ":" + counter_;
			time_.setText(" Time Elapsed: " + timeElapsed_);

			try {
				Thread.sleep(1000);

			} catch ( InterruptedException e ) {

			}
		}

	}

	public String getName () {
		return PlayerName_;

	}

	public void addName ( String name ) {
		for ( int i = 0 ; i < PlayerNames_.length ; i++ ) {
			if ( PlayerNames_[i] == null ) {
				PlayerNames_[i] = name;

				break;
			}
		}
	}

	/**
	 * Grid will be the Pente Board were the user will be able to put stones on
	 * the board.
	 */
	public class Grid extends JPanel
	    implements MouseMotionListener, MouseListener, Serializable {

		boolean drawCircle_;
		int newX_;
		int newY_;

		Grid () {

			addMouseMotionListener(this);
			addMouseListener(this);
			drawCircle_ = false;
			newX_ = -1;
			newY_ = -1;
			currPlayer_ = 2;
		}

		public void paintComponent ( Graphics g ) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0,0,1100,1100);
			g.setColor(Color.BLACK);

			for ( double i = 50 ; i < 1000 ; i = i + 900 / 18 ) { // the space between
				// every line is
				// 900/9
				g.drawLine(50,(int) i,950,(int) i);
			}
			for ( double ii = 50 ; ii < 1000 ; ii = ii + 900 / 18 ) {
				g.drawLine((int) ii,50,(int) ii,950);
			}

			for ( int ii = 0 ; ii < 19 ; ii++ ) {
				for ( int i = 0 ; i < 19 ; i++ ) {
					if ( Math.abs(mouseX_ - vertexX_.get(ii)) < 10
					    && Math.abs(mouseY_ - vertexY_.get(i)) < 10 ) {

						g.setColor(new Color(255,255,0,200)); // yellow circle
						double x = vertexX_.get(ii);
						double y = vertexY_.get(i);
						g.fillOval((int) x - 25,(int) y - 25,50,50);
					}
				}

			}

			// redraws grid

			for ( int i = 0 ; i < board_.getNumX() ; i++ ) {
				for ( int j = 0 ; j < board_.getNumY() ; j++ ) {
					int stone = board_.getStone(i,j);
					if ( stone == 1 ) {
						g.setColor(Color.black);
						double x = vertexX_.get(i);
						double y = vertexY_.get(j);
						g.fillOval((int) x - 25,(int) y - 25,50,50);
					} else if ( stone == 2 ) {
						g.setColor(Color.white);
						double x = vertexX_.get(i);
						double y = vertexY_.get(j);
						g.fillOval((int) x - 25,(int) y - 25,50,50);

					}
				}
			}

			// Updates the right side button panel
			if ( game_.getCurrentPlayer() == 1 ) {
				// executed when the 1 player is playing
				playerMode_.setText("Current Player: " + game_.getCurrentPlayer());
				playerMode_.setBackground(Color.black);
				playerMode_.setForeground(Color.WHITE);
			} else if ( game_.getCurrentPlayer() == 2 ) {
				// executed when the 2 player is playing
				playerMode_.setText("Current Player: " + game_.getCurrentPlayer());
				playerMode_.setBackground(Color.WHITE);
				playerMode_.setForeground(Color.BLACK);
			}

			Player1Capture_.setText(" Player 1: " + game_.getCapture(1));
			Player2Capture_.setText(" Player 2: " + game_.getCapture(2));

		}

		/*
		 * (non-Javadoc)
		 * @see
		 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
		 */
		public void mouseMoved ( MouseEvent e ) {

			// higlight_ = true;
			// mouseX_ = e.getX();
			// mouseY_ = e.getY();
			// repaint();

		}

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.
		 * MouseEvent)
		 */

		public void mouseDragged ( MouseEvent e ) {

		}

		/*
		 * (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */

		public void mouseClicked ( MouseEvent evt ) {

			if ( playOnline_ ) {
				System.out.println("player clicking:" + playerID_
				    + " current player's turn:" + game_.getCurrentPlayer());
				if ( game_.getCurrentPlayer() == playerID_ ) {
					doMove(evt);
					repaint();
				}
			} else {
				doMove(evt);
				repaint();
			}
		}

		public void mouseEntered ( MouseEvent e ) {

		}

		public void mouseExited ( MouseEvent e ) {

		}

		public void mousePressed ( MouseEvent e ) {

		}

		public void mouseReleased ( MouseEvent e ) {}

		/**
		 * Called when user clicks on board or buttons
		 * 
		 * @param evt
		 *          event holding the info about the mouse click
		 */
		public void doMove ( MouseEvent evt ) {

			// start of a click on the grid
			if ( winnerGomoku_ == false ) {// goes through game using pente rules

				int x = evt.getX(); // x value where user clicked.
				int y = evt.getY(); // y value where user clicked.

				newX_ = -1;
				newY_ = -1;

				for ( int i = 0 ; i < 19 ; i++ ) {
					for ( int j = 0 ; j < 19 ; j++ ) {
						if ( Math.abs(x - vertexX_.get(i)) < 10
						    && Math.abs(y - vertexY_.get(j)) < 10 ) {
							newX_ = (int) (vertexX_.get(i) / 50.0) - 1;
							newY_ = (int) (vertexY_.get(j) / 50.0) - 1;
						}
					}
				}

				// if the values are a position on the board
				if ( newX_ >= 0 && newY_ >= 0 ) {

					currPlayer_ = game_.getCurrentPlayer(); // gets the current player
					                                        // in

					// the game
					if ( !game_.placeStone(newX_,newY_) ) { // stone is not in a valid
						// position
						System.out.println("please click in a valid position");
					} else { // stone is in a valid position
						repaint();
						drawCircle_ = true;// so paint will draw the circle
						if ( drawCircle_ ) {
							System.out.println("drawcircle is true and should be drawing.");
						}

						// checks for winner after stone is placed, if not, continues game
						if ( !game_.isWinner(numberOfConnection_,numberOfCapture_) ) {

							if ( useComputer_ ) {
								comp_.move();
							} else {
								System.out.println("switching from player "
								    + game_.getCurrentPlayer());
								game_.startRound();// switches to next player
								System.out
								    .println("Changed to player " + game_.getCurrentPlayer());
								grid_.repaint();
								// send move message to server
								sendGameState();
							}
						} else {
							// pop up box to show winner
							newGameExitPrompt();
						}
					}
				} // end of if values are on the board

			} // end of if there is no winner

			// same as above code but checks for Gomoko rules
			else if ( winnerGomoku_ == true ) {
				int x = evt.getX(); // x value where user clicked.
				int y = evt.getY(); // y value where user clicked.

				newX_ = -1;
				newY_ = -1;

				for ( int i = 0 ; i < 19 ; i++ ) {
					for ( int j = 0 ; j < 19 ; j++ ) {
						if ( Math.abs(x - vertexX_.get(i)) < 10
						    && Math.abs(y - vertexY_.get(j)) < 10 ) {
							newX_ = (int) (vertexX_.get(i) / 50.0) - 1;
							newY_ = (int) (vertexY_.get(j) / 50.0) - 1;
						}
					}
				}

				if ( newX_ >= 0 && newY_ >= 0 ) { // if the values are a position on
				                                  // the
					// board

					currPlayer_ = game_.getCurrentPlayer(); // gets the current player
					                                        // in
					// the game
					if ( !game_.placeStone(newX_,newY_) ) { // stone is not in a valid
						// position
						System.out.println("please click in a valid position");
					} else { // stone is in a valid position
						drawCircle_ = true; // so paint will draw the circle

						if ( !game_.isWinnerGomoku() ) {
							if ( useComputer_ ) {
								comp_.move();
							} else {
								game_.startRound(); // switches to next player turn
								repaint();
								// send move message to server
								sendGameState();
							}
						} else {
							// pop up box to show winner
							newGameExitPrompt();
						}
					}
				}

			}

			// once the mouse is clicked, place a stone in a valid location
			// if the stone is not in a valid position-do nothing
			// if it is in a valid position, check for winner (captures/five
			// adjacent)
			// if there is a winner, get player number and do something
			// call new round (change current player)

			repaint();
		}

	}

	/**
	 * Prompts player to start new game or exit
	 */
	public void newGameExitPrompt () {
		Object[] options = { "New Game", "Exit" };
		int response = JOptionPane
		    .showOptionDialog(null,"Player " + game_.getCurrentPlayer()
		        + " has won the game! Do you want to start a new game?","Winner!",
		                      JOptionPane.YES_NO_OPTION,
		                      JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		if ( response == 0 ) {
			game_ = new Game();
			board_ = game_.getBoard();
			counter_ = 0;
			repaint();
			if ( playOnline_ ) {

				sendGameState();
			}
		} else if ( response == 1 ) {
			if ( playOnline_ ) {
				// send message to server to end
				// send disconnected
			}
			System.exit(0);

		}
	}

	/*
	 * Listener will listen for any clicks on the window.
	 */
	public class Listener implements ActionListener {

		Listener () {

		}

		public void actionPerformed ( ActionEvent e ) {

			if ( e.getActionCommand().equals(" New Game ") ) {

				int responseToNewGame = JOptionPane
				    .showConfirmDialog(null,
				                       "Are you sure you want to start a new game?",
				                       "Confirm",JOptionPane.YES_NO_OPTION,
				                       JOptionPane.QUESTION_MESSAGE);
				if ( responseToNewGame == JOptionPane.NO_OPTION ) {
					System.out.println("No button clicked");
				} else if ( responseToNewGame == JOptionPane.YES_OPTION ) {
					game_ = new Game();
					board_ = game_.getBoard();
					counter_ = 0;
					mainMenu.setVisible(true);
					grid_.repaint();
					repaint();

					System.out.println("Yes button clicked");
				} else if ( responseToNewGame == JOptionPane.CLOSED_OPTION ) {
					System.out.println("JOptionPane closed");
				}

			} else if ( e.getActionCommand().equals(" Rules ") ) { //// (http://www.pente.net/instructions.html)
				if ( winnerGomoku_ == false ) {
					// rules of the game
					String message = "\"OBJECT OF THE GAME:\"\n" + " \n"
					    + "Win by placing five (or more) of your stones in a row, vertically, \n"
					    + "horizontally, or diagonally, with no empty points between them. Or, \n"
					    + "win by capturing five (or more) pairs of your opponent's stones. \n"
					    + " \n"

					    + "\"HOW TO PLAY:\"\n" + " \n"
					    + "Play starts with the board completely clear of stones.\n"
					    + "The first player (black) begins the game by playing\n"
					    + "one stone on the center point. Thereafter the players\n"
					    + "take turns placing their stones, one at a time, on any\n"
					    + "empty intersection. The stones are placed on the intersections \n"
					    + "of the lines (including the very edge of the board), rather \n"
					    + "than in the squares. Once played, a stone cannot be moved \n"
					    + "again, except when removed by a capture. Players alternate \n"
					    + "turns adding new stones to the board, building up their \n"
					    + "positions, until one player wins." + " \n" + " \n"
					    + "\"CAPTURES:\"\n" + " \n"

					    + "Whenever your opponent has two stones (and only two) which are \n"
					    + "adjacent,those stones are vulnerable to capture. The pair can be \n"
					    + "captured by bracketing its two ends with your own stones. \n"
					    + "The captured stones are removed from the board.\n"
					    + "Captures can be made vertically, horizontally, or\n"
					    + "diagonally, and multiple captures can occur on a \n"
					    + "single move.\n" + " \n" + "\"WINNING THE GAME:\"\n" + " \n"
					    + "The game ends immediately when one player captures five \n"
					    + "pairs, or places five stones in a row. The opposing player \n"
					    + "has no “last chance” to make a final move.\n"
					    + "When a player obtains an unblocked row of four stones, called a \n"
					    + "tessera, a win is imminent. Therefore, an unblocked row of three stones, \n"
					    + "called a tria, is a serious threat that should be blocked unless a stronger o\n"
					    + "ffensive move exists. An unblocked row of three stones, if it contains one \n"
					    + "gap, is still considered a tria. In the example to the right, black has formed\n"
					    + "both a horizontal and a vertical tria, while white has formed a tessera and\n"
					    + "will win with the next move. \n" + " \n" + "\"ETIQUETTE:\"\n"
					    + " \n"
					    + "It is a customary, but not mandatory, refinement of this game to announce \n"
					    + "“three” or “tria” when moving to make an open three, and also to\n"
					    + "call “four” or “tessera” when making four in a row. This is so that \n"
					    + "one's opponent does not forget to stop the formation of an open four,\n"
					    + "or five—because there is no fun in winning a game owing to the\n"
					    + "blunder of the adversary; at least there shouldn't be. The idea is \n"
					    + "to win in spite of one's opponent seeing every threat. Pointing out\n"
					    + "a player's errant move also demonstrates one's own confidence \n"
					    + "and mastery of play. \n" + "\n"
					    + "Pente Net - How to play the game of Pente, www.pente.net/instructions.html."

					;
					JOptionPane.showMessageDialog(new JFrame(),message,"Rules",
					                              JOptionPane.INFORMATION_MESSAGE);
				} else if ( winnerGomoku_ == true ) {
					String message = "Gomoku is the simplest of the games. \n"
					    + "To win, place 5 of your stones in a straight continuous line \n"
					    + "(either horizontally, vertically or diagonally). \n"
					    + "Whoever does this first wins the game. One note, \n"
					    + "you must get exactly 5 in a row to win.\n"
					    + " 6 or more of your stones in a row is called an \"overline\" \n"
					    + "and does not count as a win, play continues.";
					JOptionPane.showMessageDialog(new JFrame(),message,"Rules",
					                              JOptionPane.INFORMATION_MESSAGE);
				}
			} else if ( e.getActionCommand().equals("Menu") ) {

				gui.setVisible(false);
				mainFrame_.setVisible(true);
			} else if ( e.getActionCommand().equals("Online") ) {

				JPanel myPanel = new JPanel();
				myPanel.setLayout(new GridLayout(3,1,5,5));
				Object[] options = { "Pente", "Gomoku", "cancle" };
				int n = JOptionPane
				    .showOptionDialog(myPanel,"Which rule do you want to choose?",
				                      "game variations",
				                      JOptionPane.YES_NO_CANCEL_OPTION,
				                      JOptionPane.QUESTION_MESSAGE,null,options,
				                      options[2]);

				if ( n == JOptionPane.YES_OPTION ) {
					mainFrame_.setVisible(false);
					waitingRoom_.setVisible(true);
					winnerGomoku_ = false;
					type = false;
				} else if ( n == JOptionPane.NO_OPTION ) {
					winnerGomoku_ = true;
					mainFrame_.setVisible(false);
					waitingRoom_.setVisible(true);
					type = true;
				}

			}

		}

	}

	/**
	 * @return the numberOfConnection
	 */
	public static int getNumberOfConnection () {
		return numberOfConnection_;
	}

	/**
	 * @param numberOfConnection
	 *          the numberOfConnection to set
	 */
	public void setNumberOfConnection ( int numberOfConnection ) {
		numberOfConnection_ = numberOfConnection;
	}

	/**
	 * @return the ifCapture
	 */
	public static String getIfCapture () {
		return ifCapture_;
	}

	/**
	 * @return the numberOfCapture
	 */
	public static int getNumberOfCapture () {
		return numberOfCapture_;
	}

	/**
	 * @return the player1Capture
	 */
	public JTextField getPlayer1Capture () {
		return Player1Capture_;
	}

	/**
	 * @return the player2Capture
	 */
	public JTextField getPlayer2Capture () {
		return Player2Capture_;
	}

	/**
	 * @return the win
	 */
	public static boolean isWin () {
		return win_;
	}

	/**
	 * @return the winnerGomoku
	 */
	public static boolean isWinnerGomoku () {
		return winnerGomoku_;
	}

	/**
	 * @return the gameInProgress
	 */
	public static boolean isGameInProgress () {
		return gameInProgress_;
	}

	/**
	 * @return the board
	 */
	public static Board getBoard () {
		return board_;
	}

	/**
	 * @return the playerMode
	 */
	public static JTextField getPlayerMode () {
		return playerMode_;
	}

}