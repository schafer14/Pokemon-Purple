package pkmn;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for PkmnHumanPlayer
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnHumanPlayerTest 
{
	PkmnHumanPlayer testHuman = null;
	
	@Before
	/**
	 * Create an instance of PkmnHumanPlayer to test
	 */
	public void setUp()
	{
		testHuman = new PkmnHumanPlayer();
	}
	
	@Test
	/**
	 * Make sure the default title is "Pokemon Purple"
	 */
	public void testDefaultTitle()
	{
		assertTrue(testHuman.defaultTitle().equals("Pokemon Purple"));
	}
	
	@Test
	/**
	 * Make sure the initial height is 650.
	 */
	public void testInitialHeight()
	{
		assertTrue(testHuman.initialHeight() == 650);
	}
	
	@Test
	/**
	 * Make sure the initial height is 1000.
	 */
	public void testInitialWidth()
	{
		assertTrue(testHuman.initialWidth() == 1000);
	}
	
	
}
