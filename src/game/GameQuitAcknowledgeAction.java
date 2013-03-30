package game;

/**
 * A GameQuitAcknowledgeAction is an action that is sent by a player to the
 * game that acknowledges to the game that the player understands that it's
 * time to quit.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2001
 */
public class GameQuitAcknowledgeAction extends GameAction {

    /**
     * constructor for GameQuitAcknowledgeAction
     *
     * @param source the player who created the action
     */
    public GameQuitAcknowledgeAction(GamePlayer p)
    {
        // invoke superclass constructor to initialize the source
        super(p);
    }

    /**
     * tells whether the action is a "quit acknowledge" action
     *
     * @return true iff the action is an acknowledgement that it's time
     *  to quit the game
     */
    public boolean isQuitAcknowledge() {
        // return "true" since this object is a "quit acknowledge" action
        return true;
    }
}
