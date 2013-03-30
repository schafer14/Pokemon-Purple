package game;

import java.io.*;

/**
 * A Game object that is used as a proxy for the real game that is on another
 * machine on the network.  Each ProxyGame is associated with exactly one
 * Player object.  Whenever a method is invoked on the ProxyGame object,
 * it encodes the message as a string and sends it across the network; when
 * ever the ProxyGame object receives a message from the network, it decodes
 * the string and invokes the corresponding method its Player.
 * 
 * @author Steven R. Vegdahl
 * @version 23 July 2002
 */
public abstract class ProxyGame extends ProxyObject
    implements Game, Runnable
{

    // the player associated with this game
    private GamePlayer player;

    // tells whether the game has started
    private boolean gameIsStarted;

    // tells whether it is time to quit
    private boolean quittingTime;

    // the game's state
    private GameState gameState;

    /**
     * ProxyGame constructor.
     *
     * @param hostName  the name of the remote site to where the actual
     *  game is running
     */
    public ProxyGame(String hostName) {

        // invoke superclass constructor
        super();

        // set instance variables to their initial values
        player = null;
        gameIsStarted = false;
        quittingTime = false;
        gameState = null;

        // attempt to connect as a client
        this.connectAsClient(hostName);

        // if connection was successful, start running in separate thread
        if (this.isComplete()) {
            Thread t = new Thread(this);
            t.start();
        }
        else {
            // error occured, so emit message to console
            System.out.println("Connection error: ProxyGame");
        }
    }

    /**
     * The code that runs during the object's lifetime.
     *
     */
    public void run() {

        // keep processing input until it's time to quit
        while (!quittingTime) {
            processInput(); // process input
            Thread.yield(); // let other threads have a chance to run
        }

        // quit the program
        System.exit(0);
    }

    /**
     * Reads (from the network) and processes a line of input.  Parses
     * the command and, when appropriate, invokes operations on the Player
     * object.
     */
    private void processInput() {
    
            // read the line of text from the network
            String str = readInputLine();
            
            // We have these possibilities:
            // - string starts with '*' -- returning game ID
            // - string starts with ':' -- generic encoded action
            // - string starts with a '%' -- game-specific encoded action
            // - string starts with ' ' -- encoded state
            
            if (str.length() == 0) {
                // string is empty: ignore
            }
            else if (str.charAt(0) == '*') {
                // string starts with '*': set the state to the game's id by
                // parsing the rest of the string and creating an
                // IntGameState object containing the corresponding number
                String rest = str.substring(1); // all but first cahr
                int id = -1; // default value
                try {
                    id = Integer.parseInt(rest); // parse as number
                }
                catch (NumberFormatException nfx) {
                }
                this.setState(new IntGameState(id)); // update game state
            }
            else if (str.charAt(0) == ':') {
                // string starts with ':': decode the generic action
                String rest = str.substring(1); // rest of string
                if (rest.startsWith("setGame")) {
                    // we have a 'setGame' command
                    
                    // parse the number as the player's position in the game
                    int id = 0;
                    try {
                        int idx = rest.indexOf(" ")+1;
                        id = Integer.parseInt(rest.substring(idx).trim());
                    }
                    catch (NumberFormatException nfx) {
                    }
                    
                    // wait for player to be set
                    while (player == null) {
                        Thread.yield();
                    }
                    
                    // invoke the setGame operation
                    player.setGame(this, id);
                }
                // ignore all further commands unless the game has started
                else if (!gameIsStarted) {
                }
                else if (rest.equals("requestMove")) {
                    // invoke the 'requestMove' operation on the player
                    player.requestMove();
                }
                else if (rest.equals("notYourMove")) {
                    // invoke the 'notYourMove' operation on the player
                    player.notYourMove();
                }
                else if (rest.equals("timeToQuit")) {
                    // invoke the 'timeToQuit' operation on the player
                    player.timeToQuit();                        
                }
                else if (rest.equals("invalidRequest")) {
                    // invoke the 'invalidRequest' operation on the player
                    player.invalidRequest();
                }
                else if (rest.equals("illegalMove")) {
                    // invoke the 'illegalMove' operation on the player
                    player.illegalMove();
                }
                else if (rest.equals("gameIsOver")) {
                    // invoke the 'illegalMove' operation on the player
                    player.gameIsOver();
                }
                else if (rest.equals("stateChanged")) {
                    // invoke the 'stateChanged' method in another thread
                    // so that we don't have deadlock problems
                    Thread t = new Thread(new StateGetter());
                    t.start();
                }
                else if (rest.equals("finishUp")) {
                    // invoke the 'finishUp' operation on the player, and
                    // mark game as being done
                    player.finishUp();
                    quittingTime = true;
                }
                
            }
            else if (str.charAt(0) == '%'){
                // process the game-specific message
                receiveMessage(str.substring(1));
            }
            else if (str.charAt(0) == ' '){
                // we have received game state; decode it and set our "state"
                // object
                GameState ac =
                    decodeState(str.substring(1));
                if (ac != null) {
                    this.setState(ac);
                }
            }

    }

    /**
     * Sets the game's state object, assuming it's not already set.
     * 
     */
    private void setState(GameState state) {
        synchronized(this) { // synch to avoid a race condition
            if (gameState == null) {
                gameState = state;
            }
        }

    }

    /**
     * gets information about the state of the game.  Only gives information
     * if the given player is allowed to know that information.  (Typically,
     * the GameState object will be a subclass of GameState that applies to
     * that particular game.
     *
     * @param p the player asking the question
     * @param stateType a game-dependent encoding, specifying the particular
     *  type of information the player wants
     * @return an object that contains the information that the caller
     *  requested
     */
    public GameState getState(GamePlayer p, int stateType) {
        if (p != player) {
            // not the right player: give him "dummy" information
            return this.decodeState("");
        }
        else {
        
            // send a message across the network requesting state information
            writeOutputLine(""+stateType);
            
            // continuously check for state information being set
            for (;;) {
                synchronized(this) { // synch to avoid race condition
                    if (gameState != null) {
                        // game state is there; clear state and return
                        // state object
                        GameState temp = gameState;
                        gameState = null;
                        return temp;
                    }
                }
                
                // game state was not there, yield control and then loop back
                Thread.yield();
            }
        }
    }

    /**
     * applies an action to the Game object.  Typically called by a player
     * who wants to request an action behalf.
     *
     * @param action  the action object to apply
     */
    public final void applyAction(GameAction action) {
    
        // attempt to encode the action as a "generic action"
        String str = encodeBasicAction(action);
        
        // send encode action over the network so that it is applied
        // to the "real" game
        if (str != null) {
            // generic action: send with '#' prefix
            writeOutputLine("#"+str);
        }
        else {
            // game-specific action: send with ':' prefix
            writeOutputLine(":"+encodeAction(action));
        }
    }

    /**
     * Encode one of the standard "generic" actions
     *
     * @param action the action to encode
     * @return the string that is the encoding of the action, or null
     *  if the action was one of the standard "generic" ones
     **/
    private final String encodeBasicAction(GameAction action) {
        if (action instanceof GameMoveAction) {
            // game-specific action: return null
            return null;
        }
        else if (action instanceof GameNullAction) {
            // generic null action
            return "null";
        }
        else if (action instanceof GameQuitAction) {
            // generic quit action
            return "quit";
        }
        else if (action instanceof GameQuitAcknowledgeAction) {
            // generic quit-acknowledge action
            return "quitAcknowledge";
        }
        else {
            // something else: return null
            return null;
        }
    }

    /**
     * Transforms a string (e.g., sent over the network) into an appropriate
     * GameState object for the game.  This should be the inverse of
     * the game's ProxyPlayer-subclass' encodeState method.
     *
     * @param str  the string to decode
     * @return the resulting GameState object
     */
    protected abstract GameState decodeState(String str);

    /**
     * Transforms an action into a String object for sending over the
     * network.  This should be the inverse of the game's ProxyPlaner-
     * subclass' decodeAction method.
     *
     * @param ga  the game action to be encoded.
     * @return the String encoding of the action
     */
    protected abstract String encodeAction(GameAction ga);

    /**
     * tells the game which players are playing.  Checks that the array
     * consists of a set of players that can legally play the game (e.g.,
     * not too many or too few.
     *
     * @param the array of players playing the game
     * @return null if game is set up properly; an error message is there
     *   was a problem
     */
    public String setPlayers(GamePlayer[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                player = players[i];
                return null;
            }
        }
        return null;
    }

    /**
     * Begins the actual game.
     */
    public void playGame() {
        // mark the game as having started
        gameIsStarted = true;
    }
    
    /**
     * Gets the player that is associated with this game.
     * 
     * @return the player that is associated with this ProxyGame.
     */
    protected GamePlayer getPlayer() {
        return player;
    }

    /**
     * Process a game-specific message that has been sent over the network.
     * 
     * @param  the message
     */
    protected void receiveMessage(String str) {
        // specific games should override this if they want to process
        // any game-specific messages
    }
    
    /**
     * An inner class that helps invoke the 'stateChanged' method on
     * a player, using a separate thread.
     */
    private class StateGetter implements Runnable {
    
        /**
         * The code that runs in the separate thread.
         */
        public void run() {
            // invoke the stateChanged method on the player
            player.stateChanged();
        }
    }
}
