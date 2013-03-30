package game;

/**
 * An action that indicates that a game's timer has "ticked".
 * 
 * @author Steven R. Vegdahl 
 * @version 24 July 2002
 */
public class GameTimerAction extends GameAction {

    // the GameTimer object that originated this action
    private GameTimer timer;
    
    /**
     * Constructor for objects of class GameTimerAction
     * 
     * @param t the GameTimer object that sent this action
     */
    public GameTimerAction(GameTimer t)
    {   
        // invoke superclass constructor, creating a dummy timer player
        // to act as a placeholder so that the action has a "player"
        // associated with it (as is required by all actions)
        super(new DummyTimerPlayer());
        
        // set the originating timer as part of the state
        timer = t;
    }

    /**
     * tells whether the action is a "timer" action (typically generated
     * from a GameTimer object).
     *
     * @return true iff the action is a "timer" action
     */
    public boolean isTimer() { return true; }

    /**
     * Get the GameTimer object that sent this action.
     * 
     * @return the object that sent this action 
     */
    public GameTimer getTimer()
    {
        // return the timer that we had saved
        return timer;
    }
}

/**
 * A DummyTimerPlayer object is not really a player.  Rather it masquerades
 * as a player so that a GameTimerAction does not violate the invariant
 * that all actions have an associated player.<P>
 * 
 * Virtually all of the DummyTimerPlayer methods are empty, because it
 * is not expected that such an object will be used as a player in a game.
 */
class DummyTimerPlayer implements GamePlayer {
    
    /**
     * Constructor for DummyTimerPlayer objects
     */
    public DummyTimerPlayer()
    {
    }

    // all the rest of these methods are dummied up, simply so that the
    // GamePlayer interface is satisfied.
    public void setGame(Game game, int playerId) {}
    public void requestMove() {}
    public void notYourMove() {}
    public void timeToQuit() {}
    public void invalidRequest() {}
    public void illegalMove() {}
    public void gameIsOver() {}
    public void stateChanged() {}
    public void finishUp() {}
    public int getId() {
        return -1;
    }
    public boolean isReady() {
        return true;
    }
}
