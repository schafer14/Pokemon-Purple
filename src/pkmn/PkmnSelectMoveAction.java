package pkmn;

import game.*;

/**
 * A PkmnSelectMoveAction is an action that is a "move" in a game (as opposed
 * to an administrative action, like "quit game") in which the player selects a Pokemon that
 *  they want to use.
 * 
 * @author Pokemon Purple Team
 * @version 20 November 2011
 */
public class PkmnSelectMoveAction extends GameMoveAction
{
	private PkmnPokemon chosenPokemon;
	
    /**
     * constructor for PkmnMoveAction
     *
     * @param source the player who created the action
     * @param chosenPokemon the Pokemon that the player selected
     */
	public PkmnSelectMoveAction(GamePlayer source, PkmnPokemon chosenPokemon) 
	{
		 // invoke superclass constructor to set source
	    super(source);
	    
	   this.chosenPokemon = chosenPokemon;
	}
	
    /**
     * get the Pokemon which was selected
     *
     * @return the Pokemon selected
     */
	public PkmnPokemon getPokemon()
	{
		return chosenPokemon;
	}
	
}
