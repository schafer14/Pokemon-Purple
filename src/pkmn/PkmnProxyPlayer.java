package pkmn;

import game.*;

/**
 * A PkmnPlayer object that is a proxy for the real player, which is somewhere
 * else on the network.
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnProxyPlayer extends ProxyPlayer implements PkmnPlayer
{


	/**
	 * Transforms a GameState object into a string so that it may be
	 * sent across the network.  
	 * 
	 * @param gs the GameState object
	 * @return the encoded string
	 */
	protected String encodeState(GameState gs) 
	{
		
		String theState = "";
		PkmnState ts = (PkmnState)gs;
		
		
		
		//pokemonteam
		PkmnPokemon firstPokemon = ts.getPokemon(0, 0);
		PkmnPokemon secondPokemon = ts.getPokemon(1, 0);
		theState = theState + firstPokemon.toString() + ","
				            + secondPokemon.toString() + ".";
		
		//currPokemon
		PkmnPokemon pokemon0 = ts.getCurrPokemon(0);
		PkmnPokemon pokemon1 = ts.getCurrPokemon(1);
		theState = theState + pokemon0.toString() + "," 
		                    + pokemon1.toString() + ".";
		
		//currHP
		Integer pokemon0HP = ts.getCurrHP(0);
		Integer pokemon1HP = ts.getCurrHP(1);
		theState = theState + pokemon0HP.toString() + ","
				            + pokemon1HP.toString() + ".";
		
		//currPP
		//pokemon 1
		Integer pokemon0PP0 = ts.getCurrMovePP(0, pokemon0.moveset[0]);
		Integer pokemon0PP1 = ts.getCurrMovePP(0, pokemon0.moveset[1]);
		Integer pokemon0PP2 = ts.getCurrMovePP(0, pokemon0.moveset[2]);
		Integer pokemon0PP3 = ts.getCurrMovePP(0, pokemon0.moveset[3]);
		//pokemon2
		Integer pokemon1PP0 = ts.getCurrMovePP(1, pokemon1.moveset[0]);
		Integer pokemon1PP1 = ts.getCurrMovePP(1, pokemon1.moveset[1]);
		Integer pokemon1PP2 = ts.getCurrMovePP(1, pokemon1.moveset[2]);
		Integer pokemon1PP3 = ts.getCurrMovePP(1, pokemon1.moveset[3]);
	
		theState = theState + pokemon0PP0.toString() + ","
				 			+ pokemon0PP1.toString() + ","
				 			+ pokemon0PP2.toString() + ","
				 			+ pokemon0PP3.toString() + ","
				 			+ pokemon1PP0.toString() + ","
				 			+ pokemon1PP1.toString() + ","
				 			+ pokemon1PP2.toString() + ","
				 			+ pokemon1PP3.toString() + ".";
		
		//has Moved
		Boolean hasMoved0 = ts.getHasMoved(0);
		Boolean hasMoved1 = ts.getHasMoved(1);
		theState = theState + hasMoved0.toString() + ","
				            + hasMoved1.toString() + ".";
		
		//missed Attack
		boolean [] missed = ts.getMissedAttack();
		Boolean missed0 = missed [0];
		Boolean missed1 = missed [1];
		theState = theState + missed0.toString() + ","
				            + missed1.toString() + ".";    
		
		//is select screen
		Boolean isSelect = ts.isPkmnSelect();
		theState = theState + isSelect.toString() + ".";
		
		//game winner 
		Integer winner = ts.gameWinner();
		theState = theState + winner.toString();
		    
		
		return theState;
	}
	/**
	 * Transforms a string into a GameAction object.
	 * 
	 * @param s the string to transform
	 * @return the GameAction object that corresponds to the string
	 */
	protected GameAction decodeAction(String s) 
	{
		
		GameAction  output = null; 
		String str = s.trim();
		int idx = str.indexOf("~"); // find first ~
		String actionType = str.substring(0,idx); // all characters before first ~
		String actionValue = str.substring(idx+1);// all characters after first ~

			//if its an PkmnAttackMoveAction
			if (actionType.equals("Attack")){
				PkmnMove playerMove=null;
				//all of the moves possible  
				PkmnMove[] moveset = PkmnMove.values();
				//find move in moveset
				for(int i=0; i< moveset.length;i++){
					if (moveset[i].toString().equals(actionValue)){
						playerMove=moveset[i];
					}
				}
				output=new PkmnAttackMoveAction(this,playerMove);
			}
			//if its an PkmnSelectMoveAction
			else if (actionType.equals("Select")){
				PkmnPokemon chosenPkmn=null;
				//find pokemon in out of entire pokemon vector 
				for(PkmnPokemon pkmn : PkmnPokemon.values()) 
				{
					if(actionValue.equals(pkmn.name()))
					{
						chosenPkmn=pkmn;
					}
				}
				output=new PkmnSelectMoveAction(this,chosenPkmn);
			}
		return output;
	}

	/**
	 * Get the default port number on the server machine that will be used
	 * in making the connection.
	 * 
	 * @return the default port number
	 */
	protected int getAdmPortNum() 
	{
		return PkmnProxyGame.PORT_NUM;
	}


}
