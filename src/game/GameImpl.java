package game;

import java.util.*;

/**
 * A generic multi-player game, which may be subclassed to implement a
 * real game.  The game is responsible for interacting with the players,
 * and for enforcing the rules of the game.
 * 
 * @author Steven R. Vegdahl 
 * @version 18 July 2002 
 */
public abstract class GameImpl implements Game
{
    ////////////////////////////////////////////
    // *********instance variables***********
    ////////////////////////////////////////////

    // tells whether we should print "debug" messages to tell what entries
    // we see in the queue
    private static boolean MONITORQUEUE = false;

    /**
     * The array of players that are playing the game.
     */
    protected GamePlayer[] player;

    /**
     * Tells whether the game has started.
     */
    protected boolean gameStarted;

    // Vector into which actions from players are placed
    private Vector actionVec;

    /**
     * The number of players that are presently playing the game.
     */
    protected int playerCount;
    
    ////////////////////////////////////////////
    // *********abstract methods***********
    ////////////////////////////////////////////

    /**
     * Initializes the game.  This method is called just before the game
     * is started.  Subclasses should put such inititialiation behavior here.
     *
     */
    protected abstract void initializeGame();

    /**
     * Tells whether the game is over.  Each specific game should be able
     * to tell whether the game is over.  The algorithm for determining
     * such should be put here.
     *
     * @return boolean value that tells whether the game is over
     */
    public abstract boolean gameOver();

    /**
     * Tells whether a given player is allowed to make a move at the
     * present time.
     *
     * @param gp the player we're interested in
     * @return true iff the player may make a move at the present time.
     */
    protected abstract boolean canMove(GamePlayer gp);

    /**
     * Tells whether a given player is allowed to quit the game at the
     * present time.
     *
     *
     * @param gp the player we're interested in
     * @return true iff the player may quit the game
     */
    protected abstract boolean canQuit(GamePlayer gp);

    /**
     * Gets information about the state of the game.  Only gives information
     * if the given player is allowed to know that information.  (Typically,
     * the GameState object will be a subclass of GameState that applies to
     * that particular game.)  If stateType is -1, it gets the player's numeric
     * id.  Subclasses should override the getGameState method to specialize
     * state-getting behavior.
     *
     * @param p the player asking the question
     * @param stateType a game-dependent encoding, specifying the particular
     *  type of information the player wants
     * @return an object that contains the information that the caller
     *  requested
     */
    public final GameState getState(GamePlayer p, int stateType) {
        if (stateType == -1) {
            return new IntGameState(whoAmI(p));
        }
        else {
            return getGameState(p, stateType);
        }
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
    protected abstract GameState getGameState(GamePlayer p, int stateType);

    /**
     * Requests that a move be made on behalf of a player.  Typically, the
     * player that wants to make the move creates a MoveAction object that
     * encodes the move he wants to make, and then calls this method, passing
     * itself as the player object.  As long as the player does not allow
     * any other player to know who it is (which is typical), no other player
     * can make a move on this player's behalf because it does not have a
     * reference to that player, and therefore cannot pass it as a parameter.
     * This method should check that the given player is allowed to make
     * the specified move before actually performing the move.
     *
     * @param thePlayer The player wishing to make the move.
     * @param move The move object that encodes the requested move
     * @return true if the move was successfully made; false it it's an
     *  illegal move.
     */
    protected abstract boolean makeMove(GamePlayer thePlayer,
        GameMoveAction move);


    ////////////////////////////////////////////
    // *********concrete methods***********
    ////////////////////////////////////////////

    /**
     * Tells the game which players are playing.  (Typically called by the
     * GameDriver object after the players and game have been created.)
     * Checks that the array consists of a set of players that can legally
     * play the game (e.g., not too many or too few).
     *
     * @param the array of players playing the game
     * @return null if game set up properly; an error message if there
     *   was a problem
     */
    public String setPlayers(GamePlayer[] players) {

        // check that array is not null
        if (players == null) return "no players selected";

        // count the players; keep track of whether any are null
        boolean haveNullPlayers = false;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) haveNullPlayers = true;
            else playerCount++;
        }

        // if have (disallowed) null players, or too many/few, return
        if (haveNullPlayers && !nullPlayersAllowed()) {
            return "all player-slots must be filled";
        }
        if (playerCount < minPlayersAllowed()) {
            return "not enough players for the game";
        }
        if (playerCount > maxPlayersAllowed()) {
            return "too many players for the game";
        }

        // make a copy of the array to store as part of our state
        player = (GamePlayer[])players.clone();
        
