package game;

/**
 * A Game, which enforces the rules of a game and interacts
 * with one or more players.  Typically there will be one game, and multiple
 * players.
 * 
 * @author Steven R. Vegdahl
 * @version 8 Nov 2001
 */

public interface Game
{
    /**
     * Gets information about the state of the game.  Only gives information
     * if the given player is allowed to know that information.  (Typically,
     * the GameState object will be a subclass of GameState that applies to
     * that particular game.) The caller can ask for different kinds of state
     * (e.g., "whose move is it?", "is there a piece on square 4") by passing
     * different values in the "stateType" parameter.  The interpretation of
     * is dependent on the game, with the exception that if -1 is passed, an
     * <TT>IntGameState</TT> object should be returned that contains the player's
     * ID (e.g., player #2) from the game's viewpoint.
     *
     * @param p the player asking the question
     * @param stateType a game-dependent encoding, specifying the particular
     *  type of information the player wants. If this paremeter is -1, it
     *  denotes a request for the player's ID. Otherwise, the interpretation
     *  is game-dependent.
     * @return an object that contains the information that the caller
     *  requested
     */
    public abstract GameState getState(GamePlayer p, int stateType);

    /**
     * applies an action to the Game object.  Typically called by a player
     * who wants to request an action.
     *
     * @param the player requesting the action
     */
    public abstract void applyAction(GameAction action);

    /**
     * tells the game which players are playing.  Checks that the array
     * consists of a set of players that can legally play the game (e.g.,
     * not too many or too few.
     *
     * @param the array of players playing the game
     * @return null if game is set up properly; an error message is there
     *   was a problem
     */
    public String setPlayers(GamePlayer[] players);


    /**
     * Begins the actual game.
     */
    public void playGame();

    /**
     * tells whether the group of player may contain "empty" positions
     * such as in a poker game when there are empty chairs
     *
     * @return tells whether game may have null players
     */
    public abstract boolean nullPlayersAllowed();

    /**
     * the minimum number of players a game can have
     *
     * @return the fewest number of players allowed
     */
    public abstract int minPlayersAllowed();

    /**
     * the maximum number of players a game can have
     *
     * @return the highest number of players allowed
     */
    public abstract int maxPlayersAllowed();


}
