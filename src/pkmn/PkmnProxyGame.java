package pkmn;

import java.util.Vector;

import game.*;

/**
 * A PkmnGame object that is used as a proxy for the real game that is on another
 * machine on the network.  Each PkmnProxyGame is associated with exactly one
 * PkmnPlayer object.  Whenever a method is invoked on the PkmnProxyGame object,
 * it encodes the message as a string and sends it across the network; when
 * ever the PkmnProxyGame object receives a message from the network, it decodes
 * the string and invokes the corresponding method its PkmnPlayer.
 * 
 * @author Pokmeon Purple Team
 * @version 8 December 2000
 */
public class PkmnProxyGame extends ProxyGame implements PkmnGame
{
	public static final int PORT_NUM = 4788;
	
    /**
     * PkmnProxyGame constructor.
     *
     * @param hostName  the name of the remote site to where the actual
     *  game is running
     */
	public PkmnProxyGame(String hostName) 
	{
		super(hostName);
	}

    /**
     * tells whether the group of player may contain "empty" positions
     * such as in a poker game when there are empty chairs
     *
     * @return tells whether game may have null players
     */
	public boolean nullPlayersAllowed() 
	{
		return false;
	}

    /**
     * the minimum number of players a game can have
     *
     * @return the fewest number of players allowed
     */
	public int minPlayersAllowed() 
	{
		return 2;
	}

    /**
     * the maximum number of players a game can have
     *
     * @return the highest number of players allowed
     */
	public int maxPlayersAllowed() 
	{
		return 2;
	}

