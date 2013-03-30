package pkmn;

import game.*;
import ai.CommonAI;

/**
 * The main class for the Pokemon AI. this class will auto-detect
 * the appropriate difficulty and import the necessary helper
 * classes.
 * 
 * @author Pokemon Purple Team
 * @version 1 January 1970
 */

public class PkmnComputerPlayer extends GameComputerPlayer implements PkmnPlayer
{
	protected static final int PLAYER1 = 0;
	protected static final int PLAYER2 = 1;
	
	protected int opponent;
	protected CommonAI intel;
	protected GamePlayer thisPlayer;
	protected PkmnState st;
	protected PkmnPokemon currPokemon;
	protected PkmnPokemon deffPokemon;
	
	/**
	 * Constructor
	 */
	public PkmnComputerPlayer()
	{
		super();
		thisPlayer = this;
		intel = null;
	}//constructor
	
    /**
     * This incredibly complex and obfuscated method calls the chooseMove method from CommonAI
     * which determines the best possible move for the computer. This move is then force fed
     * to the applyAction method (after that lazy bum sleeps for a few seconds) and the Computer 
     * goes on its merry way!
     */ 
	protected void doRequestMove() 
	{
		/*
		 * Make sure the computer doesn't attempt to move before the human
		 * player has finished choosing his/her Pokemon.
		 */
		while(st.isPkmnSelect()) 
			{ try { sleep(1000); }finally{} }
		
		deffPokemon = st.getPokemon(opponent, 0);
		PkmnMove selectedMove = intel.chooseMove(currPokemon, deffPokemon, st.getCurrHP(getId()));
		sleep(2000);
		
		if (st.getCurrMovePP(getId(), selectedMove) == 0)
		{
			game.applyAction(new PkmnAttackMoveAction(thisPlayer, 
													currPokemon.moveset[(int)(Math.random()*4)]));
		}
		else
		{
			game.applyAction(new PkmnAttackMoveAction(thisPlayer, selectedMove));
		}
		
	}//doRequestMove
	
	public void stateChanged(){
		try
		{
			st = (PkmnState)game.getState(this, 0);
		}finally{}
		
		if (st!=null)
		{	
			if (st.isPkmnSelect()) { pokemonSelect(); }
		}
    }//stateChanged
	
	/**
	 * Method used to select a Pokemon for the Computer Player
	 */
	public void pokemonSelect ()
	{
		//implemented by child classes
	}//pokemonSelect
}//PkmnComputerPlayer
