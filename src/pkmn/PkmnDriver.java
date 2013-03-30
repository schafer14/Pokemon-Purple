package pkmn;

import game.*;

/**
 * An interface that creates game-specific players and options.  Used in
 * initializing a game using a DriverEngine.
 * 
 * Many comments borrowed from classes designed
 * by Steve R. Vegdahl
 * 
 * @author Pokemon Purple Team
 * @version 3 December 2011
 */
public class PkmnDriver implements GameDriver
{
    // Array containing the "names" of the Pokemon players we know about
    private static final String[] lpNames = 
    {
        "human player",
        "easy computer player",
        "hard computer player",
    };
    
    /**
     * Creates and plays a Pokemon game.
     */
    public static void main(String[] args) 
    {
        DriverEngine.play(args, new PkmnDriver());    
    }
    
    /**
     * Returns a array containing the names of players from which a
     * selection should be made.  Typically there will be a human player
     * (i.e., a GUI that allows a human to play the game), and one or
     * more computer (i.e., automated) players.
     *
     * @return String-array containing the names of (GUI and automated)
     *  players that are available to play the game.
     */ 
	public String[] localPlayerChoices() 
	{
		return lpNames;
	}

    /**
     * Creates a player that to play the game.
     *
     * @param name The name of the player.  (This should be one of
     *  the names returned by localPlayerChoices.)
     * @return A new player for the game.
     */ 
	public GamePlayer createLocalPlayer(String name) 
	{
		if (name.equals(lpNames[0])) 
		{ // user selected "human player"
            // create/return a human/GUI player
            return new PkmnHumanPlayer();
        }
        else if (name.equals(lpNames[1])) 
        { // user selected "easy computer player"
            // create/return an easy computer player
            return new PkmnComputerEasy();
        }
        else if (name.equals(lpNames[2]))
        {// user selected "hard computer player"
        	//create/return a hard computer player
        	return new PkmnComputerHard();
        }
        else 
        {
            // bad selection: return null
            return null;
        }
	}
	
    /**
     * Creates a "proxy" player that acts as an intermediary
     * between a local game and a player somewhere else on the internet.
     * 
     * @return a "proxy" player that passes information between the
     *  game and a "real" player somewhere else on the net.
     */
	public ProxyPlayer createRemotePlayer() 
	{
		return new PkmnProxyPlayer();
	}
	
    /**
     * Creates a "proxy" game that acts as an intermediary between a
     * local player and a game that is somewhere else on the net.
     *
     * @param hostName the name of the machine where the game resides.
     *  (e.g., "upibmg.egr.up.edu")
     * @return a "dummy" game that passes information between the player
     *  and a "real" game somewhere else on the net.
     */
	public ProxyGame createRemoteGame(String hostName) 
	{
		return new PkmnProxyGame(hostName);
	}

    /**
     * Creates a game with a specified number of players.  Once the
     * game and players are created, the game's <TT>setPlayers</TT>
     * method should be used to tell the game who the players are.
     *
     * @param numPlayers The number of players that will be playing
     *  in this game.
     * @return a new game, which does not yet have any players.
     */
	public Game createGame(int numPlayers) 
	{
		return new PkmnGameImpl(numPlayers);
	}
}
