package ai;

import static org.junit.Assert.assertTrue;
import org.junit.*;
import pkmn.*;

public class AIHealingAITest {

	CommonAI artificial;
	PkmnPokemon molt;
	PkmnPokemon chariz;
	int moltHealth;
	int charizHealth;

	@Before
	public void setUp() 
	{
		artificial = new HealingAI();
		molt = PkmnPokemon.Moltres;
		chariz = PkmnPokemon.Charizard;
		chariz = PkmnPokemon.Charizard;
		moltHealth = 300;
		charizHealth = 0;
	}
	
	@Test
	public void testDefineMode()
	{
		int mode = artificial.determineMode(molt, moltHealth);
		int expected = 2;
		assertTrue(mode == expected);
	}
	/**
	 * With the healths set at 300 and Moltres in balancedmode,
	 * we can ensure that the move will be a healing move.
	 */
	@Test
	public void testBalancedMode()
	{
		PkmnMove pm = artificial.chooseMove(molt,chariz,moltHealth);
		PkmnMove expected = PkmnMove.Roost;
		assertTrue(pm == expected);
	}
	
	@Test
	public void testDefensiveMode()
	{
		moltHealth = 50;
		PkmnMove pm = artificial.chooseMove(molt,chariz,moltHealth);
		PkmnMove expected = PkmnMove.Roost;
		assertTrue(pm == expected);
	}
}
