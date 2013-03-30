package pkmn;

import game.*;

/**
 * A PkmnAttackMoveAction is an action that is a "move" in a game (as opposed
 * to an administrative action, like "quit game") in which the player selects a move which they want to use.
 * 
 * @author Pokemon Purple Team
 * @version 14 November 2011
 */
public class PkmnAttackMoveAction extends GameMoveAction
{
	private PkmnMove playerMove;
	
    /**
     * constructor for PkmnMoveAction
     *
     * @param source the player who created the action
     * @param playerMove the move that the player selected
     */
	public PkmnAttackMoveAction(GamePlayer source, PkmnMove playerMove) 
	{
		 // invoke superclass constructor to set source
	    super(source);
	    
	   this.playerMove = playerMove;
	   
	}
	
    /**
     * get the move which was selected
     *
     * @return the Move selected
     */
	public PkmnMove getMove()
	{
		return playerMove;
	}
	
}
