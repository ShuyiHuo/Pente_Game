

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testing Board methods.
 */
public class BoardTest {
	private Board board_;
	private GUI gui_;


	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown () throws Exception {}

	@Test
	public void testFiveStoneHorizontal () {
		board_.placeStone(9,9,2);
		board_.placeStone(8,9,2);
		board_.placeStone(7,9,2);
		board_.placeStone(10,9,2);
		board_.placeStone(11,9,2);
		boolean b = board_.checkNumberAdjacent(9,9,2, 5);
		assertEquals(b,true);

	}
	@Test
	public void testNumberStoneHorizontal () {
		board_.placeStone(9,9,2);
		board_.placeStone(8,9,2);
		board_.placeStone(7,9,2);

		boolean b = board_.checkNumberAdjacent(9,9,2,3);
		assertEquals(b,true);

	}

	@Test
	public void testFiveStoneVertical () {
		board_.placeStone(9,9,2);
		board_.placeStone(9,8,2);
		board_.placeStone(9,7,2);
		board_.placeStone(9,10,2);
		board_.placeStone(9,11,2);
		boolean b = board_.checkNumberAdjacent(9,9,2, 5);
		assertEquals(b,true);

	}

	@Test
	public void testNumberStoneVertical () {
		board_.placeStone(9,9,2);
		board_.placeStone(9,8,2);
		board_.placeStone(9,7,2);

		boolean b = board_.checkNumberAdjacent(9,9,2,3);
		assertEquals(b,true);

	}
	@Test
	public void testFiveStoneDiagonal1 () {
		board_.placeStone(9,9,2);
		board_.placeStone(8,8,2);
		board_.placeStone(7,7,2);
		board_.placeStone(10,10,2);
		board_.placeStone(11,11,2);
		boolean b = board_.checkNumberAdjacent(9,9,2, 5);
		assertEquals(b,true);

	}
	
	@Test
	public void testNumberStoneDiagonal1 () {
		board_.placeStone(9,9,2);
		board_.placeStone(8,8,2);
		board_.placeStone(7,7,2);
		boolean b = board_.checkNumberAdjacent(9,9,2, 3);
		assertEquals(b,true);

	}

	@Test
	public void testFiveStoneDiagonal2 () {
		board_.placeStone(9,9,2);
		board_.placeStone(10,8,2);
		board_.placeStone(11,7,2);
		board_.placeStone(8,10,2);
		board_.placeStone(7,11,2);
		boolean b = board_.checkNumberAdjacent(9,9,2,5);
		assertEquals(b,true);

	}
	@Test
	public void testNumberStoneDiagonal2 () {
		board_.placeStone(9,9,2);
		board_.placeStone(8,10,2);
		board_.placeStone(7,11,2);
		boolean b = board_.checkNumberAdjacent(9,9,2,3);
		assertEquals(b,true);

	}

}