    /**
     * Transforms a string (e.g., sent over the network) into an appropriate
     * GameState object for the game.
     *
     * @param str  the string to decode
     * @return the resulting GameState object
     */
	protected GameState decodeState(String str)
	{
		
		Vector <Vector <PkmnPokemon>> pokemonTeam = new Vector ();//0
		pokemonTeam.add(new Vector <PkmnPokemon>());
		pokemonTeam.add(new Vector <PkmnPokemon>());
		Vector <PkmnPokemon> currPokemon = new Vector();//index0
		Vector <Vector<Integer>> currentHP = new Vector ();//index1
		currentHP.add(new Vector<Integer>());
		currentHP.add(new Vector<Integer>());
		Vector <Vector <Integer []>> currentMovePP = new Vector();//index2
		currentMovePP.add(new Vector<Integer[]>());
		currentMovePP.add(new Vector<Integer[]>());
		currentMovePP.get(0).add(new Integer[4]);
		currentMovePP.get(1).add(new Integer[4]);
		boolean [] hasMovedArray = new boolean[2];//index3
		boolean [] missedAttack = new boolean[2];//index4
		boolean isPokemonSelect = false;//index5
		int gameWinner = -1;//index6
		
		//break the string up by each section marked by a "."
		Integer index0 = str.indexOf(".", 0)+1;
		Integer index1 = str.indexOf(".", index0+1);
		Integer index2 = str.indexOf(".", index1+1)+1;
		Integer index3 = str.indexOf(".", index2+1);
		Integer index4 = str.indexOf(".", index3+1);
		Integer index5 = str.indexOf(".", index4+1);
		Integer index6 = str.indexOf(".", index5+1);

		//pokemonTeam
		String team = str.substring(0, str.indexOf("."));
		String pokemon0 = team.substring(0, team.indexOf(","));
		String pokemon1 = team.substring(team.indexOf(",")+1);
		for(PkmnPokemon pkmn : PkmnPokemon.values())
		{
			if (pokemon0.equals(pkmn.toString()))
			{
				pokemonTeam.get(0).add(pkmn);
			}//if pokemon2
			if(pokemon1.equals(pkmn.toString()))
			{
				pokemonTeam.get(1).add(pkmn);
			}//if pokemon1	
		}//for


		//currPokemon
		String curr = str.substring(index0, str.indexOf(".", index0));
		String currPokemon0 = curr.substring(0, curr.indexOf(","));
		String currPokemon1 = curr.substring(curr.indexOf(",")+1);
		PkmnPokemon pkmn0=null;
		PkmnPokemon pkmn1=null;
		for(PkmnPokemon pkmn : PkmnPokemon.values())
		{
			if (currPokemon0.equals(pkmn.toString()))
			{
				pkmn0= pkmn;
			}//if pokemon2
			if(currPokemon1.equals(pkmn.toString()))
			{
				pkmn1= pkmn;
			}//if pokemon1	
		}//for
		currPokemon.add(pkmn0);
		currPokemon.add(pkmn1);

		//currHP
		String currHP = str.substring(index1+1, str.indexOf(".", index1+1));
		String currHP0 = currHP.substring(0, currHP.indexOf(","));
		String currHP1 = currHP.substring(currHP.indexOf(",")+1);
		Integer temp0 = Integer.valueOf(currHP0);
		Integer temp1 = Integer.valueOf(currHP1);
		currentHP.get(0).add(temp0);
		currentHP.get(1).add(temp1);

		//currMovePP
		String currMovePP = str.substring(index2, str.indexOf(".", index2));
		//index of each of the eight move pps
		//pokemon0
		Integer index0PP0 = currMovePP.indexOf(",", 0);
		Integer index0PP1 = currMovePP.indexOf(",", index0PP0+1);
		Integer index0PP2 = currMovePP.indexOf(",", index0PP1+1);
		Integer index0PP3 = currMovePP.indexOf(",", index0PP2+1);
		//pokemon1
		Integer index1PP0 = currMovePP.indexOf(",", index0PP3+1);
		Integer index1PP1 = currMovePP.indexOf(",", index1PP0+1);
		Integer index1PP2 = currMovePP.indexOf(",", index1PP1+1);
		Integer index1PP3 = currMovePP.indexOf(",", index1PP2+1);

		//pokemon0
		String pokemon0PP0 = currMovePP.substring(0, currMovePP.indexOf(","));
		String pokemon0PP1 = currMovePP.substring(index0PP0+1,
				currMovePP.indexOf(",", index0PP0 +1 ));
		String pokemon0PP2 = currMovePP.substring(index0PP1+1,
				currMovePP.indexOf(",", index0PP1 +1));
		String pokemon0PP3 = currMovePP.substring(index0PP2+1,
				currMovePP.indexOf(",", index0PP2 +1));
		//pokemon1
		String pokemon1PP0 = currMovePP.substring(index0PP3+1,
				currMovePP.indexOf(",", index0PP3 +1));
		String pokemon1PP1 = currMovePP.substring(index1PP0+1,
				currMovePP.indexOf(",", index1PP0 +1));
		String pokemon1PP2 = currMovePP.substring(index1PP1+1,
				currMovePP.indexOf(",", index1PP1 +1));
		String pokemon1PP3 = currMovePP.substring(index1PP2+1);
		
		//String to Integer
		Integer p0PP0 = Integer.valueOf(pokemon0PP0);
		Integer p0PP1 = Integer.valueOf(pokemon0PP1);
		Integer p0PP2 = Integer.valueOf(pokemon0PP2);
		Integer p0PP3 = Integer.valueOf(pokemon0PP3);
		Integer p1PP0 = Integer.valueOf(pokemon1PP0);
		Integer p1PP1 = Integer.valueOf(pokemon1PP1);
		Integer p1PP2 = Integer.valueOf(pokemon1PP2);
		Integer p1PP3 = Integer.valueOf(pokemon1PP3);
		//put the integers into the array
		currentMovePP.get(0).get(0)[0] = p0PP0;
		currentMovePP.get(0).get(0)[1] = p0PP1;
		currentMovePP.get(0).get(0)[2] = p0PP2;
		currentMovePP.get(0).get(0)[3] = p0PP3;
		currentMovePP.get(1).get(0)[0] = p1PP0;
		currentMovePP.get(1).get(0)[1] = p1PP1;
		currentMovePP.get(1).get(0)[2] = p1PP2;
		currentMovePP.get(1).get(0)[3] = p1PP3;

		//hasMoved
		String hasMoved = str.substring(index3+1, str.indexOf(".", index3+1));
		String hasMoved0 = hasMoved.substring(0, hasMoved.indexOf(","));
		String hasMoved1 = hasMoved.substring(hasMoved.indexOf(",")+1);
		boolean moved0 = Boolean.parseBoolean(hasMoved0);
		boolean moved1 = Boolean.parseBoolean(hasMoved1);
		hasMovedArray [0]= moved0;
		hasMovedArray [1]= moved1;

		//missedAttack
		String missed = str.substring(index4+1, str.indexOf(".", index4+1));
		String missed0 = missed.substring(0, missed.indexOf(","));
		String missed1 = missed.substring(missed.indexOf(",")+1);
		Boolean miss0 = Boolean.parseBoolean(missed0);
		Boolean miss1 = Boolean.parseBoolean(missed1);
		missedAttack[0] = miss0;
		missedAttack[1] = miss1;

		//isSelect
		String select = str.substring(index5+1, str.indexOf(".", index5+1));
		isPokemonSelect = Boolean.parseBoolean(select);

		//gameWinner
		String winner = str.substring(index6+1);
		if (winner.equals("-1")){
			gameWinner=-1;	
		}
		else gameWinner = Integer.valueOf(winner);
		
		return new PkmnState(pokemonTeam, currPokemon, currentHP, currentMovePP,
				             hasMovedArray, missedAttack, isPokemonSelect,
				             gameWinner);
	}

    /**
     * Transforms an action into a String object for sending over the
     * network.
     *
     * @param ga  the game action to be encoded.
     * @return the String encoding of the action
     */
	protected String encodeAction(GameAction ga)
	{
		
		String output="";
		if (ga instanceof PkmnAttackMoveAction)
		{
			PkmnAttackMoveAction attckAction= (PkmnAttackMoveAction)ga;
			output="Attack"+"~"+attckAction.getMove().toString();
		}
		else if (ga instanceof PkmnSelectMoveAction)
		{
			PkmnSelectMoveAction selectAction= (PkmnSelectMoveAction)ga;
			output="Select"+"~"+selectAction.getPokemon().toString();
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
		return PORT_NUM;
	}
}
