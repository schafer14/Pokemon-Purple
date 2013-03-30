package game;

/**
 * An interface that creates game-specific players and options.  Used in
 * initializing a game using a DriverEngine.
 * 
 * @author Steven R. Vegdahl
 * @version 25 July 2002
 */

public interface GameDriver
{
    /**
     * Returns a array containing the names of players from which a
     * selection should be made.  Typically there will be a human player
     * (i.e., a GUI that allows a human to play the game), and one or
     * more computer (i.e., automated) players.  This is an abstract
     * method; it must therefore be subclassed based on the players
     * that are available to play the particular game.
     *
     * @return String-array containing the names of (GUI and automated)
     *  players that are available to play the game.
     */ 
    public abstract String[] localPlayerChoices();
    
    /**
     * Creates a player that to play the game.
     *
     * @param name The name of the player.  (This should be one of
     *  the names returned by localPlayerChoices.)
     * @return A new player for the game.
     */ 
    public abstract GamePlayer createLocalPlayer(String name);
    
    /**
     * Creates a "proxy" player that acts as an intermediary
     * between a local game and a player somewhere else on the internet.
     * 
     * @return a "proxy" player that passes information between the
     *  game and a "real" player somewhere else on the net.
     */
    public abstract ProxyPlayer createRemotePlayer();
    
    /**
     * Creates a "proxy" game that acts as an intermediary between a
     * local player and a game that is somewhere else on the net.
     *
     * @param hostName the name of the machine where the game resides.
     *  (e.g., "upibmg.egr.up.edu")
     * @return a "dummy" game that passes information between the player
     *  and a "real" game somewhere else on the net.
     */
    public abstract ProxyGame createRemoteGame(String hostName);
    
    /**
     * Creates a game with a specified number of players.  Once the
     * game and players are created, the game's <TT>setPlayers</TT>
     * method should be used to tell the game who the players are.
     *
     * @param numPlayers The number of players that will be playing
     *  in this game.
     * @return a new game, which does not yet have any players.
     */
    public abstract Game createGame(int numPlayers);
}
    