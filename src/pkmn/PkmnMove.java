package pkmn;

/**
 * The PkmnMove enumeration.
 * Contains all of the possible attacks a Pokemon can have.
 * 
 * @author Pokemon Purple Team
 * @version 8 December 2011
 *
 */
public enum PkmnMove {

	// { Name, Type, Base, Power, Accuracy, PP, Priority, Op Code }
	// Base key:
	// Physical = 0
	// Special = 1
	// Neutral = 2
	
	/**
	 * Op Code Key:
	 * 
	 * 0: Nothing other than damage.
	 * 	  (Most moves)
	 * 1: User gains 1/2 of max HP (no higher than max HP) 
	 *    (Recover, Slack Off)
	 * 2: User gains 1/2 of max HP. Specifically for Flying type. 
	 *    (Roost)
	 * 3: User gains 1/2 of damage done in HP. 
	 *    (Drain Punch, Giga Drain)
	 * 4: Has a higher critical-hit chance than normal.
	 *    (Night Slash, Razor Leaf, Shadow Claw, Stone Edge)
	 */

	AerialAce("Aerial Ace", PkmnType.FLYING, 0, 60, 100, 20, 0, 0),
	AirSlash("Air Slash", PkmnType.FLYING, 1, 75, 95, 20, 0, 0),
	AncientPower("Ancient Power", PkmnType.ROCK, 1, 60, 100, 20, 0, 0),
	AquaJet("Aqua Jet", PkmnType.WATER, 0, 40, 100, 20, 1, 0),
	AquaTail("Aqua Tail", PkmnType.WATER, 0, 90, 90, 10, 0, 0),
	AuraSphere("Aura Sphere", PkmnType.FIGHTING, 1, 90, 100, 30, 0, 0),
	Bite("Bite", PkmnType.DARK, 0, 60, 100, 25, 0, 0),
	Blizzard("Blizzard", PkmnType.ICE, 1, 120, 70, 5, 0, 0),
	BrickBreak("Brick Break", PkmnType.FIGHTING, 0, 75, 100, 15, 0, 0),
	BodySlam("Body Slam", PkmnType.NORMAL, 0, 85, 100, 15, 0, 0),
	BugBite("Bug Bite", PkmnType.BUG, 0, 60, 100, 20, 0, 0),
	CloseCombat("Close Combat", PkmnType.FIGHTING, 0, 120, 100, 5, 0, 0),
	Crunch("Crunch", PkmnType.DARK, 0, 80, 100, 15, 0, 0),
	DarkPulse("Dark Pulse", PkmnType.DARK, 1, 80, 100, 15, 0, 0),
	DoubleEdge("Double Edge", PkmnType.NORMAL, 0, 120, 100, 15, 0, 0),
	DracoMeteor("Draco Meteor", PkmnType.DRAGON, 1, 140, 90, 5, 0, 0),
	DragonClaw("Dragon Claw", PkmnType.DRAGON, 0, 80, 100, 15, 0, 0),
	DragonPulse("Dragon Pulse", PkmnType.DRAGON, 1, 90, 100, 10, 0, 0),
	DrainPunch("Drain Punch", PkmnType.FIGHTING, 0, 60, 100, 5, 0, 3),
	DynamicPunch("Dynamic Punch", PkmnType.FIGHTING, 0, 100, 50, 5, 0, 0),
	Earthquake("Earthquake", PkmnType.GROUND, 0, 100, 100, 10, 0, 0),
	EarthPower("Earth Power", PkmnType.GROUND, 1, 90, 100, 10, 0, 0),
	EnergyBall("Energy Ball", PkmnType.GRASS, 1, 80, 100, 10, 0, 0),
	ExtremeSpeed("ExtremeSpeed", PkmnType.NORMAL, 0, 80, 100, 5, 2, 0),
	FireBlast("Fire Blast", PkmnType.FIRE, 1, 120, 85, 5, 0, 0),
	FirePunch("Fire Punch", PkmnType.FIRE, 0, 75, 100, 15, 0, 0),
	Flamethrower("Flamethrower", PkmnType.FIRE, 1, 95, 100, 15, 0, 0),
	FlareBlitz("Flare Blitz", PkmnType.FIRE, 0, 120, 100, 15, 0, 0),
	FlashCannon("Flash Cannon", PkmnType.STEEL, 1, 80, 100, 10, 0, 0),
	FocusBlast("Focus Blast", PkmnType.FIGHTING, 1, 120, 70, 5, 0, 0),
	GigaDrain("Giga Drain", PkmnType.GRASS, 1, 75, 100, 10, 0, 3),
	GrassKnot("Grass Knot", PkmnType.GRASS, 1, 20, 100, 20, 0, 0),
	HammerArm("Hammer Arm", PkmnType.FIGHTING, 0, 100, 90, 10, 0, 0),
	HeatWave("Heat Wave", PkmnType.FIRE, 1, 100, 90, 10, 0, 0),
	HiddenPower("Hidden Power", PkmnType.VARIABLE, 1, 70, 100, 15, 0, 0),
	HydroPump("Hydro Pump", PkmnType.WATER, 0, 120, 80, 5, 0, 0),
	IceBeam("Ice Beam", PkmnType.ICE, 1, 95, 100, 10, 0, 0), 
	IcePunch("Ice Punch", PkmnType.ICE, 0, 75, 100, 15, 0, 0),
	IronTail("Iron Tail", PkmnType.STEEL, 0, 100, 75, 15, 0, 0),
	LeafStorm("Leaf Storm", PkmnType.GRASS, 1, 140, 90, 5, 0, 0),
	Megahorn("Megahorn", PkmnType.BUG, 0, 120, 85, 10, 0, 0),
	MetalClaw("Metal Claw", PkmnType.STEEL, 0, 50, 95, 35, 0, 0),
	NightSlash("Night Slash", PkmnType.DARK, 0, 70, 100, 15, 0, 4),
	PowerWhip("Power Whip", PkmnType.GRASS, 0, 120, 85, 10, 0, 0),
	Psychic("Psychic", PkmnType.PSYCHIC, 1, 90, 100, 10, 0, 0), 
	Psyshock("Psyshock", PkmnType.PSYCHIC, 1, 80, 100, 10, 0, 0), 
	Psystrike("Psystrike", PkmnType.PSYCHIC, 1, 100, 100, 10, 0, 0), 
	QuickAttack("Quick Attack", PkmnType.NORMAL, 0, 40, 100, 30, 1, 0),
	RapidSpin("Rapid Spin", PkmnType.NORMAL, 0, 20, 100, 40, 0, 0),
	RazorLeaf("Razor Leaf", PkmnType.GRASS, 0, 55, 95, 25, 0, 4), 
	Recover("Recover", PkmnType.NORMAL, 2, 0, 100, 10, 0, 1),
	Return("Return", PkmnType.NORMAL, 0, 85, 100, 20, 0, 0),
	RockSlide("Rock Slide", PkmnType.ROCK, 0, 75, 90, 10, 0, 0),
	Roost("Roost", PkmnType.FLYING, 2, 0, 100, 10, 0, 2),
	Scald("Scald", PkmnType.WATER, 1, 80, 100, 15, 0, 0),
	SeedBomb("Seed Bomb", PkmnType.GRASS, 0, 80, 100, 15, 0, 0),
	ShadowBall("Shadow Ball", PkmnType.GHOST, 1, 80, 100, 15, 0, 0),
	ShadowClaw("Shadow Claw", PkmnType.GHOST, 0, 70, 100, 15, 0, 4),
	ShadowPunch("Shadow Punch", PkmnType.GHOST, 0, 60, 100, 20, 0, 0), 
	SlackOff("Slack Off", PkmnType.NORMAL, 2, 0, 100, 10, 0, 1),
	SludgeBomb("Sludge Bomb", PkmnType.POISON, 1, 90, 100, 10, 0, 0),
	SteelWing("Steel Wing", PkmnType.STEEL, 0, 70, 90, 25, 0, 0),
	StoneEdge("Stone Edge", PkmnType.ROCK, 0, 100, 80, 5, 0, 4),
	Strength("Strength", PkmnType.NORMAL, 0, 80, 100, 15, 0, 0),
	SuckerPunch("Sucker Punch", PkmnType.DARK, 0, 80, 100, 5, 1, 0),
	Superpower("Superpower", PkmnType.FIGHTING, 0, 120, 100, 5, 0, 0), 
	Surf("Surf", PkmnType.WATER, 1, 95, 100, 15, 0, 0),  
	Thunder("Thunder", PkmnType.ELECTRIC, 1, 120, 70, 10, 0, 0),
	Thunderbolt("Thunderbolt", PkmnType.ELECTRIC, 1, 95, 100, 15, 0, 0), 
	ThunderPunch("Thunder Punch", PkmnType.ELECTRIC, 0, 75, 100, 15, 0, 0), 
	VineWhip("Vine Whip", PkmnType.GRASS, 0, 35, 100, 15, 0, 0),
	VoltSwitch("Volt Switch", PkmnType.ELECTRIC, 1, 70, 100, 20, 0, 0),
	Waterfall("Waterfall", PkmnType.WATER, 0, 80, 100, 15, 0, 0),
	WingAttack("Wing Attack", PkmnType.FLYING, 0, 60, 100, 35, 0, 0), 
	XScissor("X-Scissor", PkmnType.BUG, 0, 80, 100, 15, 0, 0);
	
