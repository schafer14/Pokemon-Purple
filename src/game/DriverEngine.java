package game;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * A DriverEngine is a class that interacts with a user to create a game and
 * and allow the user to select what kind of players will be playing.  Its
 * standard use is to execute a line such as the following in the main
 * method of a game-specific driver-class (e.g., MyDriver):<pre>
 *   DriverEngine.play(args, new MyDriver());
 * </pre>
 * where args are the command-line arguments, and the MyDriver class
 * is declared as implementing the GameDriver interface.<p>
 * 
 * Such a call will do one of the following:<ul>
 *   <li>if the command-line arguments are empty, it will prompt the user
 *       via dialog boxes for relatant information about how to create
 *       the game.
 *   <li>if the command-line arguments are not empty, it will read the
 *       options from the command line.  A command-line option is considered
 *       to match a choice if, ignoring case, it either<ul>
 *         <li>it matches it exactly, or
 *         <li>it starts with the same letter as an option, any remaining
 *             characters are a subsequence of that option, and it does
 *             not match any other option.  For example, "hm2" would
 *             match the option "Human player 2" because they both start
 *             with "h", and the remaining characters in the "hm2" appear,
 *             in order, in "uman player 2".
 *       </ul>
 * </ul>
 * 
 * @author Steven R. Vegdahl
 * @version 25 July 2002
 */
public abstract class DriverEngine {
    
    // the game-specific driver object
    private GameDriver driver;
    
    /**
     * Creates and plays a game.  If there are no command-line arguments,
     * it prompts the user for his/her choices via dialog boxes.  If
     * there are command-line argments, it reads the choices from the
     * command-line arguments
     * 
     * @param args the command-line arguments, passed in from 'main'
     * @param gd a GameDriver object for the particular game to be
     *   played
     */
    public static void play(String[] args, GameDriver gd) {
        // variable to hold the driver-engine object
        DriverEngine de = null;
        
        // create a GuiDriverEngine or CmdLineDriverEngine object, depending
        // on whether the command-line arguments are empty.
        if (args.length == 0) {
            de = new GuiDriverEngine(gd);
        }
        else {
            de = new CmdLineDriverEngine(gd, args);
        }
        
        // create and play the game
        de.playGame();
    }
    
    /**
     * Constructor for the DriverEngine class
     * 
     * @param gd the GameDriver object to use in creating game-specific
     *  objects
     */
    protected DriverEngine(GameDriver gd) {
        // initialize the instance variable directly from the parameters
        driver = gd;
    }
   
    /**
     * Gets a selection, based on user input, from a list of choices.
     * The subclass must provide an implementation of this method.  For
     * example, a GUI-based subclass might allow the user to select one of
     * the options via a dialog box, where a command-line subclass might
     * read the next argument from the command-line and match it to a
     * choice.
     * 
     * @param title a possible title for a dialog box
     * @param prompt a possible prompt-string for prompting the user
     * @param options an array of string containing the list of legal
     *   choices
     * @return the index in the options-array of the item that was selected
     */
    protected abstract int getSelection(String title, String prompt,
                                String[] options);
                                
    /**
     * Reads a string specified by the user.  The subclass must provide
     * an implementation of this method.  For example, a GUI-based subclass
     * might allow the user to type the input into a dialog box, where a
     * command-line subclass might read the next argument from the command
     * line.
     * 
     * @param prompt a possible prompt string for prompting the user
     * @return the string that the user specified
     */
    protected abstract String promptAndReadString(String prompt);
    
    /**
     * Notify the user that an error has occured.  The subclass must
     * provide an implementation of this method (e.g., printing message
     * to System.out, putting message in a dialog box, etc.)
     *
     * @param the text to display to the user
     */
    protected abstract void errorMessage(String message);

    // An array containing the selection options for creating a game
    private static String[] remoteOptions = {
                    "On this computer",
                    "On remote computer",
                    "Cancel"};

