package game;

/**
 * An action for a generic game.  A game action is something that a player
 * tells the game that it wants to do (e.g., put an 'X' on the top-left
 * tic-tac-toe square).  The game will then decide whether the player is
 * allowed to perform that action before effecting the action on the
 * players behalf.  Most real games will subclass GameMoveAction (which
 * is a subclass of GameAction) to define actions that are relevant to the
 * particular game.  A GameAction contains the player as part of its state;
 * this way the game always knows what player sent it the action.
 * <P>
 * Several "generic" of GameAction classes are already defined.  These
 * include GameQuitAction, GameQuitAcknoledgeAction and GameMoveAction.
 *
 * @author Steven R. Vegdahl 
 * @version 2 July 2001
 */
public abstract class GameAction
{
    // the player who generated the request
    private GamePlayer source;

    /**
     * constructor for GameAction
     *
     * @param source the player who created the action
     */
    public GameAction(GamePlayer source) {
        this.source = source;
    }

    /**
     * tells the player who created the action
     *
     * @return the player who created the action
     *
     */
    public GamePlayer getSource() {
        return source;
    }

    /**
     * tells whether the action is a "move" action (e.g., put an 'X' on
     * top-left square), as opposed to an administrative action (e.g.,
     * quit game)
     *
     * @return true iff the action is a "move" action
     */
    public boolean isMove() {
        // by default, it's not a move.  (This method is overridden in
        // GameMoveAction.)
        return false;
    }

    /**
     * tells whether the action is a "timer" action (typically generated
     * from a GameTimer object).
     *
     * @return true iff the action is a "timer" action
     */
    public boolean isTimer() {
        // by default, it's not a timer action.  (This method is overridden
        // in GameTimerAction.)
        return false;
    }

    /**
     * tells whether the action is a "quit game" action
     *
     * @return true iff the action is a "quit game" action
     */
    public boolean isQuitRequest() {
        // by default, it's not a quit-request.  (This method is overridden in
        // GameQuitAction.)
        return false;
    }

    /**
     * tells whether the action is a "quit acknowledge" action
     *
     * @return true iff the action is an acknowledgement that it's time
     *  to quit the game
     */
    public boolean isQuitAcknowledge() {
        // by default, it's not a quit-ack.  (This method is overridden in
        // GameQuitAcknowledgeAction.)
        return false;
    }

    /**
     * tells whether the action is a "null" action
     *
     * @return true iff the action is null action, indicating that no
     *  actions have been requested by any player
     */
    public boolean isNullAction() {
        // by default, it's not a null action.  (This method is overridden in
        // GameNullAction.)
        return false;
    }
}
