package game;
    
/**
 * An abstract computerized game player player. This is an abstract class,
 * that should be subclassed to implement different games.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2002 
 */
public abstract class GameComputerPlayer implements GamePlayer, Runnable
{
    /**
     * the game object
     */
    protected Game game;

    // the player's id, from the game's viewpoint
    private int playerId;
    
    // boolean that tells whether a move has been requested by the game.
    // This is used to avoid having the game process extra "make move"
    // requests.
    private boolean moveHasBeenRequested;

    /**
     * Constructor for the GameComputerPlayer class.
     */
    public GameComputerPlayer() {

        // set game to null and playerId to -1, indicating that the player
        // is not yet associated with any game
        game = null;
        this.playerId = -1;

        // we have not yet been requested to make a move
        moveHasBeenRequested = false;
    }

    /**
     * Gets the player's numeric id from the pespective of the game
     * it's playing in.  (E.g., 0 if player 0, 1 if player 1.)
     *
     * @return the player's numeric id, from the game's viewpoint.
     */
    public int getId() {
        return playerId;
    }
    
    /**
     * Lets the player know what the game object is.
     * 
     * @param gm the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
    public void setGame(Game gm, int playerId) {
        this.playerId = playerId;
        if (this.game == null && gm != null) {
            // only set game if not already set

            // reject if playerId is wrong
            if (playerId < 0 || playerId >= gm.maxPlayersAllowed()) {
                return;
            }

            // set the game
            game = gm;

        }
    }

    /**
     * Lets the player know that the game object wants the him to
     * make a move.  This is where different strategies are possible, so
     * we leave this to the subclass.
     */
    public void requestMove() {
        // if a move had not already been requested, start up a thread
        // that will request a move
        if (!moveHasBeenRequested) {

            // register that move has now been requested
            moveHasBeenRequested = true;

            // create and run the thread
            Thread t = new Thread(this);
            t.start();
        }
    }
    
    /**
     * The method that will run in the new thread when a move has
     * been requested.
     */
    public void run() {

        // request the move
        doRequestMove();

        // when finished, mark object as not having a pending move-request
        moveHasBeenRequested = false;
    }

    /**
     * Action to perform when a move is requested.  This is the "key" 
     * method for a computer player: here is where the subclass specifies
     * the algorithm by which the computer player selects a move.
     */ 
    protected abstract void doRequestMove();

    /**
     * Notifies the player know that it's not his move.  Typically
     * called by the game object when the player has made a move out-of-turn.
     */
    public void notYourMove(){
        // ignore
    }

    /**
     * Notifies a player that it is time to quit.  Typically called by the
     * game object when a player has indicated a desire to quit.
     */    public void timeToQuit(){
        // acknowledge to the game
        game.applyAction(new GameQuitAcknowledgeAction(this));
    }

    /**
     * Notifies a player that a request he had made is invalid.  (For
     * example, if the player request a "quit", but he is not allowed to.)
     * Typically called by the game object.
     */
    public void invalidRequest(){
        // ignore
    }

    /**
     * Notifies a player that the move he made is illegal.  (For example,
     * moving a non-king checkers-piece backwards.) Typically called by
     * the game object when an illegal move has been detected.
     */
    public void illegalMove(){
        // ignore
    }

    /**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public void gameIsOver(){
        // acknowledge to the game
        game.applyAction(new GameQuitAcknowledgeAction(this));
    }

    /**
     * Notifies a player that the state of the game has changed.  Typically
     * called by the game object when an opponent has made a move, and the
     * player may want to take the move into account (e.g., to display it
     * on the screen.
     */
    public void stateChanged(){
        // ignore
    }

    /**
     * Notifies the player that all players have acknowledged that the game
     * will be "quit". Gives the player last chance to "clean up".  (E.g.,
     * closing windows, terminating network connections, closing files.)
     * Typically called by the Game object just before the entire program
     * terminates.
     */
    public void finishUp(){
        // ignore
    }

    /**
     * sleep for a certain number of milliseconds, to give our opponent the
     * impression that it's taking us time to think
     *
     * @param millis the number of milliseconds to sleep
     */
    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException x) {
        }
    }

    /**
     * Tells whether the player is ready to play the game.
     *
     * @return a boolean value indicating whether the player is ready
     *   to play.
     */
    public boolean isReady() {
        return true;
    }
}
