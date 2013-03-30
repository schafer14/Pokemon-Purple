package pkmn;

import static org.junit.Assert.*;
import org.junit.*;

/**
 * Test class for PkmnMove.
 * 
 * @author Pokemon Purple Team
 * @version 9 December 2011
 */
public class PkmnMoveTest 
{
	@Test
	/**
	 * Make sure that names are correctly set.
	 * This includes multi-word move names.
	 */
	public void testGetName()
	{
		assertTrue(PkmnMove.AerialAce.getName() == "Aerial Ace");
		assertTrue(PkmnMove.Blizzard.getName() == "Blizzard");
		assertTrue(PkmnMove.GigaDrain.getName() == "Giga Drain");
		assertTrue(PkmnMove.Roost.getName() == "Roost");
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct type.
	 */
	public void testGetType()
	{
		assertTrue(PkmnMove.Blizzard.getType().equals(PkmnType.ICE));
		assertTrue(PkmnMove.Roost.getType().equals(PkmnType.FLYING));
		assertTrue(PkmnMove.GigaDrain.getType().equals(PkmnType.GRASS));
		assertTrue(PkmnMove.HiddenPower.getType().equals(PkmnType.VARIABLE));
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct base.
	 */
	public void testGetBase()
	{
		assertTrue(PkmnMove.AerialAce.getBase() == 0);
		assertTrue(PkmnMove.Blizzard.getBase() == 1);
		assertTrue(PkmnMove.GigaDrain.getBase() == 1);
		assertTrue(PkmnMove.Roost.getBase() == 2);
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct power.
	 */
	public void testGetPower()
	{
		assertTrue(PkmnMove.AerialAce.getPower() == 60);
		assertTrue(PkmnMove.Blizzard.getPower() == 120);
		assertTrue(PkmnMove.GigaDrain.getPower() == 75);
		assertTrue(PkmnMove.Roost.getPower() == 0);
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct accuracy.
	 */
	public void testGetAccuracy()
	{
		assertTrue(PkmnMove.AerialAce.getAccuracy() == 100);
		assertTrue(PkmnMove.Blizzard.getAccuracy() == 70);
		assertTrue(PkmnMove.GigaDrain.getAccuracy() == 100);
		assertTrue(PkmnMove.Roost.getAccuracy() == 100);
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct amount of PP.
	 */
	public void testGetPP()
	{
		assertTrue(PkmnMove.AerialAce.getPP() == 20);
		assertTrue(PkmnMove.Blizzard.getPP() == 5);
		assertTrue(PkmnMove.GigaDrain.getPP() == 10);
		assertTrue(PkmnMove.Roost.getPP() == 10);
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct priority.
	 */
	public void testGetPriority()
	{
		assertTrue(PkmnMove.AquaJet.getPriority() == 1);
		assertTrue(PkmnMove.Blizzard.getPriority() == 0);
		assertTrue(PkmnMove.ExtremeSpeed.getPriority() == 2);
		assertTrue(PkmnMove.QuickAttack.getPriority() == 1);
	}
	
	@Test
	/**
	 * Make sure a sample of moves have the correct op code.
	 */
	public void testGetOpCode()
	{
		assertTrue(PkmnMove.StoneEdge.getOpCode() == 4);
		assertTrue(PkmnMove.Blizzard.getOpCode() == 0);
		assertTrue(PkmnMove.GigaDrain.getOpCode() == 3);
		assertTrue(PkmnMove.Roost.getOpCode() == 2);
	}
}
