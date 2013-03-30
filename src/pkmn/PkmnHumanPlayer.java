package pkmn;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.Vector;
import javax.imageio.*;
import javax.swing.*;

import game.*;

/**
 * This is a generic Human player.  Because humans interact with the
 * game via a GUI, this class is a JFrame class.
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 */
public class PkmnHumanPlayer extends GameHumanPlayer implements PkmnPlayer, MouseMotionListener
{
	/**
	 * Instance variables
	 */
	private static final long serialVersionUID = 1L;
	//a stored representation of the current state
	private PkmnState st = null;
	//the buttons used for the selection screen
	private Vector<JButton> selectButtons;
	//a reference to this player
	private GamePlayer thisPlayer;

	//Booleans that control when to show
	// move descriptions
	private boolean[] showMoveDescription = new boolean[4];

	//The panel that the GUI is drawn on.
	private JPanel selectPanel;

	//ticks through the animations
	private Thread ticker;
	
	//the current frames to be used for each players pokemon
	private int playerFrame;
	private int opponentFrame;

	//Images for GUI
	private Image background;
	private Image move;
	private Image moveDescription;
	private Image neutral;
	private Image opponentHP;
	private Image physical;
	private Image special;
	private Image userHP;
	private Image platform;

	/**
	 * Constructor for PkmnHuman Player
	 */
	public PkmnHumanPlayer()
	{
		super();
		thisPlayer = this;
		playerFrame=0;
		opponentFrame=0;

		//Upload the background images
		String nonChar = "images/nonCharImages/";

		try 
		{
			background = ImageIO.read(new File(nonChar + "background.png"));
			move = ImageIO.read(new File(nonChar + "move.png"));
			moveDescription = ImageIO.read(new File(nonChar + "moveDescription.png"));
			neutral = ImageIO.read(new File(nonChar + "neutral.png"));
			opponentHP = ImageIO.read(new File(nonChar + "opponentHP.png"));
			physical = ImageIO.read(new File(nonChar + "physical.png"));
			special = ImageIO.read(new File(nonChar + "special.png"));
			userHP = ImageIO.read(new File(nonChar + "userHP.png"));
			platform = ImageIO.read(new File(nonChar + "platform.png"));
		} 
		catch (IOException e) {
			System.err.println("FAIL WHALE");
		}

		// set the background color
		this.getContentPane().setBackground(Color.BLACK);

		//Set the initial move description booleans
		showMoveDescription[0] = false;
		showMoveDescription[1] = false;
		showMoveDescription[2] = false;
		showMoveDescription[3] = false;

		//Add the MouseMotionListener, and mouse listener
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

		//Used for double buffering
		this.setVisible(true);
		this.createBufferStrategy(2);

		//frame ticker
		ticker= new Thread(new ButtonTicker());
		ticker.start();
		
		//Don't allow the player to change the size of the window
		this.setResizable(false);
	}

	/**
     * Notifies a player that the game is over.  Typically called by the
     * game object when the game has been terminated.
     */
    public void gameIsOver() {
    	int winnerID= st.gameWinner();
    	
        // display dialog box to user wait for response
    	if (winnerID>=0)
        infoMessage(st.getCurrPokemon(winnerID).toString() +" has won the game");

    	super.gameIsOver();
    }
	
	/**
	 * returns the GUI's title-window
	 *
	 * @return the title to be put in the GUI's window
	 */
	protected String defaultTitle()
	{
		return "Pokemon Purple";
	}

	/**
	 * The GUI's initial height.
	 *
	 * @return the GUI's inital height, in pixels.
	 */
	protected int initialHeight() { return 650; }

	/**
	 * The GUI's initial width.
	 *
	 * @return the GUI's inital width, in pixels.
	 */
	protected int initialWidth() { return 1000; }

