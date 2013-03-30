package game;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * An object that communicates over the network with another object.
 * This abstract class implements the common behavior for its two primary
 * subclasses, ProxyGame and ProxyPlayer.
 * 
 * @author Steven R. Vegdahl
 * @version 23 July 2002
 */
public abstract class ProxyObject {

    // the input and output stream objects via which information is
    // received and sent over the network
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Constructor for a Proxy Object
     *
     */
    public ProxyObject() {
        // set the input and output streams to null
        in = null;
        out = null;
    }

    /**
     * Finish initialization.  Subclasses may put behavior here that they
     * want to execute after the socket-connection has been established.
     *
     */
    protected void finishInit() {
    }

    /**
     * Tells whether the object is completely built, including any
     * post-constructor initialization and network connection.
     *
     */
    public boolean isComplete() {
        return in != null && out != null;
    }

    /**
     * reads an input line from the network socket
     *
     * @return a line of text that is the next one sent by the process
     *  at the other end of the network socket.
     */
    protected String readInputLine() {

        // if there is no input stream, return an empty string
        if (in == null) return "";

        // read and return the input line; if an error occurs, return
        // the empty string
        try {
            String input = in.readLine();
            return input;
        }
        catch (IOException iox) {
            return "";
        }
    }

    /**
     * writes a string to the network socket
     *
     * @param str the string to send
     */
    protected void writeOutputLine(String str) {
        if (out == null) return;
        out.println(str);
    }

    /**
     * The default port number on the server machine that will be used
     * in making the connection.  Hopefully, each game will have a different
     * number so that we don't have games confusing each other.
     * (If we wanted to do this "right", we'd have reserve one with the
     * system administrator.)  For now, each game should select a somewhat
     * random integer.  FOR A GIVEN GAME, THE ProxyGame AND ProxyPlayer
     * CLASSES SHOULD RETURN THE SAME VALUE.
     */
    protected abstract int getAdmPortNum();

    /**
     * Connect to a process as the server.  This entails "camping" on a
     * port number and waiting for a client to request service.
     */
    protected void connectAsServer() {

        // get the default port number
        int firstPort = getAdmPortNum();

        // eventually, we'll store the socket object here
        ServerSocket theServerSocket = null;

        // create server socket.  Begin at the default port number, but
        // increment/iterate through a total of 50 ports in case multiple
        // players are trying to make a connection.
        for (int delta = 0; delta < 50; delta++) {
            // attempt to find an unused port; if successful, break out of
            // loop; otherwise continue looping
            try {
                theServerSocket = new ServerSocket(firstPort+delta);
                break;
            }
            catch (IOException iox) {
            }
        }

        // If we've found a port, wait for a client to connect and set up
        // the streams
        if (theServerSocket != null) {
            try {

                // wait for client connnection
                Socket theSocket = theServerSocket.accept();

                // set up the input and output streams
                in =
                    new BufferedReader(
                        new InputStreamReader(theSocket.getInputStream()));
                out =
                    new PrintWriter(
                        new OutputStreamWriter(theSocket.getOutputStream()),
                        true);

                // close the socket server
                theServerSocket.close();   

                // perform any user-initialization 
                finishInit();
            }
            catch (IOException x) {
                // if we run into a problem, set the input and output
                // streams to null, indicating that the object is not
                // set up
                in = null;
                out = null;
            }
        }
    }

    /**
     * Connect to a process as a client.  This entails requesting a
     * connection from a server and waiting for it to be established.
     */
    protected void connectAsClient(String hostName) {

        // initially, set the input and output streams to null
        in = null;
        out = null;

        // determine the first port number to try for this game
        int firstPort = getAdmPortNum();

        // loop through 50 ports, beginning at the first one; try to
        // connect to a servert at each.  When we find one, set up
        // the streams
        for(int delta = 0; delta < 50; delta++) {
            try {

                // attempt to connect to the server
                Socket theSocket = new Socket(hostName, firstPort+delta);

                // If we get here, we have successfully made a connection

                // set up the input and output streams
                in =
                    new BufferedReader(
                            new InputStreamReader(
                                    theSocket.getInputStream()));
                out =
                    new PrintWriter(
                            new OutputStreamWriter(
                                    theSocket.getOutputStream()),
                            true);

                // perform user-definied initialization
                finishInit();

                // break out of loop, since we succeeded                       
                break;
            }
            catch (Exception x) {
                // if we get here, we failed to make a connect, for the
                // given port number
            }
        }
    }

    /**
     * Extracts the "command name" portion of a string.  For example,
     * if the string is "  funny  3 55 8 -2", then the string "funny" will
     * be returned.  It is expected that the method will be used to decode
     * messages between ProxyGame and ProxyPlayer objects that are sent
     * over the network.
     *
     * @param str the string to parse
     * @return the command--which is the first (blank-delimited) word
     *   of the string
     */
    protected static String parseCommand(String str) {
        String str2 = str.trim()+" "; // trim, pad with dummy-blank
        int idx = str2.indexOf(" "); // find first blank
        return str2.substring(0,idx); // return string, up to first blank
    }

    /**
     * Extracts the "numeric arguments" portion of a string.  For example,
     * if the string is "  funny  3 55 8 -2", then value returned will be
     * a 4-element array of int containing the values 3, 55, 8 and -2,
     * respectively.  It is expected that the method will be used to decode
     * messages between ProxyGame and ProxyPlayer objects that are sent
     * over the network.
     *
     * @param str the string to parse
     * @return the numeric arguments--which are extracted from each (blank-
     *  separated) word except the first.  Argument parsing is aborted early
     *  if a non-integer is encountered.
     */
    protected static int[] parseArgs(String str) {

        // extract all characters after the first blank
        String str2 = str.trim()+" "; // trim, pad with dummy-blank
        int idx = str2.indexOf(" "); // find first blank
        str2 = str2.substring(idx+1); // all characters after first blank

        // create a vector to hold temporarily hold the elements
        Vector v = new Vector();

        // find each word; parse each as an integer an put into the vector
        try {
            // set position in string we're currently at
            idx = 0;

            // loop through, parsing each word
            for (;;) {
                int next = str2.indexOf(" ", idx); // next position
                if (next < 0) break; // exit loop of no more blanks
                v.addElement( // parse integer; add to vector
                    new Integer(Integer.parseInt(str2.substring(idx,next))));
                idx = next+1; // update position to look next
            }
        }
        catch (NumberFormatException nfx) {
            // if we attempt to parse a non-integer, well drop out of the
            // loop by coming here
        }

        // At this point, the vector contains all the numbers we want.  All
        // we need do now is to convert it to an array of int.

        // create appropriately sized int-array
        int[] rtnVal = new int[v.size()];

        // copy each element from the vector into the array
        for (int i = 0; i < rtnVal.length; i++) {
            rtnVal[i] = ((Integer)v.elementAt(i)).intValue();
        }

        // return the array
        return rtnVal;        
    }
}
