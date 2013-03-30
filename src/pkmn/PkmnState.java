package pkmn;

import java.util.*;

import game.*;

/**
 * A class that represents the state of a game.
 * 
 * @author Pokemon Purple Team
 * @version 20 November 2011
 */
public class PkmnState extends GameState
{
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



	/**
	 * Constructor for PkmnState
	 * 
	 * @param pokemonTeam the two teams of Pokemon ("team" also includes teams of one).
	 */
	public PkmnState(Vector<Vector<PkmnPokemon>> pokemonTeam,Vector<PkmnPokemon> currPokemon,
			Vector<Vector<Integer>> currHP, Vector<Vector<Integer[]>> currMovePP, 
			boolean hasMoved[], boolean missedAttack[], boolean isPkmnSelect, int gameWinner)
	{
		this.pokemonTeam=pokemonTeam;
		this.currPokemon=currPokemon;
		this.hasMoved=hasMoved;
		this.missedAttack = missedAttack;
		this.isPkmnSelect = isPkmnSelect; 
		this.gameWinner = gameWinner;

		//Initialize HP values
		this.currHP = currHP;
		
		this.currMovePP = currMovePP;
	}

	/**
	 * Get the Specified players Current pokemon
	 * 
	 * @return the specified player's current Pokemon 
	 */
	public PkmnPokemon getCurrPokemon(int player) {
		// return the specified Pokemon
		return currPokemon.get(player);
	}

	/**
	 * Get the specified player's current Pokemon's current health 
	 * 
	 * @return the specified player's current Pokemon's current health.
	 */
	public int getCurrHP(int player) {
		PkmnPokemon pokemon = getCurrPokemon(player);
		//finds the given pokemon out of the current pokemon
		for (PkmnPokemon thisPkmn:pokemonTeam.get(player)){
			if (thisPkmn.toString().equals(pokemon.toString())){
				// return the current Pokemon's health
				return currHP.get(player).get(pokemonTeam.get(player).indexOf(thisPkmn)).intValue();
			}
		}
		return -1; 
	}
	
	public int getCurrMovePP(int player, PkmnMove move)
	{
		PkmnPokemon pokemon = getCurrPokemon(player);
		//find the move
		for (int i = 0; i < 4; i++)
		{
			if(pokemon.moveset[i].toString().equals(move.toString()))
				//return the move pp
				return currMovePP.get(player).get(0)[i];
		}
		return -1; 
	}

	/**
	 * check whether it is in the pokemon select State 
	 * 
	 * @return true if it is in the pokemon select state, and false otherwise
	 */
	public boolean isPkmnSelect() {
		return isPkmnSelect;
	}
	
	/**
	 * Get one value out of the boolean array hasMoved
	 * 
	 * @param player
	 * @return hasMoved[player]
	 */
	public boolean getHasMoved(int player)
	{
		return hasMoved[player];
	}
	
	/**
	 * Get the boolean array representing
	 * if an attack missed.
	 * 
	 * @return missedAttack
	 */
	public boolean[] getMissedAttack()
	{
		return missedAttack;
	}
	
	/**
	 * returns the current game winner
	 * 
	 * @return the ID of the winner if there is one, otherwise -1
	 */
	public int gameWinner() {
		return gameWinner;
	}

	/**
	 * Get the specified Pokemon based on a given player and Pokemon number
	 * 
	 * @return the specified Pokemon 
	 */
	public PkmnPokemon getPokemon(int player, int pokemon) {
		// return the specified Pokemon
		return pokemonTeam.get(player).get(pokemon);
	}

	/**
	 * Get the specified Pokemon based on a given player and Pokemon 
	 * 
	 * @return the specified Pokemon 
	 */
	public PkmnPokemon getPokemon(int player,PkmnPokemon pokemon) {
		// finds the given pokemon
		for (PkmnPokemon thisPkmn:pokemonTeam.get(player)){
			if (thisPkmn.toString().equals(pokemon.toString())){
				return thisPkmn;
			}
		}
		return null; 
	}

	/**
	 * Get the specified Pokemon based on a given player and Pokemon name
	 * 
	 * @return the specified Pokemon 
	 */
	public PkmnPokemon getPokemon(int player,String pokemon) {
		// finds the given pokemon
		for (PkmnPokemon thisPkmn:pokemonTeam.get(player)){
			if (thisPkmn.toString().equals(pokemon)){
				return thisPkmn;
			}
		}
		return null; 
	}

	/**
	 * Get the specified Pokemon's health based on a given player and Pokemon number
	 * 
	 * @return the specified Pokemon's current health. 
	 */
	public int getHP(int player, int pokemon) {
		// return the specified Pokemon's health
		return currHP.get(player).get(pokemon).intValue();
	}

	/**
	 * Get the specified Pokemon's health based on a given player and Pokemon
	 * 
	 * @return the specified Pokemon's current health.
	 */
	public int getHP(int player,PkmnPokemon pokemon) {
		//finds the given pokemon out of the current pokemon
		for (PkmnPokemon thisPkmn:pokemonTeam.get(player)){
			if (thisPkmn.toString().equals(pokemon.toString())){
				// return the specified Pokemon's health
				return currHP.get(player).get(pokemonTeam.get(player).indexOf(thisPkmn)).intValue();
			}
		}
		return -1; 
	}

	/**
	 * Get the specified Pokemon's hp based on a given player and Pokemon name
	 * 
	 * @return the specified Pokemon's current health.
	 */
	public int getHP(int player,String pokemon) {
		//finds the given pokemon out of the current pokemon
		for (PkmnPokemon thisPkmn:pokemonTeam.get(player)){
			if (thisPkmn.toString().equals(pokemon)){
				// return the specified Pokemon's health
				return currHP.get(player).get(pokemonTeam.get(player).indexOf(thisPkmn)).intValue();
			}
		}
		return -1; 
	}

	/**
	 * Returns a string representation of the state.
	 * 
	 * @return a string representation of the state.
	 */
	public String toString(){
		return "has not been implemented";
	}

}