    /**
     * Creates the game and players, and starts the game.  This method is the
     * "heart" of the GameDriver class.  It prompts the user (via dialog boxes)
     * for the type of game and player(s) to be created.  Then it creates them
     * and begins the game.
     *
     * @param args command-line arguments from 'main'
     */
    private final void playGame() {

        // ask user what kind of game to play
        int response = getSelection(
                "Play game on ...",
                "Where should the game be played?",
                remoteOptions);

        // variable to hold the array of players
        GamePlayer[] p = null;

        // variable to hold the game
        Game game = null;
        
        // depending on user choice, either do network or not
        if (response == 0) { // local game
            // create a "dummy game" so that the createLocalPlayers
            // method knows the minimum and maximum number of players
            // allowed, etc.
            Game dummyGame = driver.createGame(0);

            // interact with the user to create the players
            p = createLocalPlayers(dummyGame);

            // create the game
            game = driver.createGame(p.length);
        }
        else if (response == 1) { // remote game

            // create a single player, which is the one playing on this
            // network node
            p = new GamePlayer[]{createMyRemotePlayer()};

            // if no player was created, notify user and quit
            if (p[0] == null) {
                errorMessage("No player selected");
                System.exit(1);
            }

            // ask user for the name of the remote machine
            String machName =
                promptAndReadString("Remote machine name:");

            // create a "proxy" game that connects to a game on the
            // remote machine
            ProxyGame pg = driver.createRemoteGame(machName);

            // the game-creation operation failed, give error message
            // and exit
            if (pg == null || !pg.isComplete()) {
                errorMessage("Could not make connection to server");
                System.exit(1);
            }
            else {
                game = pg;
            }
        }
        else { // response == 2
            // user said "Cancel", so return without playing the game
            return;
        }

        /////////////////////////////////////////////////////////
        // At this point, the game and players have been created,
        // but they don't know about each other
        /////////////////////////////////////////////////////////

        // tell the game who its players are.  If the operation fails
        // give user an error message and exit.
        String msg = game.setPlayers(p);
        if (msg != null) {
            errorMessage(msg);
            System.exit(1);
        }

        // Tell each player who its game is
        for (int i = 0; i < p.length; i++) {
            p[i].setGame(game, i);
        }

        // start the game
        game.playGame();
    }
    
    /**
     * Creates a array of choices for selecting a player.
     *
     * @param extras the "extra" options (i.e. those in addition
     *   to the player--for example "Cancel", "No more")
     * @return an array of String that contains all the player
     *   options plus the extra ones
     */
    private String[] playerChoiceMenu(String[] extras) {
    
        // compute the local-player choices
        String[] localChoices = driver.localPlayerChoices();
        
        // create a return value that contains room for all the player
        // choices, plus all the extra choices
        String[] rtnVal =
            new String[localChoices.length+(extras.length)];
            
        // copy all the local-player choices--then all the extra choices--
        // into the return-value array
        for (int i = 0; i < localChoices.length; i++) {
            String str = localChoices[i];
            rtnVal[i] = str;
        }
        for (int i = 0; i < extras.length; i++) {
            rtnVal[localChoices.length+i] = extras[i];
        }
        
        // return the filled array
        return rtnVal;
    }

    // the "extra choices" array for a remote player
    private static String[] cancelStrArray = {"Cancel"};

    /**
     * Creates a player who will be playing a remote game.  The type of
     * player selected will be based on user-responses to prompts.
     * 
     * @return the particular kind of player that the user selected
     * 
     */
    private GamePlayer createMyRemotePlayer() {
    
        // create the array of player choices, including a "cancel"
        String menuStrings[] = playerChoiceMenu(cancelStrArray);
        
        // ask user what kind of player to create
        int response = getSelection(
                "Player type ...",
                "Remote player",
                menuStrings);

        // if user says "Cancel", exit the program
        if (response ==  menuStrings.length-1) {
            System.exit(1);
        }
        
        // create and return the appropriate kind of player
        return driver.createLocalPlayer(menuStrings[response]);
    }

    // private arrays for selecting local players; the difference between
    // the two is that we don't allow the user to select "no more" until
    // the minimum number of players have been created.
    private static String[] networkCancelStrArray =
        {"network client", "Cancel"};
    private static String[] networkNoMoreCancelStrArray =
        {"network client", "No more", "Cancel"};

