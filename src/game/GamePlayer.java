package game;


/**
 * A player who plays a (generic) game. Typically, this interface will
 * be subclassed when a particular game is built.
 * 
 * @author Steven R. Vegdahl
 * @version 22 July 2002
 */

public interface GamePlayer
{
    /**
     * Lets the player know what the game object is.
     * 
     * @param game the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
    public abstract void setGame(Game game, int playerId);

    /**
     * Lets the player know that the game object wants the him to
     * make a move.
     */
    public abstract void requestMove();

    /**
     * Notifies the player know that it's not his move.  Typically
     * called by the game object when the player has made a move out-of-turn.
     */
    public abstract void notYourMove();

    /**
     * Notifies a player that it is time to quit.  Typically called by the
     * game object when a player has indicated a desire to quit.
     */
    public abstract void timeToQuit();

    /**
     * Notifies a player that a request he had made is invalid.  (For
     * example, if the player request a "quit", but he is not allowed to.)
     * Typically called by the game object.
     */
    public abstract void invalidRequest();

    /**
     * Notifies a player that the move he made is illegal.  (For example,
     * moving a non-king checkers-piece backwards.) Typically called by
     * the game object when an illegal move has been detected.
     */
    public abstract void illegalMove();

    /**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public abstract void gameIsOver();

    /**
     * Notifies a player that the state of the game has changed.  Typically
     * called by the game object when an opponent has made a move, and the
     * player may want to take the move into account (e.g., to display it
     * on the screen.
     */
    public abstract void stateChanged();

    /**
     * Notifies the player that all players have acknowledged that the game
     * will be "quit". Gives the player last chance to "clean up".  (E.g.,
     * closing windows, terminating network connections, closing files.)
     * Typically called by the Game object just before the entire program
     * terminates.
     */
    public abstract void finishUp();

    /**
     * Gets the player's numeric id from the pespective of the game
     * it's playing in.  (E.g., 0 if player 0, 1 if player 1.)
     *
     * @return the player's numeric id, from the game's viewpoint.
     */
    public abstract int getId();

    /**
     * Tells whether the player is ready to play the game.
     *
     * @return a boolean value indicating whether the player is ready
     *   to play.
     */
    public abstract boolean isReady();
}
