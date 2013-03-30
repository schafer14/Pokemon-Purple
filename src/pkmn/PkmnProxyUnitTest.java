package pkmn;

import game.GameAction;
import junit.framework.TestCase;

import org.junit.*;

import static org.junit.Assert.*;
/**
 * A class that checks the encode and decode of actions
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnProxyUnitTest {
	//the game to be tested
	PkmnProxyGame myGame;
	//player to be used as reference
	PkmnProxyPlayer myplayer;
	//cases to be tested
	public static final PkmnPokemon pkmn1=PkmnPokemon.Bulbasaur;
	public static final PkmnPokemon pkmn2=PkmnPokemon.Pikachu;
	public static final PkmnMove move1=pkmn1.moveset[0];
	public static final PkmnMove move2=pkmn1.moveset[1];
	public static final PkmnPokemon[] pkmn=PkmnPokemon.values();
	public static final PkmnMove[] moves= PkmnMove.values();
	//variables to store the different actions being tested 
	private PkmnSelectMoveAction select1;
	private PkmnSelectMoveAction select2;
	private PkmnAttackMoveAction att1;
	private PkmnAttackMoveAction att2;
	
	//initiate all the test variables. 
	@Before
	public void setup()
	{
		myGame= new PkmnProxyGame("George");
		myplayer = new PkmnProxyPlayer();
		
		select1= new PkmnSelectMoveAction(new PkmnProxyPlayer(), pkmn1);
		select2= new PkmnSelectMoveAction(new PkmnProxyPlayer(), pkmn2);
		att1= new PkmnAttackMoveAction(new PkmnProxyPlayer(), move1);
		att2= new PkmnAttackMoveAction(new PkmnProxyPlayer(), move2);
	}
	
	/**
	 * test the encodeAction
	 * 
	 */
	@Test
	public void encodeAction()
	{
		//represents a successful comparison, assume they were all successful
		boolean suc=true;
		// make sure that the encode states string is what is expected
		String encodeS1= myGame.encodeAction(select1);
		if (!encodeS1.equals("Select~"+pkmn1.toString()))
			suc=false;
		// make sure that the encode states string is what is expected
		String encodeS2= myGame.encodeAction(select2);
		if (!encodeS2.equals("Select~"+pkmn2.toString()))
			suc=false;
		// make sure that the encode states string is what is expected
		String encodeA1= myGame.encodeAction(att1);
		if (!encodeA1.equals("Attack~"+move1.toString()))
			suc=false;
		// make sure that the encode states string is what is expected
		String encodeA2= myGame.encodeAction(att2);
		if (!encodeA2.equals("Attack~"+move2.toString()))
			suc=false;
		//assert that they all have their expected values
		assertTrue(suc);
		
	}
	
	/**
	 * test the decodeAction
	 * 
	 */
	@Test
	public void decodeAction()
	{
		//represents a successful comparison, assume they were all successful
		boolean suc=true;
		//encode all the actions
		String encodeS1= myGame.encodeAction(select1);
		String encodeS2= myGame.encodeAction(select2);
		String encodeA1= myGame.encodeAction(att1);
		String encodeA2= myGame.encodeAction(att2);
		//decode al teh actions
		GameAction selectR1= myplayer.decodeAction(encodeS1);
		GameAction selectR2= myplayer.decodeAction(encodeS2);
		GameAction  attR1= myplayer.decodeAction(encodeA1);
		GameAction attR2= myplayer.decodeAction(encodeA2);
		//make sure the decoded actions are the right type
		if (!(selectR1 instanceof PkmnSelectMoveAction))suc=false;
		if (!(selectR2 instanceof PkmnSelectMoveAction))suc=false;
		if (!(attR1 instanceof PkmnAttackMoveAction))suc=false;
		if (!(attR2 instanceof PkmnAttackMoveAction))suc=false;
		
		//make sure the actions have the right value
		if(!(((PkmnSelectMoveAction)selectR1).getPokemon().toString().equals(pkmn1.toString())))
			suc=false;
		if(!(((PkmnSelectMoveAction)selectR2).getPokemon().toString().equals(pkmn2.toString())))
			suc=false;
		if(!(((PkmnAttackMoveAction)attR1).getMove().toString().equals(move1.toString())))
			suc=false;
		if(!(((PkmnAttackMoveAction)attR2).getMove().toString().equals(move2.toString())))
			suc=false;
		//assert that the actions were all correct
		assertTrue(suc);
		
	}
	
	
	
}