    /**
     * Creates an array of local players.  The type of players selected
     * will be based on user-responses to prompts.
     * 
     * @param dummyGame a Game object that is an instance of the game to
     *   be played.  (This allows us to ask it the minimum and maximum
     *   number of players that are allowed.)
     * @return the array of players for this game, as selected by the user
     * 
     */
    private GamePlayer[] createLocalPlayers(Game dummyGame) {
        // create an array that contains all the player choices, plus
        // the "network player" and "cancel" selections.
        String menuStrings[] = playerChoiceMenu(networkCancelStrArray);
        
        // create a vector for collecting the players
        Vector playerVec = new Vector();

        // the minimum and maximum players that are allowed to participate
        // in a single game
        int min = dummyGame.minPlayersAllowed();
        int max = dummyGame.maxPlayersAllowed();
        
        // the position (counting from the end) of the "network player"
        // selection in the array
        int networkPos = 2;
        
        // iterate until all players have been created
        //
        for (;;) {

            // if we have reached the minimum number of players for the
            // game, we'll add "no more" to our list of choices.  Due to
            // the positions of the selections the we also need to modify
            // our idea of where the "network player" selection is
            if (playerVec.size() == min) {
                menuStrings = playerChoiceMenu(networkNoMoreCancelStrArray);
                networkPos = 3;
            }
            
            // if we've reached the maximum number of players, quit the loop
            if (playerVec.size() >= max) break;

            // ask user what kind of game to play
            int response = getSelection(
                            "Player type ...",
                            "Player #"+(playerVec.size()+1),
                            menuStrings);

            // if user selects "Cancel", quit the program
            if (response == menuStrings.length-1) {
                System.exit(1);
            }
            // if user selects "network client", create it
            else if (response == menuStrings.length-networkPos) {
                playerVec.addElement(driver.createRemotePlayer());
            }
            // if user selects "No More", break out of the loop
            else if (response == menuStrings.length-2) {
                break;
            }
            // otherwise, create a local player of the appropriate type,
            // and add it to the vector
            else {
                playerVec.addElement(
                    driver.createLocalPlayer(menuStrings[response]));
            }
        }

        // if user did not select enough players, exit (I don't think this
        // will ever happen unless the minimum is greater than the maximum)
        if (playerVec.size() < min) {
            errorMessage("Not enough players");
            System.exit(1);
        }
        
        // create an array to return the players in; copy the players
        // into the array; return it
        GamePlayer[] rtnVal = new GamePlayer[playerVec.size()];
        for (int i = playerVec.size()-1; i >= 0; i--) {
            rtnVal[i] = (GamePlayer)playerVec.elementAt(i);
        }
        return rtnVal;
    }
    
    /**
     * Returns the index of an object in an array.  Gives -1 if the object
     * was not found.
     * 
     * @param obj the object we're searching for
     * @param arr the array we're searching
     * 
     */
    protected static int indexOf(Object obj, Object[] arr) {
        // iterate through each element of the array, returning the index
        // whenever we find a match
        for (int i = 0; i < arr.length; i++) {
            if (obj.equals(arr[i])) return i;
        }
        
        // if we get here, the element was not there: return -1
        return -1;
    }
}

/**
 * GUI-based driver engine.  Allows user to create a game by interacting
 * via dialog boxes.
 */
class GuiDriverEngine extends DriverEngine
{
    /**
     * Constructor for GUIDriverEngine class
     * 
     * @param gd the game-specific game-driver object
     */
    protected GuiDriverEngine(GameDriver gd) {
        // perform superclass initialiazation
        super(gd);
    }
   