	//Instance variables
	private String name;
	private PkmnType type;
	private int base, power, accuracy, pp, priority;
	private int opCode;
	
	
	/**
	 * Constructor for PkmnMove
	 * 
	 * @param name The title of the move
	 * @param type The move's type (Fire, water, grass, etc.)
	 * @param base Whether the move is Physical, Special, or Neutral.
	 * @param power How much power the move can deliver.
	 * @param accuracy How accurate the move is.
	 * @param pp How many times the move can be performed.
	 * @param priority The priority of the move for which move goes for.
	 * @param opCode int representing other effects moves can have.
	 */
	private PkmnMove(String name, PkmnType type, int base, int power, int accuracy, int pp, int priority, int opCode)
	{
		this.name = name;
		this.type = type;
		this.base = base;
		this.power = power;
		this.accuracy = accuracy;
		this.pp = pp;
		this.priority = priority;
		this.opCode = opCode;
	}

	/**
	 * For moves that can have different types, 
	 * this method sets which type the move has 
	 * for the current game.
	 * 
	 * @param newType The type to be set
	 * @return The move with the set type.
	 */
	public PkmnMove setType(PkmnType newType)
	{
		if (this.type.equals(PkmnType.VARIABLE))
		{
		type = newType;
		}
		return this;
	}
	
	/**
	 * Get the move's name.
	 * 
	 * @return name
	 */
	public String getName() { return name; }
	
	/**
	 * Get the move's type.
	 * 
	 * @return type
	 */
	public PkmnType getType() { return type; }
	
	/**
	 * Get the move's base.
	 * 
	 * @return base
	 */
	public int getBase() { return base; }
	
	/**
	 * Get the move's power.
	 * 
	 * @return power
	 */
	public int getPower() { return power; }
	
	/**
	 * Get the move's accuracy.
	 * 
	 * @return accuracy
	 */
	public int getAccuracy() { return accuracy; }
	
	/**
	 * Get the move's pp.
	 * 
	 * @return pp
	 */
	public int getPP() { return pp; }
	
	/**
	 * Get the move's priority.
	 * 
	 * @return priority
	 */
	public int getPriority() { return priority; }
	
	/**
	 * Get the move's opCode.
	 * 
	 * @return opCode
	 */
	public int getOpCode() { return opCode; }
}

