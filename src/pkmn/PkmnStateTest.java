package pkmn;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * A class that checks the methods of the PkmnState class
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnStateTest {
	//the state to be used
	PkmnState myState;
	//double array of Pokemon, 
	//first index differentiates between Players, second contains each players Pokemon
	private Vector<Vector<PkmnPokemon>> pokemonTeam;
	//the current pokemon which each player has out
	private Vector<PkmnPokemon> currPokemon;
	//the current health of each pokemon on a team
	private Vector<Vector<Integer>> currHP;
	// the current PP of each move
	private Vector<Vector<Integer[]>> currMovePP;
	//Boolean arrays
	private boolean hasMoved[]; 
	private boolean missedAttack[];
	//whether game is in the selection stage
	private boolean isPkmnSelect; 
	//Who has won the game
	private int gameWinner;
	
	//Instantiate all the given game variables and state. 
	@Before
	public void setup()
	{
		//init team
		pokemonTeam = new Vector<Vector<PkmnPokemon>>();
		pokemonTeam.add(new Vector<PkmnPokemon>());
		pokemonTeam.add(new Vector<PkmnPokemon>());
		pokemonTeam.get(0).add(PkmnPokemon.Bulbasaur);
		pokemonTeam.get(1).add(PkmnPokemon.Pikachu);
		//init curr pkmn
		currPokemon=new Vector<PkmnPokemon>();
		currPokemon.add(pokemonTeam.get(0).get(0));
		currPokemon.add(pokemonTeam.get(1).get(0));
		//init curr hp
		currHP= new Vector<Vector<Integer>>();
		currHP.add(new Vector<Integer>());
		currHP.add(new Vector<Integer>());
		currHP.get(0).add(pokemonTeam.get(0).get(0).hp);
		currHP.get(1).add(pokemonTeam.get(1).get(0).hp);
		
		//init curr pp
		currMovePP= new Vector<Vector<Integer[]>>();
		currMovePP.add(new Vector<Integer[]>());
		currMovePP.add(new Vector<Integer[]>());
		currMovePP.get(0).add(new Integer[4]);
		currMovePP.get(1).add(new Integer[4]);
		for (int i =0; i<4;i++){
		currMovePP.get(0).get(0)[i]=(pokemonTeam.get(0).get(0).getMoveSet()[i].getPP());
		currMovePP.get(1).get(0)[i]=(pokemonTeam.get(1).get(0).getMoveSet()[i].getPP());
		}
		//init hasMoved
		hasMoved= new boolean[2];
		hasMoved[0]=false;
		hasMoved[1]=true;
		//init missedAttack
		missedAttack= new boolean[2];
		missedAttack[0]=false;
		missedAttack[1]=true;
		//init isPkmnSelect
		isPkmnSelect=false;
		//init gameWinner
		gameWinner=-1;
		
		myState= new PkmnState(pokemonTeam, currPokemon,currHP, currMovePP,
				hasMoved,missedAttack,isPkmnSelect,gameWinner);
	}
	
	/**
	 * test the getCurrPokemon
	 * 
	 */
	@Test
	public void getCurrPokemon() {
		//make sure the current pokemon returned by the state was the same one give to it
		assertEquals(currPokemon.get(0).toString(),myState.getCurrPokemon(0).toString());
		assertEquals(currPokemon.get(1).toString(),myState.getCurrPokemon(1).toString());
	}

	/**
	 * test the getCurrHP
	 * 
	 */
	@Test
	public void getCurrHP() {
		//make sure the current hp returned by the state was the same one give to it
		assertTrue(currHP.get(0).get(0)==myState.getCurrHP(0));
		assertTrue(currHP.get(1).get(0)==myState.getCurrHP(1));
		
	}
	
	/**
	 * test the getCurrMovePP
	 * 
	 */
	@Test
	public void getCurrMovePP()
	{
		//make sure the current pp returned by the state was the same one give to it
		for (int i=0; i<4;i++){ 
		assertTrue(currMovePP.get(0).get(0)[i]==
				myState.getCurrMovePP(0,currPokemon.get(0).moveset[i]));
		assertTrue(currMovePP.get(1).get(0)[i]==
				myState.getCurrMovePP(1,currPokemon.get(1).moveset[i]));
		}

	}

	/**
	 * test the isPkmnSelect
	 * 
	 */
	@Test
	public void isPkmnSelect() {
		//make sure the state gives the correct information about what phase the game is in
		assertEquals(isPkmnSelect,myState.isPkmnSelect());
		
	}
	
	/**
	 * test the getHasMoved
	 * 
	 */
	@Test
	public void getHasMoved()
	{
		//make sure the current information about who has moved returned by the state
		//is the same one give to it
		assertEquals(hasMoved[0],myState.getHasMoved(0));
		assertEquals(hasMoved[1],myState.getHasMoved(1));
	}
	
	/**
	 * test the getMissedAttack
	 * 
	 */
	@Test
	public void getMissedAttack()
	{
		//make sure the state gives the correct information about
		//whether the players attacks missed
		assertEquals(missedAttack[0],myState.getMissedAttack()[0]);
		assertEquals(missedAttack[1],myState.getMissedAttack()[1]);

	}

	/**
	 * test the gameWinner
	 * 
	 */
	@Test
	public void gameWinner() {
		//make sure the state gives the correct information about
		//the game winner
		assertEquals(gameWinner,myState.gameWinner());

	}

	/**
	 * test the getPokemon
	 * 
	 */
	@Test
	public void getPokemon() {
		//make sure the given pokemon returned by the state was the same one give to it
		assertEquals(pokemonTeam.get(0).get(0).toString(),myState.getPokemon(0,0).toString());
		assertEquals(pokemonTeam.get(1).get(0).toString(),myState.getPokemon(1,0).toString());

	}

	/**
	 * test the getHP
	 * 
	 */
	@Test
	public void getHP() {
		//make sure the given hp returned by the state was the same one give to it
		assertTrue(currHP.get(0).get(0)==myState.getHP(0,0));
		assertTrue(currHP.get(1).get(0)==myState.getHP(1,0));

	}

}
