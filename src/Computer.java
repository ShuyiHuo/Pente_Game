
/**
 * @author Sergio Perez
 * @author Zoe Huo
 * @author Terrance Rose
 * @author Justin Lynch
 *
 */

public class Computer {

	Board board_; // the board to look through
	boolean inUse_; // is the computer inUse
	Game game_; // Create a Game of Pente

	/**
	 * The Computer constructer set's a computer game.
	 */
	public Computer(Board board, Game gm) {
		board_ = board;
		game_ = gm;

	}

	/**
	 * The computer's move
||||||| .r2965
=======
	/**
	 * The computer's move decides a place to place a stone in the pente board.
>>>>>>> .r2987
	 */

	public void move() {

		boolean canMove = true;

		while (canMove) {

			int randomX = (int) (Math.random() * 19);
			System.out.println("x " + randomX);
			int randomY = (int) (Math.random() * 19);
			System.out.println("y " + randomY);

			if (board_.placeStone(randomX, randomY, 1)) {

				canMove = false;
			} else {
			}
		}

	}

	/**
	 * @return boolean inUse_ the method getUse will return true if a location on a
	 *         board is already in use by a pente stone.
	 */
	public boolean getUse() {
		return inUse_;
	}
}
