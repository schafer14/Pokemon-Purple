package ai;

import static org.junit.Assert.*;

import org.junit.*;
import pkmn.*;

public class AICommonAITest 
{
	CommonAI artificial;
	PkmnPokemon blast;
	PkmnPokemon chariz;
	int blastHealth;
	int charizHealth;

	@Before
	public void setUp() 
	{
		artificial = new CommonAI();
		blast = PkmnPokemon.Blastoise;
		chariz = PkmnPokemon.Charizard;
		chariz = PkmnPokemon.Charizard;
		blastHealth = 500;
		charizHealth = 0;
	}
	
	@Test
	public void testDefineMode()
	{
		int mode = artificial.determineMode(blast, blastHealth);
		int expected = 2;
		assertTrue(mode == expected);
	}
	
	@Test
	public void testChooseMove()
	{
		PkmnMove move = artificial.chooseMove(blast, chariz, blastHealth);
		PkmnMove expected = PkmnMove.RockSlide;
		assertTrue(move == expected);
	}

}