    /**
     * Gets a selection, based on user input, from a list of choices.
     * Because this is a GUI-based driver, we to this by creating a
     * dialog-box with button for each choice.
     * 
     * @param title a possible title for a dialog box
     * @param prompt a possible prompt-string for prompting the user
     * @param options an array of string containing the list of legal
     *   choices
     * @return the index in the options-array of the item that was selected
     */
    protected int getSelection(String title, String prompt,
                                String[] options) {
        // approximate the total button size, in order to determine which
        // style dialog box to use
        int maxLen = 0;
        for (int i = 0; i < options.length; i++) {
            maxLen = Math.max(maxLen, options[i].length());
        }
        
        if (maxLen*options.length <= 75) {
            // create the dialog box with the appropriate buttons; return
            // index of selection that user made
            return JOptionPane.showOptionDialog(null,
                    title,
                    prompt,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
        }
        else {
            // create menu-style dialog box; return index of selection that
            // user made
            Object obj =
                JOptionPane.showInputDialog(null,
                    title,
                    prompt,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);
                    
            // if user pressed the "Cancel" button, return the index of
            // the options-array's last element
            if (obj == null) return options.length-1;
            
            // return the index of the user's selection
            for (int i = 0; i < options.length; i++) {
                if (obj.equals(options[i])) return i;
            }
            
            // if somehow we don't have a match, return the index of
            // the options-array's last element
            return options.length-1;
        }
    }
                                
    /**
     * Reads a string specified by the user.  The subclass must provide
     * an implementation of this method.  For example, a GUI-based subclass
     * might allow the user to type the input into a dialog box, where a
     * command-line subclass might read the next argument from the command
     * line.
     * 
     * @param prompt a possible prompt string for prompting the user
     * @return the string that the user specified
     */    
    protected String promptAndReadString(String prompt) {
        // prompt user and get string froma a dialog box
        return JOptionPane.showInputDialog(prompt);
    }
 
    /**
     * Notify the user that an error has occured.
     *
     * @param message the text to display to the user
     */
    protected void errorMessage(String message) {
        // pop up a dialog box with the message
        JOptionPane.showMessageDialog(null, message,
            "Please confirm",JOptionPane.ERROR_MESSAGE);
    }
}
    
/**
 * Command-line-based driver engine.  Allows user to specify the selections
 * on the command line.
 */
class CmdLineDriverEngine extends DriverEngine
{
    // array of command-line arguments specified by the user
    private String[] argArray;
    
    // number of arguments that we've already processed
    private int argsUsed;
    
    /**
     * Constructor for GUIDriverEngine class
     * 
     * @param gd the game-specific game-driver object
     * @param args the command-line arguments
     */
    public CmdLineDriverEngine(GameDriver gd, String[] args) {
    
        // perform superclass initialization
        super(gd);
        
        // keep a copy of the array of command-line arguments in the object
        argArray = (String[])(args.clone());
        
        // begin counter at 0, indicating that we have not yet processed
        // any of the arguments
        argsUsed = 0;
    }
   
    /**
     * Gets a selection, based on user input, from a list of choices.
     * Because this is a command-line-based driver, we to this by finding
     * a match for the next command-line argument from the list of choices,
     * and returning the index in the array that corresponds to that match.<P>
     * 
     * If command-line string does not match a valid selection, or if there
     * are no more command-line strings, then an error message is produced
     * and the program is terminted by performing a System.exit(1).
     * 
     * @param title a possible title for a dialog box (ignored)
     * @param prompt a possible prompt-string for prompting the user (ignored)
     * @param options an array of string containing the list of legal
     *   choices
     * @return the index in the options-array of the item that was selected
     */
    protected int getSelection(String title, String prompt,
                                String[] options) {
                                
        // if we've run out of arguments, print a message and terminate
        // the program
        if (argsUsed >= argArray.length) {
            errorMessage("Not enough arguments");
            System.exit(1);
        }
        
        // get the next argument from the command line; bump the counter
        // to indicate that one more has been used
        String currentArg = argArray[argsUsed];
        argsUsed++;
        
        // find the position the options-array of a string that matches
        // the string
        int rtnVal = findMatch(currentArg, options);

        if (rtnVal < 0) { // no match found
            
            // print error message that tells the legal
            String msg =
                (rtnVal == -1 ? "Illegal" : "Ambiguous") +
                    " selection: " + currentArg + "\nOptions are:\n";
            for (int i = 0; i < options.length; i++) {
                msg += "  "+options[i]+"\n";
            }
            errorMessage(msg);
            
            // terminate the program
            System.exit(1);            
        }
        
        // if we get here, a match was found: return its index in the array
        return rtnVal;
    }
                                
