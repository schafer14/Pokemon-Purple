package pkmn;

import ai.*;

/**
 * This is the hard computer player, which is extremely
 * aggressive but intelligent enough to select healing
 * moves when the going gets tough. Most classes are 
 * imported straight from the parent class, however
 * the pokemonSelect method is implemented here to
 * allow for a larger pool of pokemon.
 * 
 * @author Pokemon Purple
 * @version 1 January 1970
 *
 */
public class PkmnComputerHard extends PkmnComputerPlayer
{
	public PkmnComputerHard()
	{
		super();
	}//constructor;
	
	public void pokemonSelect ()
	{
		
		if (getId() == 0) { opponent = PLAYER2; }
		else              { opponent = PLAYER1; }
		
		String[] available = {
				"Ivysaur",
				"Venusaur",
				"Charmander",
				"Charmeleon",
				"Charizard",
				"Squirtle",
				"Wartortle",
				"Blastoise",
				"Pikachu",
				"Alakazam",
				"Machamp",
				"Magneton",
				"Haunter",
				"Gengar",
				"Onix",
				"Cubone",
				"Rhydon",
				"Scyther",
				"Jynx",
				"Electabuzz",
				"Magmar",
				"Pinsir",
				"Gyarados",
				"Vaporeon",
				"Jolteon",
				"Flareon",
				"Omastar",
				"Kabutops",
				"Snorlax",
				"Dragonite",
				"Mewtwo",
				"Mew",
				"sZapdos",
				"sAerodactyl",
				"sMoltres",
				"sArticuno",
				"sBulbasaur",
				"sExeggutor",
				"sKangaskhan",
			};//available

			int select = (int)(Math.random()*43);
			
			//If the Pokemon has healing moves, make sure to import
			//the right AI
			if (available[select].toCharArray()[0] == 's')
			{
				char[] charArr = available[select].toCharArray();
				int EOL = available[select].length()-1;
				
				available[select] = new String(charArr, 1, EOL);
				intel = new HealingAI();
			} else {
				intel = new CommonAI();
			}
			
			for(PkmnPokemon pkmn : PkmnPokemon.values()) 
			{
				if(available[select].equals(pkmn.name()))
				{
					game.applyAction(new PkmnSelectMoveAction(thisPlayer, pkmn));
					currPokemon = pkmn;
				}
			}	
	}//pokemonSelect

}//PkmnComputerHard