	/**
	 * creates the application-specific GUI component
	 *
	 * @return the application-specific component of the GUI
	 */
	protected Component createApplComponent() 
	{
		//size of each button
		int Bsize = 100;

		//if there is no panel make a new panel
		if(selectPanel == null) selectPanel = new JPanel(true);
		selectPanel.paint(getGraphics());
		selectPanel.setSize(initialWidth(),initialHeight());
		selectPanel.setPreferredSize(selectPanel.getSize());
		selectPanel.setOpaque(false);

		int numRows =((int)Math.sqrt(PkmnPokemon.values().length))+2;
		int numCols =((int)Math.sqrt(PkmnPokemon.values().length));
		JPanel ButtonPanel = new JPanel(true);
		ButtonPanel.setSize(numRows*(Bsize+10),numCols*(Bsize+10));
		ButtonPanel.setPreferredSize(ButtonPanel.getSize());
		ButtonPanel.setMaximumSize(ButtonPanel.getSize());
		ButtonPanel.setMinimumSize(ButtonPanel.getSize());
		ButtonPanel.setOpaque(false);

		//and a button listener for each button
		SelectionButtonListener sbl = new SelectionButtonListener();

		selectButtons = new Vector<JButton>();

		//iterates through the Pokemon enum
		for(PkmnPokemon pkmn : PkmnPokemon.values()) 
		{
			//create button for each of the pokemon 
			Icon PkmnIcon = new ImageIcon(pkmn.animationFrame.get(0));
			JButton myButton = new JButton (PkmnIcon);
			myButton.setName(pkmn.name());
			myButton.setActionCommand(pkmn.name());
			myButton.addActionListener(sbl);
			myButton.setSize(Bsize, Bsize);
			myButton.setPreferredSize(myButton.getSize());
			myButton.setOpaque(false);
			myButton.setVisible(true);
			myButton.setBackground(new Color(0,0,0,0));
			ButtonPanel.add(myButton);
			selectButtons.add(myButton);
		}

		selectPanel.add(ButtonPanel);

		return selectPanel;
	}

	/**
	 * An inner class that listens to clicks on the selection buttons
	 */

