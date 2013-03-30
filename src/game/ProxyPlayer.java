package game;

import java.io.*;
    
/**
 * A Player object that is a proxy for the real player, which is somewhere
 * else on the network.  Each ProxyPlayer
 * 
 * @author Steven R. Vegdahl
 * @version 8 Nov 2001
 */
public abstract class ProxyPlayer extends ProxyObject
    implements GamePlayer, Runnable
{
    
    // the game object
    private Game game;
    
    // tells whether it is time to quit
    private boolean quittingTime;
    
    // The thread object that requests a move across the network.  This
    // allows the move-request operation to occur in parallel with other
    // operations.  If this variable is non-null, it means that there is
    // a pending 'requestMove' operation in progress, and so another one
    // should not be started.
    private Thread requestMoveThread;
    
    // this player's ID, from the game's viewpoint
    private int playerId;
    
    // whether this player is ready to play the game.  In this case, being
    // ready essentially means that the a connection with a remote player
    // has been established
    private boolean isReady;

    /**
     * Transforms a GameState object into a string so that it may be
     * sent across the network.  The decodeState method (of the ProxyGame
     * subclass that is associated with this game) should be written so
     * that it can reconstruct the object from the string.
     * 
     * @param gs the GameState object
     * @return the encoded string
     */
    protected abstract String encodeState(GameState gs);

    /**
     * Transforms a string into a GameAction object. This method should
     * be written to reconstruct any object that had been transformed to
     * a string by the ProxyGame subclass for this game.
     * 
     * @param s the string to transform
     * @return the GameAction object that corresponds to the string
     */
    protected abstract GameAction decodeAction(String s);
    
    /**
     * ProxyPlayer constructor.
     */
    public ProxyPlayer() {
    
        // perform superclass initialization
        super();
        
        // set instance variables to their initial values
        game = null;
        requestMoveThread = null;
        quittingTime = false;
        isReady = false;
        playerId = -1;
        
        // start the thread for this object that processes network requests
        Thread t = new Thread(this);
        t.start();
    }

    /**
     * The code that runs during the object's lifetime.
     *
     */
    public void run() {
    
        // wait for a connection from a remote player
        this.connectAsServer();
        
        // if the connection was successful, mark self as ready to play
        // the game; otherwise abort
        if (this.isComplete()) {
            isReady = true;
        }
        else {
            return;
        }
        
        // Continuously read input from the network and process it.
        // Do this until it's time to quit
        //
        while (!quittingTime) {
        
            // read the next string sent over the network, sent from
            // the remote player
            String str = readInputLine();
            
            // If the string starts with a colon, then it's an
            // game-specific encoded action.  If it starts with a'#',
            // then it's a generic action.  Otherwise, it's a "getState"
            // call, where the string is the ascii-ized version of the
            // stateType parameter
            
            if (str.length() == 0) {
                // string empty: ignore
            }
            else if (str.charAt(0) == ':') {
                // game-specific action: decode action and, if not null,
                // apply it to the game
                GameAction ac =
                    decodeAction(str.length() == 0 ? "" : str.substring(1));
                if (ac != null) {
                    game.applyAction(ac);
                }
            }
            else if (str.charAt(0) == '#') {
                // generic action: parse and perform appropriate action
                // on the game
                //
                String cmd = str.substring(1); // remove '#'
                if (cmd.equals("null")) {
                    // null action
                    game.applyAction(new GameNullAction(this));
                }
                else if (cmd.equals("quit")) {
                    // quit action
                    game.applyAction(new GameQuitAction(this));
                }
                else if (cmd.equals("quitAcknowledge")) {
                    // quit-acknowledge action
                    game.applyAction(new GameQuitAcknowledgeAction(this));
                }
                else {
                    // anything else: apply a null action
                    game.applyAction(new GameNullAction(this));
                }
            }
            else {
                // it's a "get state" request.  The string contains a number
                // which is the parameter that should be passed to the
                // 'getState' method
                
                int val = 0; // default parameter value
                try {
                    // transform the number into a string
                    val = Integer.parseInt(str.trim());
                }
                catch (NumberFormatException nfx) {
                    // if number is not a string, leave value at 0
                }
                
                // get the state from the game
                GameState gs = game.getState(this, val);
                
                if (val == -1) {
                    // parameter is -1: this means we are asking for the
                    // player's ID and so the state will be an IntGameState
                    // object that holds the ID.  Send the value over the
                    // network, prefixed with a '*'.
                    writeOutputLine("*"+((IntGameState)gs).getNum());
                }
                else {
                    // it's game-specific state.  Encode it as a string
                    // and send it over the network, prefixed with a ' '.
                    String encodedState = encodeState(gs);
                    writeOutputLine(" "+encodedState);
                }
            }
            
            // allow other threads to take their turn
            Thread.yield();
        }
    }

    /**
     * Lets the player know what the game object is.
     * 
     * @param game the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
    public void setGame(Game game, int playerId) {
    
        // set our local variables appropriately
        this.game = game;
        this.playerId = playerId;
        
        // also apply the operation to the "real" player across the network
        sendGenericMessage("setGame "+playerId);
    }

    /**
     * Lets the player know that the game object wants the him to
     * make a move.
     */
    public void requestMove() {
        // if there is not already a move-request in progress for this
        // player, start up a thread to request a move from the "real"
        // player across the network
        if (requestMoveThread == null) {
            requestMoveThread = new Thread(new MoveRequester());
            requestMoveThread.start();
        }
    }

    /**
     * Notifies the player know that it's not his move.  Typically
     * called by the game object when the player has made a move out-of-turn.
     */
    public void notYourMove() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("notYourMove");
    }

    /**
     * Notifies a player that it is time to quit.  Typically called by the
     * game object when a player has indicated a desire to quit.
     */
    public void timeToQuit() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("timeToQuit");
    }

    /**
     * Notifies a player that a request he had made is invalid.  (For
     * example, if the player request a "quit", but he is not allowed to.)
     * Typically called by the game object.
     */
    public void invalidRequest() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("invalidRequest");
    }

    /**
     * Notifies a player that the move he made is illegal.  (For example,
     * moving a non-king checkers-piece backwards.) Typically called by
     * the game object when an illegal move has been detected.
     */
    public void illegalMove() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("illegalMove");
    }

    /**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public void gameIsOver() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("gameIsOver");
    }

    /**
     * Notifies a player that the state of the game has changed.  Typically
     * called by the game object when an opponent has made a move, and the
     * player may want to take the move into account (e.g., to display it
     * on the screen.
     */
    public void stateChanged() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("stateChanged");
    }

    /**
     * Notifies the player that all players have acknowledged that the game
     * will be "quit". Gives the player last chance to "clean up".  (E.g.,
     * closing windows, terminating network connections, closing files.)
     * Typically called by the Game object just before the entire program
     * terminates.
     */
    public void finishUp() {
        // forward the operation to the "real" player across the network
        sendGenericMessage("finishUp");
    }

    /**
     * Sends a generic message (e.g., "quit") across the network.
     * 
     * @param msg the message to send
     */
    private void sendGenericMessage(String msg) {
        // send the message, prefixed with a ':'
        writeOutputLine(":"+msg);
    }

    /**
     * Sends a game-specific specific message across the network.
     * 
     * @param msg the message to send
     */
    protected final void sendMessage(String msg) {
        // send the message, prefixed with a '%'
        writeOutputLine("%"+msg);
    }

    /**
     * Gets the player's numeric id from the pespective of the game
     * it's playing in.  (E.g., 0 if player 0, 1 if player 1.)
     *
     * @return the player's numeric id, from the game's viewpoint.
     */
    public int getId() {
        // return the player-id that we have stored in our instance
        // variable
        return playerId;
    }
    
    /**
     * Gets the game
     * 
     * @return the game object we're playing in.
     */
    public Game getGame() {
        return game;
    }

    protected void finishInit() {
        // perform a 'setGame' operation on the "real" (remote) player
        sendGenericMessage("setGame "+playerId);
    }

    /**
     * Tells whether the player is ready to play the game.
     *
     * @return a boolean value indicating whether the player is ready
     *   to play.
     */
    public boolean isReady() {
        // return value based on whether we are marked as being ready
        return isReady;
    }
    
    /**
     * An inner class that helps invoke the 'requestMove' method on
     * a remote player using a separate thread.
     */
    private class MoveRequester implements Runnable {
    
        /**
         * The code that runs in the separate thread.
         */
        public void run() {
            try {
            
                // send the message
                sendGenericMessage("requestMove");
                
                // wait one second so that we are not constantly bombarding
                // the network with messages
                Thread.sleep(1000);
                
            }
            catch (InterruptedException ix) {
            } 
            
            // clear the instance variable, allowing a subsequent
            // 'requestMove' operation to be performed
            requestMoveThread = null;
        } 
    }

}
