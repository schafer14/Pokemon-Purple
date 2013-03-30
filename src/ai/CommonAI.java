package ai;

import pkmn.*;

/**
 * CommonAI is the framework from which the computer will choose
 * moves for each individual Pokemon. Most Pokemon that 
 * the computer will control utilize this class to choose moves, 
 * however in certain cases it will be necessary to implement 
 * child classes that overload the balanced and defensive methods 
 * in order to flesh out their respective skill sets.
 *
 * Dustin Dalen wishes it to be known that this class is
 * beautiful.
 *
 * @author Pokemon Purple
 * @version 1 January 1970
 */
public class CommonAI
{
	//Some global final variables to define AI modes
	public static final int AGGRESSIVE = 0;
	public static final int DEFENSIVE = 1;
	public static final int BALANCED = 2;
	
	protected int currentMode;
	protected PkmnMove currentMove;
	
	public CommonAI()
	{
		currentMode = AGGRESSIVE;
	}//constructor
	
	/**
	 * chooseMove is the only method that should be called by
	 * ComputerPlayer. It's function is to determine the
	 * state of the controlled Pokemon, determine the move to use
	 * based upon that assessment, and return the selected move.
	 * 
	 * @param computer The computer player.
	 * @param defender The opponent player.
	 * @param int The current health of the computer player.
	 * @return The move that has been selected by the AI.
	 */
	public PkmnMove chooseMove (PkmnPokemon computer, PkmnPokemon defender, int health)
	{
		determineMode(computer, health);
		
		switch(currentMode)
		{
			case AGGRESSIVE:
				aggressiveMode(computer, defender);
				break;
			case BALANCED:
				balancedMode(computer, defender);
				break;
			case DEFENSIVE:
				defensiveMode(computer, defender);
				break;
		}
		return currentMove;
	}//chooseMove
	
	/**
	 * roughCalc calculates a _very_ rough estimate of 
	 * total damage done so that the AI can more or less
	 * determine what it's most effective move will be.
	 * 
	 * @param cp The computer player.
	 * @param dp The defending player.
	 * @return A rough estimate of total damage done.
	 */
	protected int roughCalc(PkmnPokemon cp, PkmnPokemon dp, PkmnMove pm)
	{		
		double typeEffect = 
				PkmnGameImpl.typeEffectiveness[pm.getType().getNum()]
				                              [dp.getType1().getNum()];
		
		return (int)((.84*(cp.getAtk()/dp.getDef())+2)*typeEffect);
	}//roughCalc
	
//==============================================================================//
//========================== Mode Definitions ==================================//
//==============================================================================//
	/**
	 * Depending on the current health of the Computer's Pokemon,
	 * this method will determine the appropriate mode for the
	 * Computer to base its decision on.
	 * 
	 * @param health The current health of the Pokemon.
	 */
	protected int determineMode (PkmnPokemon computer, int health)
	{
		double percent = ((double)(health)/(double)(computer.getHP()))*100;

		if (percent > 70)                          { currentMode = AGGRESSIVE; }
		else if ((percent < 70) && (percent > 30)) { currentMode = BALANCED;   }
		else                                       { currentMode = DEFENSIVE;  }
		
		return currentMode;
	}//determineMode

	/**
	 * We implement a simple highest value algorithm to determine
	 * the best possible move 
	 * 
	 * @param computer
	 * @param defender
	 */
	protected void aggressiveMode (PkmnPokemon computer, PkmnPokemon defender)
	{
		int highest = 0;
		for(PkmnMove pm : computer.getMoveSet())
		{
			int damage = roughCalc(computer, defender, pm);
			if (damage > highest)
			{
				highest = damage;
				currentMove = pm;
			}
		}
	}//aggressiveMode

	/**
	 * This does nothing for Pokemon that don't have stat-modifying
	 * moves, therefore the aggressiveMode method is called to simply choose
	 * a move as normal. This method should be overloaded in child classes 
	 * in order to create a more dynamic experience.
	 * 
	 * @param computer
	 * @param defender
	 */
	private void defensiveMode (PkmnPokemon computer, PkmnPokemon defender)
	{
		aggressiveMode(computer, defender); 
	}//defensiveMode

	/**
	 * This does nothing for Pokemon that don't have stat-modifying
	 * moves, therefore the aggressiveMode method is called to simply choose
	 * a move as normal. This method should be overloaded in child classes 
	 * in order to create a more dynamic experience.
	 * 
	 * @param computer
	 * @param defender
	 */
	private void balancedMode (PkmnPokemon computer, PkmnPokemon defender)
	{
		aggressiveMode(computer, defender); 
	}//balancedMode

//==============================================================================//	
//======================== End Mode Definitions ================================//
//==============================================================================//

}//CommonAi
