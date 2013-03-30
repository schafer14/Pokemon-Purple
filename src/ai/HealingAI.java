package ai;

import pkmn.PkmnMove;
import pkmn.PkmnPokemon;

/**
 * HealingAI is an extension of CommonAI which will allow
 * the computer play to intelligently select healing moves.
 * 
 * Most classes are imported as is, defensive and balanced
 * modes are overloaded to provide the added functionality.
 * 
 * @author Pomkemon Purple
 * @version 1 January 1970
 *
 */
public class HealingAI extends CommonAI
{
	private boolean heal;
	private int counter;
	
	public HealingAI()
	{
		super();
		heal = true;
		counter = 1;
	}//constructor
	
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
		switch(counter)
		{
			case 1:
					heal = true;
					balancedMode(computer,defender);
					counter+=1;
					break;
			case 2:
					heal = true;
					balancedMode(computer,defender);
					counter+=1;
					break;
			case 3:
					aggressiveMode(computer, defender);
					counter=1;
					break;
		}
	}//defensiveMode

	/**
	 * When the AI is one with the Buddha, it will alternate between
	 * attack and heal moves!
	 * 
	 * @param computer
	 * @param defender
	 */
	private void balancedMode (PkmnPokemon computer, PkmnPokemon defender)
	{
		if(!heal)
		{
			aggressiveMode(computer, defender);
			heal = !heal;
		}
		else
		{
			for(PkmnMove pm : computer.getMoveSet())
			{
				if((pm.getOpCode() != 0) &&
					(pm.getOpCode() != 4))
				{
					currentMove = pm;
				}
			}
			heal = !heal;
		}
	}//balancedMode

}//HealingAI
