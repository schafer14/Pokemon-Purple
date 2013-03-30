package pkmn;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for PkmnType.
 * 
 * @author Pokemon Purple Team
 * @version 9 December 2011
 */
public class PkmnTypeTest 
{
	
	@Test
	/**
	 * Make sure that a sample of the types have the right number.
	 */
	public void testGetNum()
	{
		assertTrue(PkmnType.FIRE.getNum() == 1);
		assertTrue(PkmnType.GHOST.getNum() == 13);
		assertTrue(PkmnType.DARK.getNum() == 15);
		assertTrue(PkmnType.VARIABLE.getNum() == 17);
		assertTrue(PkmnType.NULL.getNum() == 18);
	}

}