	public class SelectionButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent ae) 
		{
			String pokemonName = ae.getActionCommand();
			//find pokemon in out of entire pokemon vector 
			for(PkmnPokemon pkmn : PkmnPokemon.values()) 
			{
				if(pokemonName.equals(pkmn.name()))
				{
					game.applyAction(new PkmnSelectMoveAction(thisPlayer, pkmn));
				}
			}
		}  	
	}

	/**
	 * An inner class that ticks the animation frames 
	 */
	class ButtonTicker implements Runnable {

		@Override
		public void run() {
			boolean keepTicking=true;
			while(keepTicking){
				if(st !=null)
					if (!st.isPkmnSelect())tickFrame();
				try {
					Thread.sleep(150);
				} catch (InterruptedException e){}
			}
		}

	}

	/**
	 * Paints the images for the GUI
	 * 
	 * @param g the Graphics object
	 */
	public void paint(Graphics g)
	{	
		BufferStrategy bs = this.getBufferStrategy();

		if (bs != null)
			g = bs.getDrawGraphics();
		
		//Both states have the same background image.
		g.drawImage(background, 0, 0, this);
		
		//if state hasn't been update assume in select state and pant string
		if(st == null){
		//Set the font and color.
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
		g.setColor(Color.WHITE);
		g.drawString("Choose a Pokemon to battle with!", 200, 610);
		}

		//Paint different GUIs based on which state it is.
		if(st != null)
		{
			if(st.isPkmnSelect())
			{
				paintSelectState(g);
				//Set the font and color.
				g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
				g.setColor(Color.WHITE);
				g.drawString("Choose a Pokemon to battle with!", 200, 610);
			}
			else if (!st.isPkmnSelect())
			{
				paintFightState(g);
			}
		}

		g.dispose();

		if (bs != null)
			bs.show();
		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Paints the pokemon fight
	 * 
	 * @param g the Graphics object
	 */
	public void paintFightState(Graphics g)
	{
		int opponentID;
		if (getId() == 1)
			opponentID = 0;
		else
			opponentID = 1;
		
		//Paint the other background images
		g.drawImage(userHP, 10, 30, this);
		g.drawImage(opponentHP, 540, 30, this);
		g.drawImage(move, 10, 440, this);
		g.drawImage(move, 10, 540, this);
		g.drawImage(move, 210, 440, this);
		g.drawImage(move, 210, 540, this);
		g.drawImage(moveDescription, 440, 440, this);
		g.drawImage(platform, 180, 350, platform.getWidth(this)*2, platform.getHeight(this)*2, this);
		g.drawImage(platform, 580, 350, platform.getWidth(this)*2, platform.getHeight(this)*2, this);

		//Get the opponent's Pokemon
		Image opponentPkmn = st.getCurrPokemon(opponentID).getAnimationFrames().elementAt(opponentFrame);

		//Get the player's Pokemon
		Image playerPkmn= st.getCurrPokemon(getId()).getAnimationFrames().elementAt(playerFrame);

		//Paint the two Pokemon that were chosen
		drawBattlePokemon(g, opponentPkmn, playerPkmn);

		//Draw most of what has to do with text
		drawText(g);

		//Paint the health bars.
		paintHealthBars(g);
		
		//Boolean for determining if any or none
		// of the move descriptions should show.
		boolean showDescription = false;

		//If one of the move descriptions is supposed to show
		// , draw it on the screen.
		//Otherwise show other information for the user.
		for (int i = 0; i < showMoveDescription.length; i++)
		{
			if (showMoveDescription[i] == true)
			{
				showDescription = true;
				drawMoveDescription(g, st.getCurrPokemon(getId()).moveset[i]);
			}
		}
		
		if (!showDescription)
		{
			//Set the font used
			g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));

			//Display who's attack(s) missed.
			if(st.getMissedAttack()[getId()] 
			  && st.getMissedAttack()[opponentID])
			{
				g.drawString("Both attacks missed!", 500, 525);
			}
			else if(st.getMissedAttack()[getId()])
			{
				g.drawString("Player attack missed!", 500, 525);
			}
			else if(st.getMissedAttack()[opponentID])
			{
				g.drawString("Opponent attack missed!", 500, 525);
			}
		}
	}

	/**
	 * Handles painting the health bars of the Pokemon
	 * @param g
	 */
	public void paintHealthBars(Graphics g)
	{
		//Integers used for calculating how much
		// of the rectangle to paint.
		//Player's health
		double maxHP = st.getCurrPokemon(getId()).hp;
		double currentHP = st.getCurrHP(getId());
		double maxLength = 280;

		double percent = currentHP/maxHP;
		int length = (int) (maxLength*percent);

		g.setColor(Color.GREEN);

		//Paint the player's health.
		g.fillRoundRect(150, 113, length, 10, 3, 3);

		//Opponent's health
		int opponentId;
		if (getId() == 0)
			opponentId = 1;
		else
			opponentId = 0;

		maxHP = st.getCurrPokemon(opponentId).hp;
		currentHP = st.getCurrHP(opponentId);

		percent = currentHP/maxHP;
		length = (int)(maxLength*percent);

		//Paint the opponent's health.
		g.fillRoundRect(610, 113, length, 10, 3, 3);

		//Reset the color
		g.setColor(Color.WHITE);
	}

	/**
	 * Draws the move description of the move in the parameter.
	 * 
	 * @param g the Graphics object
	 * @param move the Pokemon move to show the description
	 */
	public void drawMoveDescription(Graphics g, PkmnMove move)
	{
		//Set the font used
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));

		//Draw the move name
		g.drawString(move.getName(), 500, 525);

		//Draw the PP of the move
		g.drawString("PP", 500, 580);
		g.drawString(Integer.toString(st.getCurrMovePP(getId(), move)), 560, 580);
		g.drawString("/", 610, 580);
		g.drawString(Integer.toString(move.getPP()), 630, 580);

		//Draw the images associated with the move type
		Image typeImage = move.getType().getImage();
		g.drawImage(typeImage, 830, 490, typeImage.getWidth(this)*3, typeImage.getHeight(this)*3, this);

		//Draw what the base of the move is.
		switch(move.getBase())
		{
		case 0:
			g.drawImage(physical, 830, 540, physical.getWidth(this)*3, physical.getHeight(this)*3, this);
			break;
		case 1:
			g.drawImage(special, 830, 540, special.getWidth(this)*3, special.getHeight(this)*3, this);
			break;
		case 2:
			g.drawImage(neutral, 830, 540, neutral.getWidth(this)*3, neutral.getHeight(this)*3, this);
			break;
		}
	}

	/**
	 * Handles showing the player any text
	 * in the GUI.
	 * 
	 * @param g the Graphics object
	 */
	public void drawText(Graphics g)
	{
		//Set the font for the moves
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 25));

		PkmnMove[] moves = st.getCurrPokemon(getId()).moveset;

		//Draw the four moves
		g.setColor(Color.WHITE);
		g.drawString(moves[0].getName(), 30, 500);
		g.drawString(moves[1].getName(), 230, 500);
		g.drawString(moves[2].getName(), 30, 600);
		g.drawString(moves[3].getName(), 230, 600);

		//Set the font for the names
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));

		//Draw player's Pokemon's name
		g.drawString(st.getCurrPokemon(getId()).toString(), 100, 100);

		//Draw opponent's Pokemon's name
		if (getId() == 1)
			g.drawString(st.getCurrPokemon(0).toString(), 560, 100);
		else
			g.drawString(st.getCurrPokemon(1).toString(), 560, 100);

		//Set the font for the health numbers
		g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));

		//Draw the player's health (the numbers)
		//Max health
		g.drawString(Integer.toString(st.getCurrPokemon(getId()).hp), 350, 160);

		//Current health
		g.drawString(Integer.toString(st.getCurrHP(getId())), 260, 160);
	}

	/**
	 * Paints the two Pokemon on the battle screen
	 * 
	 * @param g the Graphics object
	 */
	public void drawBattlePokemon(Graphics g, Image opponentPkmn, Image playerPkmn)
	{	
		int imageWidth = opponentPkmn.getWidth(this);
		int imageHeight = opponentPkmn.getHeight(this);

		//Drawing the opponent's Pokemon
		g.drawImage(opponentPkmn, 704-imageWidth, 370-(imageHeight*3)/2, imageWidth*2, imageHeight*2, this);

		//Tutorial site used:
		//http://zetcode.com/tutorials/java2dtutorial/java2dimages/
		Graphics2D g2d = (Graphics2D)g;

		//Create a BufferedImage with a transparent background
		BufferedImage bI =  new BufferedImage(playerPkmn.getWidth(null), 
				playerPkmn.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		Graphics gb = bI.getGraphics();
		gb.drawImage(playerPkmn, 0, 0, null);
		gb.dispose();

		//Horizontal flip the BufferedImage
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-playerPkmn.getWidth(this), 0);
		AffineTransformOp op = new AffineTransformOp(tx, 
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bI = op.filter(bI, null);

		//Draw the player's Pokemon
		g2d.drawImage(bI, 304-bI.getWidth(), 370-(bI.getHeight()*3)/2, bI.getWidth()*2, bI.getHeight()*2, this);
	}

	/**
	 * increments the frames each time a tick frame is called
	 * 
	 */
	public void tickFrame(){
		playerFrame++;
		opponentFrame++;
		//Get the number of frames of player's Pokemon
		int playerNumFrames= st.getCurrPokemon(getId()).getAnimationFrames().size();
		if(playerFrame>=playerNumFrames){
			playerFrame=0;
		}

		//Get the number of frames of opponent's Pokemon
		int opponentNumFrames;
		if (getId() == 1)
			opponentNumFrames = st.getCurrPokemon(0).getAnimationFrames().size();
		else
			opponentNumFrames = st.getCurrPokemon(1).getAnimationFrames().size();
		if(opponentFrame>=opponentNumFrames){
			opponentFrame=0;
		}
		this.repaint();
	}

	/**
	 * Paints the pokemon selection 
	 * 
	 * @param g the Graphics object
	 */
	public void paintSelectState(Graphics g)
	{
		selectPanel.repaint(60, 0, 860, 540);
	}

	/**
	 * Notifies a player that the state of the game has changed.  Typically
	 * called by the game object when an opponent has made a move, and the
	 * player may want to take the move into account (e.g., to display it
	 * on the screen).
	 */
	public void stateChanged() 
	{
		try 
		{
			// ask the game for the updated state
			st = (PkmnState)game.getState(this, 0);
		}finally{}

		//if you are no longer in the select stage than the selectPane is no longer needed
		if (st!=null){
			if (!st.isPkmnSelect()){
				selectPanel = null;
				for(JButton thisButton:selectButtons){
					thisButton.setEnabled(false);
					thisButton.setVisible(false);
				}
			}
		}
		//repaint after all updates have been made
		this.repaint();
	}

	/**
	 * Listening for when the mouse is dragged.
	 * Currently does nothing.
	 */
	public void mouseDragged(MouseEvent me) {}

	/**
	 * Listening for when the mouse is moved.
	 * Displays information about a move
	 * when the use hovers over it.
	 */
	public void mouseMoved(MouseEvent me) 
	{
		//If the user is on the battle screen
		try
		{
			if (!st.isPkmnSelect())
			{
				//If the user is hovering over one of the moves,
				// show the corresponding move description.
				if (me.getX() > 10 && me.getY() > 440 && me.getX() < 210 && me.getY() < 540)
				{
					showMoveDescription[0] = true;
					showMoveDescription[1] = false;
					showMoveDescription[2] = false;
					showMoveDescription[3] = false;
				}
				else if (me.getX() > 10 && me.getY() > 540 && me.getX() < 210 && me.getY() < 640)
				{
					showMoveDescription[0] = false;
					showMoveDescription[1] = false;
					showMoveDescription[2] = true;
					showMoveDescription[3] = false;
				}
				else if (me.getX() > 210 && me.getY() > 440 && me.getX() < 410 && me.getY() < 540)
				{
					showMoveDescription[0] = false;
					showMoveDescription[1] = true;
					showMoveDescription[2] = false;
					showMoveDescription[3] = false;
				}
				else if (me.getX() > 210 && me.getY() > 540 && me.getX() < 410 && me.getY() < 640)
				{
					showMoveDescription[0] = false;
					showMoveDescription[1] = false;
					showMoveDescription[2] = false;
					showMoveDescription[3] = true;
				}
				else
				{
					showMoveDescription[0] = false;
					showMoveDescription[1] = false;
					showMoveDescription[2] = false;
					showMoveDescription[3] = false;
				}
			}
		}
		//Making sure the program isn't upset about 
		// the mouse being on the screen before
		// the state has been created.
		catch (NullPointerException npe){}
	}

	/**
	 * Invoked when the mouse has been clicked on a component.
	 *
	 * @param me  the event object
	 */
	public void mouseClicked(MouseEvent me) {
		//If the user is on the battle screen
		try
		{
			if (!st.isPkmnSelect())
			{
				PkmnMove selectedMove=null;
				//If the user clicks on one of the moves,
				// choose action corresponding to given move.
				if (me.getX() > 10 && me.getY() > 440 && me.getX() < 210 && me.getY() < 540){
					selectedMove = st.getCurrPokemon(getId()).moveset[0];
				}
				else if (me.getX() > 10 && me.getY() > 540 && me.getX() < 210 && me.getY() < 640){
					selectedMove = st.getCurrPokemon(getId()).moveset[2];
				}
				else if (me.getX() > 210 && me.getY() > 440 && me.getX() < 410 && me.getY() < 540){
					selectedMove = st.getCurrPokemon(getId()).moveset[1];

				}
				else if (me.getX() > 210 && me.getY() > 540 && me.getX() < 410 && me.getY() < 640){
					selectedMove = st.getCurrPokemon(getId()).moveset[3];
				}
				//if a move was chosen then apply it
				if(selectedMove != null){
					game.applyAction(new PkmnAttackMoveAction(thisPlayer, selectedMove));
				}
			}
		}
		//Making sure the program isn't upset about 
		// the mouse being on the screen before
		// the state has been created.
		catch (NullPointerException npe){}
	}

}