    /**
     * Reads a string specified by the user.  Because this is a command-line-
     * based class, we just return the string in the next command-line
     * argument.
     * 
     * @param prompt a possible prompt string for prompting the user (ignored)
     * @return the string that the user specified.  Returns the empty string
     *   if there are no more arguments.
     */
    protected String promptAndReadString(String prompt) {
    
        if (argsUsed >= argArray.length) {
            // no more arguments: return the empty string
            return "";
        }
        else {
            // argument available: bump counter and return it
            String currentArg = argArray[argsUsed];
            argsUsed++;
            return currentArg;
        }
    }
 
    /**
     * Notify the user that an error has occured.
     *
     * @param message the text to display to the user
     */
    protected void errorMessage(String message) {
        // print message to System.out
        System.out.println(message);
    }
    
    /**
     * Find a unique index in an array of String that matches a given key.
     * Rules for matching are:<UL>
     *   <LI>If there is an exact match, take it; disregard the rest of
     *       the rules.
     *   <LI>If there is a unique string in the array whose first letter
     *       matches that of the key, and whose subseqent letters are an
     *       ordered superset of the key, take it.
     *   <LI>If there is no string in the array that matches the key,
     *       return -1;
     *   <LI>If multiple strings in the array match the key, return -2.
     *   </UL>
     *   Case is ignored in performing all matches.
     * 
     * @param key the string to match
     * @param options the array of string containting the candidates
     * @return the index in the array of the candidate that matches; -1
     *   if there was no match; -2 if the match was not unique.
     */
    protected static int findMatch(String key, String[] options) {
    
        // if the key is the empty string, return: no match
        if (key.length() == 0) return -1;
        
        // initialize: position of last match found is "none" (-1); number of
        // matches found is 0
        int lastMatch = -1;
        int numMatches = 0;
        
        // convert key to lower-case for matching
        String keyLow = key.toLowerCase();
        
        // search through the option-array to find a match.  If we find an
        // exact match, return immediately.  Otherwise, keep track of the
        // number of matches found and the last match found.  The idea is
        // that if there is only one match found, then it will be the one
        // we return
        //
        for (int i = 0; i < options.length; i++) {
        
            // get the i'th option-string, converted to lower-case for matching
            String optLow = options[i].toLowerCase();
            
            if (optLow.length() == 0) {
                // empty option-string: ignore
            }
            else if (keyLow.equals(optLow)) {
                // exact match: return index immediately
                return i;
            }
            else if (keyLow.charAt(0) == optLow.charAt(0)) {
                // not exact match, but both start with same letter:
                // determine if its a subsequence-match
                
                // current position in option-string
                int start = 1;
                
                // have we determined it's a mismatch?  (not yet)
                boolean mismatch = false;
                
                // loop through every character in the key-string, finding
                // a match in the option-string, beginning at the previous
                // place we left off in the option-string.  If we get all
                // the way through, then we've matched each key-string
                // characters--in this case we have a match.
                for (int k = 1; k < keyLow.length(); k++) {
                
                    // find position of current key-string char in option-
                    // string
                    int idx = optLow.indexOf(""+keyLow.charAt(k), start);
                    
                    if (idx == -1) {
                        // failed to find match; mark as mismatch and quit loop
                        mismatch = true;
                        break;
                    }
                    else {
                        // found match.  On next iteration, start looking one
                        // position beyond where we found the match.
                        start = idx + 1;
                    }
                }
                
                // At this point 'mismatch' tells us whether we have a match
                
                // if we have a match, mark this position as being the last
                // match found, and increment the number of matches found
                if (!mismatch) {
                    lastMatch = i;
                    numMatches++;
                }
            }
        }
        
        // if we have more than one match, return -2 (i.e. "ambiguous").
        // Otherwise, return the last match found (which will be -1 if
        // no match was found).
        return numMatches <= 1 ? lastMatch : -2;
    }
}
  