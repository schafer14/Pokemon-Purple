package pkmn;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * This class tests the PkmnGamImpl class
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnGameImplTest 
{
	//Instance of PkmnGameImpl to use for testing.
	PkmnGameImpl testImpl = null;
	
	//Other classes used to test PkmnGameImpl.
	PkmnHumanPlayer testHumanPlayer = null;
	PkmnComputerPlayer testComputerPlayer = null;
	
	@Before
	/**
	 * Create an instance of PkmnGameImpl with 2 players
	 * to test with.
	 */
	public void setUp()
	{
		testImpl = new PkmnGameImpl(2);
		testHumanPlayer = new PkmnHumanPlayer();
		testComputerPlayer = new PkmnComputerPlayer();
	}
	
	@Test
	/**
	 * Make sure null players are not allowed.
	 */
	public void testNullPlayersAllowed()
	{
		assertTrue(testImpl.nullPlayersAllowed() == false);
	}
	
	@Test
	/**
	 * Make sure 2 is the minimum number of players allowed.
	 */
	public void testMinPlayersAllowed()
	{
		assertTrue(testImpl.minPlayersAllowed() == 2);
	}
	
	@Test
	/**
	 * Make sure 2 is the maximum number of players allowed.
	 */
	public void testMaxPlayersAllowed()
	{
		assertTrue(testImpl.maxPlayersAllowed() == 2);
	}
	
	@Test
	/**
	 * Game is "over" when this is accessed.
	 */
	public void testGameOver()
	{
		assertTrue(testImpl.gameOver() == true);
	}
	
	@Test
	/**
	 * Human players should be able to quit.
	 * Computer players should not.
	 */
	public void testCanQuit()
	{
		assertTrue(testImpl.canQuit(testHumanPlayer) == true);
	}
	
	@Test
	/**
	 * Make sure checkIfCrit always returns 1 or 2.
	 */
	public void testCheckIfCrit()
	{
		assertTrue((testImpl.checkIfCrit(PkmnMove.AerialAce) == 1) || (testImpl.checkIfCrit(PkmnMove.AerialAce) == 2));
	}
}
