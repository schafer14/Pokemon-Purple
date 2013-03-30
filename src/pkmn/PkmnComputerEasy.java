package pkmn;

import ai.*;

public class PkmnComputerEasy extends PkmnComputerPlayer
{
	public PkmnComputerEasy()
	{
		super();
		intel = new CommonAI();
	}//constructor
	
	protected void doRequestMove() 
	{
		/*
		 * Make sure the computer doesn't attempt to move before the human
		 * player has finished choosing his/her Pokemon.
		 */
		while(st.isPkmnSelect()) 
			{ try { sleep(1000); }finally{} }
		
		deffPokemon = st.getPokemon(opponent, 0);
		PkmnMove selectedMove = currPokemon.getMoveSet()[(int)(Math.random()*4)];
		
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
	
	public void pokemonSelect ()
	{
		
		if (getId() == 0) { opponent = PLAYER2; }
		else              { opponent = PLAYER1; }
		
		String[] available = {
				"Ivysaur",
				"Charmander",
				"Charmeleon",
				"Squirtle",
				"Wartortle",
				"Pikachu",
				"Haunter",
				"Cubone",
				"Scyther",
				"Jynx",
				"Electabuzz",
				"Magmar",
				"Pinsir",
				"Vaporeon",
				"Jolteon",
				"Flareon",
			};//available
		
			int select = (int)(Math.random()*17);
			for(PkmnPokemon pkmn : PkmnPokemon.values()) 
			{
				if(available[select].equals(pkmn.name()))
				{
					game.applyAction(new PkmnSelectMoveAction(thisPlayer, pkmn));
					currPokemon = pkmn;
				}
			}	
	}//pokemonSelect
}//PkmnComputerEasy