        // return "success"
        return null;
    }

    /**
     * Constructor for the GameImpl class.  After creation, the game is not
     * ready to be played until the setPlayers method has been called to tell
     * the game who its players are.
     */
    public GameImpl() {

        // do superclass initialization
        super();

        // mark game as not having started
        gameStarted = false;

        // initialize vector for getting messages from players
        actionVec = new Vector();

        // set player-count to 0, as we don't have any players yet
        playerCount = 0;
        
        // set the player-array to null, for now (this will eventually get
        // filled in
        player = null;
    }

    /**
     * Plays a generic game.  (An actual game will be played by a subclass
     * object.)  Initializes the game; waits for all players to tell it
     * that they are ready; continually processes requests from the players
     * until the game is over.
     */
    public void playGame() {

        // if game is already started, return immediately so we don't
        // reinitialize, etc.
        if (gameStarted) return;

        // initialize the game
        initializeGame();

        // wait for all players to be ready
        waitForAllReady();

        // keep asking players for moves and such until the game is over
        while (!gameOver()) {

            // get an action-request from a player; find out which player requested
            // it.
            GameAction action = this.getAction();
            GamePlayer source = action.getSource();

            // if the action is a timer "move", handle the timer event
            if (action.isTimer()) {
                handleTimerEvent();
            }
            // if the action is a "game move" attempt to make that move
            else if (action.isMove()) {
                if (MONITORQUEUE) {
                    System.out.println("de-queue: GAME MOVE");
                }
                GameMoveAction move = (GameMoveAction)action;
                if (canMove(source)) { // player allowed to make move: try
                    if (!makeMove(source, move)) {
                        // move is illegal: notify player
                        source.illegalMove();
                    }
                }
                else {  // player not allowed to make move: notify him
                    source.notYourMove();
                }
            }
            // if action is a request to quit, try to allow him to quit game
            else if (action.isQuitRequest()) {
                if (MONITORQUEUE) {
                    System.out.println("de-queue: QUIT REQUEST");
                }
                if (canQuit(source)) { // player is allowed to quit
                    // notify all players that game is being terminated
                    performActionOnAll(new TimeToQuitInvoker());
                    
                    // wait for all players to acknowledge the "quit"
                    waitForQuitAck();
                }
                else { // player not allowed to quit
                    source.invalidRequest();
                }
            }
            // if no players are requesting service, request a move from each,
            // in case they have forgotten; then wait for a move to occur
            else if (action.isNullAction()) {
                requestMoves();
                wait(100);
            }
            // if it's some other kind of action, it's invalid: notify user
            else {
                source.invalidRequest();
            }
        }

        // game is over: notify each player; then wait for each player to
        // acknowledge
        notifyAllStateChanged();
        performActionOnAll(new GameIsOverInvoker());
        
        waitForQuitAck();
    }

    /**
     * Requests a move from each player.
     * 
     */
    private void requestMoves() {
        // iterate through the player array and invoke 'requestMove' on
        // each non-null player.
        performActionOnAll(new RequestMoveInvoker());
    }
    /**
     * Waits for each player to acknowledge that it's time to quit
     *
     */
    private void waitForQuitAck() {

        // Declare variable to keep track of which players have acknowledged
        // the "quit"; initialially, all are false (except those corresponding
        // to null players
        boolean[] acknowledged = new boolean[player.length];
        for (int i = 0; i < player.length; i++) {
            acknowledged[i] = player[i] == null;
        }

        // Declare variable to keep track of the number of players left to
        //acknowledge
        int playersLeft = playerCount;

        // Keep getting actions from the action-queue; if we get an
        // acknowledge action, mark it down; otherwise notify user that
        // request is invalid
        for (;;) {

            // get the action and the player doing it
            GameAction action = this.getAction();
            GamePlayer source = action.getSource();

            // if the action acknowledges a "quit" mark that player off;
            // if it's the last player, quit the game
            if (action.isQuitAcknowledge()) {
                int idx = indexOf(source);
                if (idx >= 0 && !acknowledged[idx]) {
                    acknowledged[idx] = true; // mark that player
                    playersLeft--; // decrement count
                    if (playersLeft == 0) {
                        // if no more players left to acknowledge, quit game
                        quitGame();
                    }
                }
            }
            // if no player has given an action, just wait some more
            else if (action.isNullAction()) {
                Thread.yield();
            }
            // if we get an action that is anything but a "quit acknowledge",
            // tell user that it's invalid
            else {
                source.invalidRequest();
            }
        }
    }

    /**
     * Tells the numeric position of a player in the game
     *
     * @param gp  the player 
     * @return the numeric position of the player in the game, -1 if the
     * player is not part of the game
     *
     */
    protected int indexOf(GamePlayer p) {
        // find the player in the array, returning the index when found
        for (int i = 0; i < player.length; i++) {
            if (player[i] == p) return i;
        }
        
        // if we get here, the player wasn't there, so return -1
        return -1;
    }

    /**
     * Gets the next action requested by a player.
     *
     * @return the next action requested by a player, or a GameNullAction if
     *  no player has requested an action.
     */
    private GameAction getAction() {
        // synchronize so that we don't get messed up with threads
        // testing/modifying the vector at the same time
        synchronized(actionVec) {
            // there is no request, return a GameNullAction
            if (actionVec.isEmpty()) return new GameNullAction(null);
            // if there is at least one request, remove the first one
            // and return it
            else {
                GameAction rtnVal = (GameAction)actionVec.elementAt(0);
                actionVec.removeElementAt(0);
                return rtnVal;
            }
        }
    }

    /**
     * Ends the game.
     */
    private void quitGame() {
    
        // tell each player to finish up; then exit
        for (int i = 0; i < player.length; i++) {
            if (player[i] != null) player[i].finishUp();
        }
        System.exit(0);
    }

    /**
     * Applies an action to the GameImpl object.  Typically called by a player
     * who wants to request an action on its behalf.
     *
     * @param the player requesting the action
     */
    public void applyAction(GameAction action) {
        // synchronize so that we don't get messed up with threads
        // testing/modifying the vector at the same time
        synchronized(actionVec) {
            // as long as the action is not null, add it to the end of our
            // action vector
            if (action != null) {
                actionVec.addElement(action);
            }
        }
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
     * Tells the player-number of a given player
     * 
     * @param gp The player we're asking about
     * @return the players index (e.g., 0 if it's the first player, 1 if
     *   it's the second, etc.)
     */
    private int whoAmI(GamePlayer gp) {
        if (gp == null || player == null) return -1;
        return indexOf(gp);
    }

    /**
     * Waits for all players to acknowledge that they are ready to play
     * the game.
     * 
     */
    private void waitForAllReady() {
    
        // continually loop through the array of players until all are
        // ready to play
        for (;;) {
            boolean foundStraggler = false;
            for (int i = 0; i < player.length; i++) {
                foundStraggler |= player[i] != null && !player[i].isReady();
            }
            if (!foundStraggler) {
                // perform any action that is necessary after all
                // players are ready; then return
                performAfterAllAreReady();
                return;
            }
            // If straggler was wait for one second before trying again
            wait(1000);
        }
    }
    
    /**
     * Performs game-specific action that needs to be done after all
     * players are ready (e.g., initializing timers).
     * 
     */
    protected void performAfterAllAreReady() {
        // by default, do nothing
    }

    /**
     * Waits for a specified amount of time
     *
     * @param milliSeconds the number of milliseconds to wait
     * 
     */
    private void wait(int milliSeconds) {
    
        // perform a "sleep" for the specified number of milliseconds;
        // if an "interruptedException" occurs, return early.
        try {
            Thread.sleep(milliSeconds);
        }
        catch (InterruptedException ix) {
        }
    }
    
    /**
     * Notifies all players that the state has changed.  Performs each
     * notification in a separate thread so that players don't have to
     * wait for one another
     */
    protected final void notifyAllStateChanged() {
        // notify all players that the state of the game has changed
        performActionOnAll(new StateChangedInvoker());
    }
    
    protected final void performActionOnAll(PlayerActionInvoker pai) {
        
        for (int i = 0; i < player.length; i++) {
            if (player[i] != null) {
                Thread t = new Thread(new ParallelNotifier(player[i], pai));
                t.start();
            }
        }
    }
    
    /**
     * A class that invokes the 'requestMove' action.
     */
     class RequestMoveInvoker extends PlayerActionInvoker {
     
        /**
         * The action for this method
         *
         * @param player  the player on which to invoke the action
         */
         public void invokeAction(GamePlayer player) {
             if (canMove(player)) {
                 player.requestMove();
             }
         }
    }
}


        
    
    /**
     * An inner class that helps invoke the 'stateChanged' method on
     * a player, using a separate thread.
     */
    class ParallelNotifier implements Runnable {
    
        // the player to invoke the action on
        private GamePlayer myPlayer;
        
        // the action to perform
        private PlayerActionInvoker actionObject;
        
        /**
         * Constructor for ParallelNotifier
         * 
         * @param player  the player to invoke the action on
         * @param pai  the object representing the action to invoke
         */
        public ParallelNotifier(GamePlayer player, PlayerActionInvoker pai) {
        
            // invoke superclass constructor
            super();
            
            // initialize instance variables
            myPlayer = player;
            actionObject = pai;
        }
    
        /**
         * The code that runs in the separate thread.
         */
        public void run() {
            // invoke the appropriate method on the player
            actionObject.invokeAction(myPlayer);
        }
    }   
    
/**
 * An abstract class specifies an action to perform.
 */
abstract class PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public abstract void invokeAction(GamePlayer player);
} 
       
/**
 * A class that invokes the 'stateChanged' action.
 */
class StateChangedInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.stateChanged();
    }
}
 
/**
 * A class that invokes the 'timeToQuit' action.
 */
class TimeToQuitInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.timeToQuit();
    }
}
  
/**
 * A class that invokes the 'gameIsOver' action.
 */
class GameIsOverInvoker extends PlayerActionInvoker { 
    /**
     * The action for this method
     * 
     * @param player  the player on which to invoke the action
     */
    public void invokeAction(GamePlayer player) {
        player.gameIsOver();
    }
}
     

    