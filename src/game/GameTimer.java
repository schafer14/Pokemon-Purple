package game;

/**
 * A GameTimer is an object that sends a GameTimerAction to a Game at
 * regular intervals.  It can be used, for example, in games where players
 * have deadlines for performing certain actions (e.g., chess clock,
 * 24-second clock in a basketball simultation).<P>
 * 
 * If a Game creates and starts a GameTimer is created as follows:
 * <PRE>
 *   // create timer to go off every 100 milliseconds
 *   GameTimer t = new GameTimer(this, 100);
 *   // start the game timer
 *   t.start();
 * </PRE>
 * then the Game will receive a GameTimerAction every 100 milliseconds, and
 * can perform whatever time-related operations it wishes to perform.
 * 
 * @author Steven R. Vegdahl
 * @version 24 July 2002
 */
public class GameTimer
{
    // the game to send actions to
    private Game game;
    
    // the number of ticks since timer was created (or last reset)
    private int ticks;
    
    // the interval, in milliseconds, between timer events
    private int interval;
    
    // the thread that is running, which causes the "ticks" to occur.  If
    // this variable is null, it means that the timer is stopped
    private Thread thread;
    
    // this object, set here so that the (inner-class) Timer object can
    // refer to it from its code
    private GameTimer self;
    
    /**
     * Constructor for objects of class GameTimer
     * 
     * @param g the game to which GameTimerActions should be sent
     * @param interval the number of milliseconds between successive
     *  GameTimerAction sends
     */
    public GameTimer(Game g, int interval)
    {
        this.game = g; // initialize game
        this.interval = Math.max(0,interval); // set interval
        ticks = 0; // start #ticks at zero
        thread = null; // indicates thread not running   
        self = this; // set 'this' for inner class 
    }

    /**
     * Starts the timer.  Has no effect if the timer is already running.
     * 
     */
    public void start() {
        // synchronize to ensure null test and thread-start are "atomic"
        synchronized(this) {
            if (thread == null) {
                // if thread is not null create new timer object/thread
                // and start it
                Timer timer = new Timer();
                thread = new Thread(timer);
                thread.start();
            }
        }
    }

    /**
     * Stops the timer.
     * 
     */
    public void stop() {
        // set the 'thread' instance-variable to null.  The next time the
        // thread tests this variable, it will terminate.
        thread = null;
    }
    
    /**
     * The number of GameTimerActions sent to the game since the timer
     * was started, or since the last reset was performed on this GameTimer.
     * 
     * @return if a reset had been performed, the number of ticks since the
     *  most recent reset; otherwise since the object was created.
     */
    public int getTicks() {
        // return # ticks
        return ticks;
    }
     
    /**
     * Resets the timer's tick-counter to zero.
     */
    public void reset() {
        // reset tick-counter to zero
        ticks = 0;
    }

    /**
     * Timer: A private inner-class that runs the timer-thread.
     */
    private class Timer implements Runnable {
    
        /**
         * Timer constructor
         */
        public Timer() {
        }
        
        /**
         * The code that runs in the separate thread.
         */
        public void run() {

            // create a timer action which will be repeatedly sent to the game
            GameTimerAction action = new GameTimerAction(self);
            
            // as long as we've not been marked as "stopped", apply the
            // timer action to the games at the appropriate time-intervals
            //
            while (thread != null)
                try {
                    Thread.sleep(interval); // wait for approriate interval
                    ticks++; // increment # ticks
                    game.applyAction(action); // apply action to game
                }
                catch (InterruptedException ix) {
                    // this should never happen, but if it does, loop back
                }
            }
        }
    }

