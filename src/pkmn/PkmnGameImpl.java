package pkmn;

import java.util.Vector;

import game.*;

/**
 * This class manages a multi-player game.  
 * The class is responsible for interacting with the players,
 * and for enforcing the rules of the game.
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnGameImpl extends GameImpl implements PkmnGame 
{
	//How many players are playing the game.
	private int numPlayers;

	// tells which player (0 or 1) is the game-winner; -1 indicates that
	// the game has no winner
	private int gameWinner;

	//double array of Pokemon, 
	//first index differentiates between Players, second contains each players Pokemon
	private Vector<Vector<PkmnPokemon>> pokemonTeam;
	//the currently selected pokemon of each player
	private Vector<PkmnPokemon> currPokemon;
	// the current Health the players pokemon
	private Vector<Vector<Integer>> currHP;
	// the current PP of each move
	private Vector<Vector<Integer[]>> currMovePP;
	//whether each player has selected a move for this turn
	private boolean hasMoved[]; 
	//Whether or not an attack has missed
	private boolean missedAttack[];
	//the moves the players have selected for this turn
	private PkmnMove selectedMove[]; 
	//whether the game is in the pokemon selection state, or alternatively the normal game state. 
	private boolean isPkmnSelect;
	//the number of Pokemon each player is allowed to have
	private int PkmnPerTeam;


	//Table representing how effective different types are against each other.
	public static double[][] typeEffectiveness = {
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, .5, 0, 1, 1, .5}, //Normal
		{1, .5, .5, 1, 2, 2, 1, 1, 1, 1, 1, 2, .5, 1, .5, 1, 2}, //Fire
		{1, 2, .5, 1, .5, 1, 1, 1, 2, 1, 1, 1, 2, 1, .5, 1, 1}, //Water
		{1, 1, 2, .5, .5, 1, 1, 1, 0, 2, 1, 1, 1, 1, .5, 1, 1}, //Electric
		{1, .5, 2, 1, .5, 1, 1, .5, 2, .5, 1, .5, 2, 1, .5, 1, .5}, //Grass
		{1, .5, .5, 1, 2, .5, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, .5}, //Ice
		{2, 1, 1, 1, 1, 2, 1, .5, 1, .5, .5, .5, 2, 0, 1, 2, 2}, //Fighting
		{1, 1, 1, 1, 2, 1, 1, .5, .5, 1, 1, 1, .5, .5, 1, 1, 0}, //Poison
		{1, 2, 1, 2, .5, 1, 1, 2, 1, 0, 1, .5, 2, 1, 1, 1, 2}, //Ground
		{1, 1, 1, .5, 2, 1, 2, 1, 1, 1, 1, 2, .5, 1, 1, 1, .5}, //Flying
		{1, 1, 1, 1, 1, 1, 2, 2, 1, 1, .5, 1, 1, 1, 1, 0, .5}, //Psychic
		{1, .5, 1, 1, 2, 1, .5, .5, 1, .5, 2, 1, 1, .5, 1, 2, .5}, //Bug
		{1, 2, 1, 1, 1, 2, .5, 1, .5, 2, 1, 2, 1, 1, 1, 1, .5}, //Rock
		{0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1, .5, .5}, //Ghost
		{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, .5}, //Dragon
		{1, 1, 1, 1, 1, 1, .5, 1, 1, 1, 2, 1, 1, 2, 1, .5, .5}, //Dark
		{1, .5, .5, .5, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, .5} //Steel
	};

	/**
	 * Constructor for PkmnGameImpl
	 * 
	 * @param numPlayers the number of players in this game
	 */
	public PkmnGameImpl(int numPlayers)
	{
		super();

		this.numPlayers = numPlayers;

		//Initialize has moved
		hasMoved = new boolean[numPlayers];
		//Initialize missed attack
		missedAttack = new boolean[numPlayers];

		//Initialize current Health
		currHP = new Vector<Vector<Integer>>();
		for (int i=0; i <numPlayers;i++){
			currHP.add(new Vector<Integer>());
		}

		//Initialize move PPs
		currMovePP = new Vector<Vector<Integer[]>>();
		for(int i = 0; i < numPlayers; i++)
		{
			currMovePP.add(new Vector<Integer[]>());
		}

		//Initialize pokemon team
		pokemonTeam = new Vector<Vector<PkmnPokemon>>();
		for (int i=0; i <numPlayers;i++){
			pokemonTeam.add(new Vector<PkmnPokemon>());
		}

		//Initialize currPokemon (values given once all pokemon have been selected)
		currPokemon = new Vector<PkmnPokemon>();

		PkmnPerTeam = 1;
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
	 * is called just before game commences, initializes the game
	 *
	 */
	protected void initializeGame() 
	{
		//game winner is -1 as long as no one has won
		gameWinner = -1; 

		for(int i =0; i<hasMoved.length;i++) hasMoved[i]=false;

		selectedMove = new PkmnMove[numPlayers];

		isPkmnSelect = true;


		// tell both players that the game's state has changed
		notifyAllStateChanged();
	}

	/**
	 * Tells whether the game is over.  Each specific game should be able
	 * to tell whether the game is over.  The algorithm for determining
	 * such should be put here.
	 *
	 * @return boolean value that tells whether the game is over
	 */
	public boolean gameOver() 
	{
		if(gameWinner >= 0){
			// make sure all have up to date state
			notifyAllStateChanged();
		}
		return gameWinner >= 0;
	}

	/**
	 * Tells whether a given player is allowed to make a move at the
	 * present time.
	 *
	 * @param gp the player we're interested in
	 * @return true if the player may make a move at the present time.
	 */
	protected boolean canMove(GamePlayer gp) 
	{
		// get the 0/1 id of our player
		int playerId = indexOf(gp);

		return !hasMoved[playerId];
	}

	/**
	 * Tells whether a given player is allowed to quit the game at the
	 * present time.
	 *
	 * @param gp the player we're interested in
	 * @return true if the player may quit the game
	 */
	protected boolean canQuit(GamePlayer gp) 
	{
		return true;
	}

	/**
	 * Gets information about the state of the game.  Only gives information
	 * if the given player is allowed to know that information.  Subclasses
	 * should override this method to specialize its behavior based on the
	 * particular game that is being played.  (The handling of the -1
	 * stateType is done in the public getState method.)
	 *
	 * @param p the player asking the question
	 * @param stateType a game-dependent encoding, specifying the particular
	 *  type of information the player wants
	 * @return an object that contains the information that the caller
	 *  requested
	 */
	protected GameState getGameState(GamePlayer p, int stateType) 
	{
		return new PkmnState(pokemonTeam,currPokemon,currHP,currMovePP, 
				hasMoved, missedAttack, isPkmnSelect, gameWinner);
	}

	/**
	 * Handles a timer event.  By default, the method does nothing.  However,
	 * for games that require a timer, this method provides a place to put
	 * the behavior to perform whenever a time event occurs.
	 * 
	 */
	protected void handleTimerEvent() {
	}

	/**
	 * attempts to make a move on behalf of the player
	 *
	 * @return true if the move was successfully make; false it it's an
	 *  illegal move.
	 */
	protected boolean makeMove(GamePlayer thePlayer, GameMoveAction move) 
	{
		boolean moveSuccess = false;

		if (move != null){
			if(move instanceof PkmnSelectMoveAction){
				moveSuccess = makeSelectMove(thePlayer,move);
			}
			else if (move instanceof PkmnAttackMoveAction){
				moveSuccess = makeAttackMove(thePlayer,move);
			}
		}
		return moveSuccess;
	}

	/**
	 * attempts to make a pokemon selection on behalf of the player
	 *
	 * @return true if the move was successfully make; false it it's an
	 *  illegal move.
	 */
	protected boolean makeSelectMove(GamePlayer thePlayer, GameMoveAction move) 
	{
		// get the 0/1 id of our player
		int playerId = indexOf(thePlayer);

		//checks if player has moved, and selects move if they have not
		if(hasMoved[playerId]) return false;
		else{
			Vector<PkmnPokemon> thisTeam = pokemonTeam.get(playerId);
			thisTeam.add(((PkmnSelectMoveAction) move).getPokemon());
			//checks to see if player has selected all of the pokemon they are aloud to
			if (thisTeam.size() >=PkmnPerTeam){
				hasMoved[playerId]=true;
			}
			else{
				hasMoved[playerId]=false;
			}

			//checks to see if all the players have moved assume is true until
			//proven otherwise
			boolean allMoved = true;
			for(boolean thisHasMoved: hasMoved){
				if (!thisHasMoved){
					allMoved=false;
				}
			}

			//what to do if everyone has selected their pokemon
			if (allMoved){
				//set their first pokemon as the first one they selected, and initialize
				//the current HealthValues and move PPs
				for(int i=0; i<numPlayers;i++){
					currPokemon.add(pokemonTeam.get(i).get(0));
					for(int j=0; j<pokemonTeam.get(i).size();j++){
						currHP.get(i).add(new Integer(pokemonTeam.get(i).get(j).hp));

						Integer[] tempPPArray = new Integer[4]; 
						for(int k = 0; k < 4; k++)
						{
							tempPPArray[k] = pokemonTeam.get(i).get(j).moveset[k].getPP();
						}
						currMovePP.get(i).add(tempPPArray);
					}
				}

				//reset hasMoved
				for(int i=0;i<hasMoved.length;i++) hasMoved[i]=false;

				//switch to fight phase
				isPkmnSelect=false;

				// tell both players that the game's state has changed
				notifyAllStateChanged();
			}
		}
		//if the end is reached return true
		return true;
	}

	/**
	 * attempts to make an attack move on behalf of the player
	 *
	 * @return true if the move was successfully make; false it it's an
	 *  illegal move.
	 */
	protected boolean makeAttackMove(GamePlayer thePlayer, GameMoveAction move) 
	{
		// get the 0/1 id of our player
		int playerId = indexOf(thePlayer);
		// index of players pokemon in their team array
		int indexOfPkmn = pokemonTeam.get(playerId).indexOf(currPokemon.get(playerId));

		//index of chosen move in pokemon's move array, finds the moves in the Pokemon's moveset
		int moveIndex=0;
		for (int i = 0; i < currPokemon.get(playerId).moveset.length; i++)
		{
			if (currPokemon.get(playerId).moveset[i].equals(((PkmnAttackMoveAction)move).getMove()))
				moveIndex = i;
		}

		//checks if player has moved, and whether they have enough pp
		if(hasMoved[playerId] || (currMovePP.get(playerId).get(indexOfPkmn)[moveIndex] <= 0))
			return false;
		//add selected moveh
		else{
			selectedMove[playerId] = ((PkmnAttackMoveAction) move).getMove(); 
			hasMoved[playerId]= true; 

			// tell all players that the game's state has changed
			notifyAllStateChanged();
		}

		//checks to see if all players have selected their moves 
		boolean allHaveMoved= true; 
		for(boolean thisPlayerHasMoved: hasMoved){
			//if either this player hasn't moved or a previous one hasn't
			//than 
			if (!(allHaveMoved && thisPlayerHasMoved)){
				allHaveMoved= false; 
			}
		}
		//execute battle if all have moved
		if (allHaveMoved){
			boolean battleExe = executeBattle();

			//reset hasMoved
			for(int i=0;i<hasMoved.length;i++) hasMoved[i]=false;

			if (!battleExe)return false;
		}
		return true;
	}

	/**
	 * checks whether there is a winner; if so, sets the 'gameWinner' instance
	 * variable to be the winning player's id
	 *
	 * @return true if check for winner was successful, or false if the check failed
	 */
	private boolean checkWinner() {

		//array to represent each player and whether all there pokemon are dead
		boolean[] allDead = new boolean[pokemonTeam.size()];

		//assume they are all dead unless proven otherwise
		for(int i=0;i<allDead.length;i++) allDead[i]=true;

		//checks each pokemon of each player
		for (int indexThisPlayer=0;indexThisPlayer<pokemonTeam.size();indexThisPlayer++){
			Vector<PkmnPokemon> thisPlayer= pokemonTeam.get(indexThisPlayer);
			//check each pokemon
			for(int indexThisPkmn=0;indexThisPkmn<thisPlayer.size();indexThisPkmn++){
				int thisPkmnCurrHP = currHP.get(indexThisPlayer)
				.get(indexThisPkmn);

				//if a pokemons HP is above 0 then they are not all dead
				if (thisPkmnCurrHP > 0){
					allDead[indexThisPlayer]=false;
				}
			}
		}

		// determines if there  is a winner 
		if (!allDead[0] && allDead[1]){
			gameWinner=0;
		}
		else if (allDead[0] && !allDead[1]){
			gameWinner=1;
		}
		else if (!allDead[0] && !allDead[1]){
			return false;
		}
		return true;
	}

	/**
	 * attempts to execute a battle based on the current move choices
	 *
	 * @return true if the battle executed successfully and false if the,
	 * battle cannot be executed
	 */
	private boolean executeBattle(){
		boolean battleSuccess =true;
		//the ID of the player to go first and to go second
		int firstPl=0;
		int secondPl=1;
		if (currPokemon.get(0).spe<currPokemon.get(1).spe){
			firstPl=1;
			secondPl=0;
		}

		//Pause to let the human players see what happens better.
		try{
			Thread.sleep(500);
		}
		catch(InterruptedException ie) {}

		battleSuccess = attack(firstPl, secondPl, battleSuccess);
		// checks for winner if returns false than neither player has living pokemon
		boolean successWinCheck = checkWinner();

		//quits attack action if someone has already won
		if(gameWinner != -1){
			// tell both players that the game's state has changed
			notifyAllStateChanged();
			return battleSuccess;
		}

		//Pause to let the human players see what happens better.
		try{
			Thread.sleep(500);
		}
		catch(InterruptedException ie) {}

		battleSuccess = attack(secondPl, firstPl, battleSuccess);
		// checks for winner if returns false than neither player has living pokemon
		successWinCheck = checkWinner();

		// tell both players that the game's state has changed
		notifyAllStateChanged();

		return battleSuccess;
	}

	/**
	 * Performs an attack and adjusts stats accordingly.
	 * 
	 * @param player
	 * @param opponent
	 * @param battleSuccess
	 * @return battleSuccess
	 */
	public boolean attack(int player, int opponent, boolean battleSuccess)
	{
		//index of the given move in the move set array
		int moveIndex = 0;

		//Finds the moves in the Pokemon's moveset
		for (int i = 0; i < currPokemon.get(player).moveset.length; i++)
		{
			if (currPokemon.get(player).moveset[i].equals(selectedMove[player]))
				moveIndex = i;
		}

		//index of Attackers currPokemon out of the pokemon Team
		int indexOfAttPkmn=0;
		// finds the defenders pokemon out of the pokemon Team and updates HP and PP
		PkmnPokemon AttPokemon = currPokemon.get(player);
		for (PkmnPokemon thisPkmn : pokemonTeam.get(player)) {
			if (thisPkmn.toString().equals(AttPokemon.toString())) {
				indexOfAttPkmn = pokemonTeam.get(player).indexOf(thisPkmn);
			}
		}

		//index of defenders currPokemon
		int indexOfDefPkmn=0;
		// finds the defenders pokemon out of the pokemon Team
		PkmnPokemon DefPokemon = currPokemon.get(opponent);
		for (PkmnPokemon thisPkmn : pokemonTeam.get(opponent)) {
			if (thisPkmn.toString().equals(DefPokemon.toString())) {
				indexOfDefPkmn = pokemonTeam.get(opponent).indexOf(thisPkmn);
			}
		}

		// make sure no one has won 
		if (gameWinner == -1) {

			//Do slightly different things depending on the op code of the move.
			switch(selectedMove[player].getOpCode())
			{
			case 0:
				standardAttack(player, opponent,moveIndex,indexOfAttPkmn,indexOfDefPkmn);
				break;
			case 1:
				healMove1(player,moveIndex,indexOfAttPkmn,indexOfDefPkmn);
				break;
			case 2:
				healMove2(player,moveIndex,indexOfAttPkmn,indexOfDefPkmn);
				break;
			case 3:
				healMove3(player, opponent,moveIndex,indexOfAttPkmn,indexOfDefPkmn);
				break;
				//The critical hit is taken care of 
				// in the damage calculation				
			case 4:
				standardAttack(player, opponent,moveIndex,indexOfAttPkmn,indexOfDefPkmn);
				break;
			}
		}

		return battleSuccess;
	}

	/**
	 * Perform a standard attack with damage dealt
	 * and nothing else special happens.
	 * 
	 * @param player
	 * @param opponent
	 * @param moveIndex
	 * @param indexOfAttPkmn
	 * @param indexOfDefPkmn
	 */
	public void standardAttack(int player, int opponent,int moveIndex,int indexOfAttPkmn,int indexOfDefPkmn)
	{
		// Check to see if the attack hits first.
		if (Math.random() * 100 + .01 < selectedMove[player].getAccuracy()) {
			int attckDamage = calcDamage(player, opponent,
					selectedMove[player]);

			// current HP of given pokemon
			int prevHP = currHP.get(opponent).get(indexOfDefPkmn)
			.intValue();
			// update health
			if (prevHP - attckDamage < 0)
				currHP.get(opponent).setElementAt(0, indexOfDefPkmn);
			else
				currHP.get(opponent).setElementAt(new Integer(prevHP - attckDamage), indexOfDefPkmn);
			// update PP
			currMovePP.get(player).get(indexOfAttPkmn)[moveIndex] -= 1;
			//The attack hit
			missedAttack[player] = false;
		}
		//If the attack misses
		else missedAttack[player] = true;
	}

	/**
	 * Performs an attack where the player's
	 * Pokemon is partially healed.
	 * 
	 * @param player
	 * @param moveIndex
	 * @param indexOfAttPkmn
	 * @param indexOfDefPkmn
	 */
	public void healMove1(int player,int moveIndex,int indexOfAttPkmn,int indexOfDefPkmn)
	{
		PkmnPokemon playerPkmn = currPokemon.get(player);
		int halfMaxHP = playerPkmn.hp/2;

		// current HP of given pokemon
		int prevHP = currHP.get(player).get(indexOfAttPkmn).intValue();
		// update health
		if (prevHP + halfMaxHP > playerPkmn.hp)
			currHP.get(player).setElementAt(playerPkmn.hp, indexOfAttPkmn);
		else
			currHP.get(player).setElementAt(new Integer(prevHP + halfMaxHP), indexOfAttPkmn);
		// update PP
		currMovePP.get(player).get(indexOfAttPkmn)[moveIndex] -= 1;

		//attack always hits
		missedAttack[player] = false;
	}

	/**
	 * Performs an attack where the player's
	 * Flying-type Pokemon is partially healed.
	 * 
	 * @param player
	 * @param moveIndex
	 * @param indexOfAttPkmn
	 * @param indexOfDefPkmn
	 */
	public void healMove2(int player,int moveIndex,int indexOfAttPkmn,int indexOfDefPkmn)
	{
		PkmnPokemon playerPkmn = currPokemon.get(player);

		//If the player's Pokemon is a Flying-type.
		if (playerPkmn.getType1().equals(PkmnType.FLYING) ||
				playerPkmn.getType2().equals(PkmnType.FLYING))
		{
			int halfMaxHP = playerPkmn.hp/2;

			// current HP of given pokemon
			int prevHP = currHP.get(player).get(indexOfAttPkmn).intValue();
			// update health
			if (prevHP + halfMaxHP > playerPkmn.hp)
				currHP.get(player).setElementAt(playerPkmn.hp, indexOfAttPkmn);
			else
				currHP.get(player).setElementAt(new Integer(prevHP + halfMaxHP), indexOfAttPkmn);
			// update PP
			currMovePP.get(player).get(indexOfAttPkmn)[moveIndex] -= 1;
		}

		//attack always hits
		missedAttack[player] = false;
	}

	/**
	 * Perform an attack with damage dealt
	 * and the player is healed by half
	 * damage dealt.
	 * 
	 * @param player
	 * @param opponent
	 * @param moveIndex
	 * @param indexOfAttPkmn
	 * @param indexOfDefPkmn
	 */
	public void healMove3(int player, int opponent,int moveIndex,int indexOfAttPkmn,int indexOfDefPkmn)
	{
		//The player's Pokemon
		PkmnPokemon playerPkmn = currPokemon.get(player);

		// Check to see if the attack hits first.
		if (Math.random() * 100 + .01 < selectedMove[player].getAccuracy()) {
			int attackDamage = calcDamage(player, opponent,
					selectedMove[player]);

			// current HP of given pokemon
			int prevHP = currHP.get(opponent).get(indexOfDefPkmn)
			.intValue();
			// update opponent health
			if (prevHP - attackDamage < 0)
				currHP.get(opponent).setElementAt(0, indexOfDefPkmn);
			else
				currHP.get(opponent).setElementAt(new Integer(prevHP - attackDamage), indexOfDefPkmn);
			//update player health
			if (prevHP + attackDamage/2 > playerPkmn.hp)
				currHP.get(player).setElementAt(playerPkmn.hp, indexOfAttPkmn);
			else
				currHP.get(player).setElementAt(new Integer(prevHP + attackDamage/2), indexOfAttPkmn);
			// update PP
			currMovePP.get(player).get(indexOfAttPkmn)[moveIndex] -= 1;
			//The attack hit
			missedAttack[player] = false;

		}
		//If the attack misses
		else
			missedAttack[player] = true;
	}

	/**
	 * Calculates the damage that a PkmnMove does in battle.
	 * 
	 * @param player
	 * @param opponent
	 * @param move
	 * @return totalDamage
	 */
	public int calcDamage(int player, int opponent, PkmnMove move)
	{
		//For this program's purposes this is always 100.
		double level = 100;

		//First two terms used in the damage
		// calculation equation.
		double term1 = (2*level+10)/250;
		double term2 = 1;

		//Alter term two based on if the move is
		// a Physical or Special move.
		if (move.getBase() == 0)
		{
			term2 = currPokemon.elementAt(player).atk / currPokemon.elementAt(opponent).def;
		}
		else if (move.getBase() == 1)
		{
			double specialA = currPokemon.elementAt(player).spa;
			double specialD = currPokemon.elementAt(opponent).spd;
			term2 = specialA / specialD;
		}

		//Multiply the terms together with the move's power
		double totalDamage = (term1*term2*move.getPower() + 2);

		//Multiply the total by other modifications
		// the level of damage can go through.
		totalDamage *= calcMod(player, opponent, move);

		return (int)totalDamage;

	}

	/**
	 * Calculate the numbers associated
	 * with the different modifications
	 * a move's damage can have.
	 * 
	 * @param player
	 * @param opponent
	 * @param move
	 * @return mod
	 */
	public double calcMod(int player, int opponent, PkmnMove move)
	{
		double mod;
		double STAB;
		double typeEffect;
		double typeEffect2;
		double random = 0;
		PkmnPokemon playerPkmn= currPokemon.elementAt(player);
		PkmnPokemon opponentPkmn= currPokemon.elementAt(opponent);

		//If the move type and Pokemon type match,
		// damage gets a bonus called STAB
		// (Same Type Attack Bonus).

		if (move.getType().equals(playerPkmn.getType1()) ||
				move.getType().equals(playerPkmn.getType2()))
			STAB = 1.5;
		else
			STAB = 1.0;

		typeEffect = typeEffectiveness[move.getType().getNum()]
		                               [opponentPkmn.getType1().getNum()];

		//Random number with a range of [.85, 1.00]
		while(random < .85)
		{
			random = Math.random() + 0.01;
		}

		//If the opponent's Pokemon has a secondary type,
		// include it in the calculation.
		if (opponentPkmn.getType2().getNum() > 16)
			mod = STAB * typeEffect * checkIfCrit(move) * random;
		else
		{
			typeEffect2 = typeEffectiveness[move.getType().getNum()]
			                                [opponentPkmn.getType2().getNum()];
			mod = STAB * typeEffect * typeEffect2 * checkIfCrit(move) * random;
		}

		return mod;
	}

	/**
	 * Check to see if the move is a critical hit.
	 * Based on a 6.25% chance.
	 * Some moves have a higher chance of 12.5%.
	 * 
	 * @return 1 or 2
	 */
	public int checkIfCrit(PkmnMove move)
	{
		int crit = (int) (Math.random()*1000);

		//If the move has a higher critical-hit chance
		if (move.getOpCode() == 4)
		{
			//The attack has a 12.5% chance of
			// doing more damage.
			if (crit < 125)
				return 2;
			else
				return 1;
		}
		//otherwise
		else
		{
			//The attack has a 6.25% chance of
			// doing more damage.
			if (crit < 63)
				return 2;
			else
				return 1;
		}
	}
}
