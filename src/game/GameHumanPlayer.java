package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
    
/**
 * This is a generic Human player.  Because Humans interace with the
 * game via a GUI, this class is a JFrame class.  This class will need to
 * be subclassed in order to be used to play a particular game.
 * 
 * @author Steven R. Vegdahl 
 * @version 30 July 2002
 */
public abstract class GameHumanPlayer
    extends JFrame
    implements GamePlayer, ActionListener, MouseListener, WindowListener
{
    ///////////////////////////////////////////////////////
    // instance variables
    ///////////////////////////////////////////////////////
    
    /**
     * The game object.
     */
    protected Game game;

    // The player's ID, from the game's viewpoint
    private int playerId;

    // used to generate unique event-action id's
    private static int localEventCount = 2000;

    // the GUI's default height and width, in pixels
    private static final int DEFAULT_HEIGHT = 400;
    private static final int DEFAULT_WIDTH = 400;

    /**
     * The GUI's initial height.
     *
     * @return the GUI's inital height, in pixels.
     */
    protected int initialHeight() { return DEFAULT_HEIGHT; }

    /**
     * The GUI's initial width.
     *
     * @return the GUI's inital width, in pixels.
     */
    protected int initialWidth() { return DEFAULT_WIDTH; }

    // the "quit" button
    private JButton quitButton;

    // a "button" that does not show up on the GUI, but is rather used
    // to generate events
    private DummyButton dummyButton;

    /**
     * Lets the player know what the game object is.
     * 
     * @param game the game object
     * @param playerId the unique numeric player-id assigned to this
     *  player by the game
     */
    public void setGame(Game game, int playerId) {
        // disallow setting game if one is already set
        // (actually, we're allowing that so that the remote-GUI stuff
        // will work)
        if (true || this.game == null) {
            this.game = game; // set game
            this.playerId = playerId; // set player Id
            setGameMore(); // do additional initialization
            updateTitle(); // set title
            this.setVisible(true); // show the GUI to the user
        }
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
     * Updates the GUI's title.
     */
    protected void updateTitle() {
        this.setTitle(this.defaultTitle()); // set title
    }

    /**
     * performs additional initialization, as required by the GUI
     *
     */
    protected void setGameMore() {
        // does nothing, but may be overriden by subclasses to do additional
        // initialization
    }

    /**
     * returns the GUI's title-window
     *
     * @return the title to be put in the GUI's window
     */
    protected String defaultTitle() {
        // Since we don't know the game, we'll be "real generic".  This
        // method really should be subclassed.
        return "A Game";
    }

    /**
     * GameHumanPlayer constructor.
     */
    public GameHumanPlayer() {

        // perform any superclass initialization
        super();

        // set the game to null and player id to -1, indicating that
        // the player is not yet attached to any game
        game = null;
        playerId = -1;

        // create a dummy button for receiving messages; set ourself up
        // as the listener
        dummyButton = new DummyButton("dummy");
        dummyButton.addActionListener(this);

        // set the GUI's initial size
        setSize(initialWidth(), initialHeight());

        // create a box and add it to the GUI's top pane
        Container topPane = getContentPane();
        Box semiTopPane = new Box(BoxLayout.X_AXIS);
        // create an application-specific GUI component; this is where
        // most of the action will occur
        Component applComponent = createApplComponent();
        
        // set our background color to match that of the application component
        topPane.setBackground(applComponent.getBackground());

        // create a Box to contain the application-specific portion of
        // the GUI and a "quit" button
        Box contentPane = new Box(BoxLayout.Y_AXIS);
        contentPane.add(Box.createGlue());
        contentPane.add(applComponent);
        contentPane.add(Box.createGlue());
        quitButton = addButton("Quit", contentPane, this);
        contentPane.add(Box.createVerticalGlue());

        // connect all the top-level GUI structure
        semiTopPane.add(Box.createHorizontalGlue());
        semiTopPane.add(contentPane);
        semiTopPane.add(Box.createHorizontalGlue());
        topPane.add(semiTopPane);

        // listen to the window so that we can interrupt a click on
        // the "quit" box
        this.addWindowListener(this);
    }

    /**
     * creates the application-specific GUI component
     *
     * @return the application-specific component of the GUI
     */
    protected Component createApplComponent() {
        // for now, just return an empty panel.  It is expected that this
        // method will be subclassed
        return new Panel();
    }

    /**
     * Handles events generated in response to human action, or by the
     * game.
     *
     * @param ae the action event
     */
    public void actionPerformed(ActionEvent ae) {
        // get the text describing the command
        String command = ae.getActionCommand();

        // user pressed "quit" button 
        if (command.equals("Quit")) {
            // request a "quit" from the game
            requestQuit();
        }
        // game told us it's time to quit
        if (command.equals("cmdQuit")) {
            // display dialog box to user; wait for response
            infoMessage("It's time to quit");
            // acknowledge to game object that it's time to quit
            game.applyAction(new GameQuitAcknowledgeAction(this));
        }
        // game told us that the game is over
        else if (command.equals("cmdOver")) {
            // display dialog box to user; wait for response
            infoMessage("Game is over");
            // acknowledge to game object that the game is over
            game.applyAction(new GameQuitAcknowledgeAction(this));
        }
        // game told us that we sent an invalid operation
        else if (command.equals("cmdInvalid")) {
            // display dialog box to user; wait for response
            errorMessage("Invalid operation");
        }
        // game told us that we sent an illegal move
        else if (command.equals("cmdBadMove")) {
            // display dialog box to user; wait for response
            errorMessage("Illegal move");
        }
        // game told us that we sent a move when it was not our turn
        else if (command.equals("cmdNotYours")) {
            // display dialog box to user; wait for response
            errorMessage("It's not your move");
        }
        else {
            // if none of the above match, then the event must be in
            // response to a widget in our game-specific GUI: respond to
            // it
            this.moreActionPerformed(ae);
        }
    }

    /**
     * Give the user an information dialog box
     *
     * @param message the text to display to the user
     */
    protected static void infoMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
            "Please confirm", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Give the user an error dialog box
     *
     * @param message  the text to display to the user
     */
    protected static void errorMessage(String message) {
        JOptionPane.showMessageDialog(null, message,
            "Please confirm", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * handles specific action events that the generic GameHumanPlayer
     * does not know about.
     * @param ae  the event object 
     */
    protected void moreActionPerformed(ActionEvent ae) {
        // by default, do nothing.  Subclasses should override this if they
        // want to respond to specific events.
    }

    /**
     * make a "quit" request to the game
     */
    private void requestQuit() {
        game.applyAction(new GameQuitAction(this));
    }

    /**
     * submit an action to the player.  This will cause me to respond to
     * an event in the GUI's main thread.  (Typically, this is called by the
     * game from the game's thread.)
     *
     * @param actionType  a string describing the action (e.g., "you won").
     */
    protected final void submitAction(String actionType) {
        // create the event
        ActionEvent event =
            new ActionEvent(dummyButton, localEventCount++, actionType);
        // submit the event
        dummyButton.submitActionEvent(event);
    }

    /**
     * Lets the player know that it should make a move.  Typically called
     * by the game object.
     */
    public void requestMove() {
        // since this a generic game, we do nothing
    }

    /**
     * Notifies the player know that it's not his move.  Typically
     * called by the game object when the player has made a move out-of-turn.
     */
    public void notYourMove() {
        // notify GUI in its main thread
        this.submitAction("cmdNotYours");
    }

    /**
     * Notifies a player that the move he made is illegal.  (For example,
     * moving a non-king checkers-piece backwards.) Typically called by
     * the game object when an illegal move has been detected.
     */
    public void illegalMove() {
        // notify GUI in its main thread
        this.submitAction("cmdBadMove");
    }

    /**
     * Notifies a player that it is time to quit.  Typically called by the
     * game object when a player has indicated a desire to quit.
     */
    public void timeToQuit() {
        // notify GUI in its main thread
        this.submitAction("cmdQuit");
    }

    /**
     * Notifies a player that a request he had made is invalid.  (For
     * example, if the player request a "quit", but he is not allowed to.)
     * Typically called by the game object.
     */
    public void invalidRequest() {
        // notify GUI in its main thread
        this.submitAction("cmdInvalid");
    }

    /**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public void gameIsOver() {
        // notify GUI in its main thread
        this.submitAction("cmdOver");
    }

    /**
     * Notifies a player that the state of the game has changed.  Typically
     * called by the game object when an opponent has made a move, and the
     * player may want to take the move into account (e.g., to display it
     * on the screen).
     */
    public void stateChanged() {
        // for now, do nothing
    }

    /**
     * Invoked when the mouse has been clicked on a component.
     *
     * @param e  the event object
     */
    public void mouseClicked(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a mouse button has been pressed on a component.

     *
     * @param e  the event object
     */
    public void mousePressed(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a mouse button has been released on a component.

     *
     * @param e  the event object
     */
    public void mouseReleased(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the mouse enters a component.

     *
     * @param e  the event object
     */
    public void mouseEntered(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the mouse exits a component.

     *
     * @param e  the event object
     */
    public void mouseExited(MouseEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the user attempts to close the window from the
     * window's system menu.
     *
     * @param e  the event object
     */
    public void windowClosing(WindowEvent e) {
        // treat it as if the user pressed the "quit" button
        requestQuit();
    }

    /**
     * Invoked the first time a window is made visible.
     *
     * @param e  the event object
     */
    public void windowOpened(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window has been closed as the result of calling
     * dispose on the window.
     *
     * @param e  the event object
     */
    public void windowClosed(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is changed from a normal to a minimized state.
     *
     * @param e  the event object
     */
    public void windowIconified(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is changed from a minimized to a normal state.
     *
     * @param e  the event object
     */
    public void windowDeiconified(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when the window is set to be the user's active window,
     * which means the window (or one of its subcomponents) will receive
     * keyboard events.
     *
     * @param e  the event object
     */
    public void windowActivated(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * Invoked when a window is no longer the user's active window, which
     * means that keyboard events will no longer be delivered to the window
     * or its subcomponents.
     *
     * @param e  the event object
     */
    public void windowDeactivated(WindowEvent e) {
      // by default, do nothing (i.e., ignore it)
    }

    /**
     * helper-method to add a button the GUI
     *
     * @param text the button's text
     * @param where the contiainer to which the button should be added
     * @param listener the listener that should listen to the button
     * @return the button created
     */
    protected JButton addButton(String text, Container where,
            ActionListener listener) {

        // create the button and add it
        JButton rtnVal = new JButton(text);
        where.add(rtnVal);

        // set up the listener
        rtnVal.addActionListener(listener);

        // set the button's action text to be the same as its displayed
        // text
        rtnVal.setActionCommand(text);

        // return the created button
        return rtnVal;
    }

    /**
     * Notifies the player that all players have acknowledged that the game
     * will be "quit". Gives the player last chance to "clean up".  (E.g.,
     * closing windows, terminating network connections, closing files.)
     * Typically called by the Game object just before the entire program
     * terminates.
     */
    public void finishUp() {
        // hide and dispose of the GUI
        this.setVisible(false);
        this.dispose();
    }

    /**
     * Tells whether the player is ready to play the game.
     *
     * @return a boolean value indicating whether the player is ready
     *   to play.
     */
    public boolean isReady() {
        // typically, a GUI player is always ready to play
        return true;
    }
}

/**
 * A helper-class that defines a button that is not shown on the GUI, but is
 * rather used via the game object to generate actions for the player objects.
 */
class DummyButton extends Button
{
    /**
     * Constructor for objects of class DummyButton
     *
     * @param name the text that is to appear on the button.
     */
    public DummyButton(String name)
    {
        // perform superclass initialization
        super(name);
    }

    /**
     * submits an action event to the button.  This has the effect of causing
     * 'actionPerformed' to be called on the button's listener.
     *
     * @param ae the ActionEvent object to be sent to the button's listener
     */
    public void submitActionEvent(ActionEvent ae)
    {
        // cause the event to be processed
        super.processActionEvent(ae);
    }
}
