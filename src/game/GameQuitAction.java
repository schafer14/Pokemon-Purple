package game;

/**
 * A GameQuitAction is an action that is sent by a player to the game
 * to tell it that it wants to quit the game.
 * 
 * @author Steven R. Vegdahl
 * @version 2 July 2001
 */
public class GameQuitAction extends GameAction {

    /**
     * constructor for GameQuitAction
     *
     * @param source the player who created the action
     */
    public GameQuitAction(GamePlayer p)
    {
        // invoke superclass constructor to initialize the source
        super(p);
    }

    /**
     * tells whether the action is a "quit game" action
     *
     * @return true iff the action is a "quit game" action
     */
    public boolean isQuitRequest() {
        return true;
    }
}
