package pkmn;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * The PkmnPokemon enumeration.
 * Contains all of the possible Pokemon that players can choose from.
 * 
 * @author Pokemon Purple Team
 * @version 1 December 2011
 *
 */
public enum PkmnPokemon {

	// { Type1, Type2, HP, Attack, Defense, Special Attack, Special Defense, Speed, four moves, image url }

	Bulbasaur(PkmnType.GRASS, PkmnType.POISON, 588, 111, 111, 128, 128, 106, PkmnMove.GigaDrain, PkmnMove.SeedBomb, PkmnMove.SludgeBomb, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Ivysaur(PkmnType.GRASS, PkmnType.POISON, 648, 125, 126, 145, 145, 123, PkmnMove.EnergyBall, PkmnMove.LeafStorm, PkmnMove.SludgeBomb, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Venusaur(PkmnType.GRASS, PkmnType.POISON, 728, 134, 135, 152, 152, 132, PkmnMove.PowerWhip, PkmnMove.EnergyBall, PkmnMove.SludgeBomb, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Charmander(PkmnType.FIRE, PkmnType.NULL, 564, 114, 104, 123, 112, 128, PkmnMove.ThunderPunch, PkmnMove.FlareBlitz, PkmnMove.RockSlide, PkmnMove.BrickBreak),
	Charmeleon(PkmnType.FIRE, PkmnType.NULL, 640, 127, 121, 145, 128, 145, PkmnMove.FireBlast, PkmnMove.DragonPulse, PkmnMove.Crunch, PkmnMove.MetalClaw),
	Charizard(PkmnType.FIRE, PkmnType.FLYING, 720, 149, 143, 177, 150, 167, PkmnMove.FireBlast, PkmnMove.AirSlash, PkmnMove.FocusBlast, PkmnMove.ShadowClaw),
	Squirtle(PkmnType.WATER, PkmnType.NULL, 584, 110, 128, 112, 127, 104, PkmnMove.Bite, PkmnMove.AquaJet, PkmnMove.Waterfall, PkmnMove.BrickBreak),
	Wartortle(PkmnType.WATER, PkmnType.NULL, 644, 126, 145, 128, 145, 121, PkmnMove.Bite, PkmnMove.IceBeam, PkmnMove.Surf, PkmnMove.BrickBreak),
	Blastoise(PkmnType.WATER, PkmnType.NULL, 724, 148, 167, 150, 172, 143, PkmnMove.RockSlide, PkmnMove.Scald, PkmnMove.FlashCannon, PkmnMove.IceBeam),
	Pikachu(PkmnType.ELECTRIC, PkmnType.NULL, 548, 117, 90, 112, 101, 156, PkmnMove.Strength, PkmnMove.GrassKnot, PkmnMove.Thunderbolt, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	//Raichu(PkmnType.ELECTRIC, PkmnType.NULL, 648, 156, 117, 156, 145, 167, PkmnMove.Thunder, PkmnMove.Thunderbolt, PkmnMove.HiddenPower.setType(PkmnType.ICE), PkmnMove.FocusBlast),
	Alakazam(PkmnType.PSYCHIC, PkmnType.NULL, 628, 112, 106, 205, 150, 189, PkmnMove.Psychic, PkmnMove.Psyshock, PkmnMove.FocusBlast, PkmnMove.ShadowBall),
	Machamp(PkmnType.FIGHTING, PkmnType.NULL, 768, 200, 145, 128, 150, 117, PkmnMove.DynamicPunch, PkmnMove.IcePunch, PkmnMove.ThunderPunch, PkmnMove.StoneEdge),
	Magneton(PkmnType.ELECTRIC, PkmnType.STEEL, 608, 123, 161, 189, 134, 134, PkmnMove.VoltSwitch, PkmnMove.Thunderbolt, PkmnMove.FlashCannon, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	//Gastly(PkmnType.GHOST, PkmnType.POISON, 528, 95, 90, 167, 95, 145, PkmnMove.ShadowBall, PkmnMove.Psychic, PkmnMove.SludgeBomb, PkmnMove.DarkPulse),
	Haunter(PkmnType.GHOST, PkmnType.POISON, 588, 112, 106, 183, 117, 161, PkmnMove.ShadowBall, PkmnMove.EnergyBall, PkmnMove.SludgeBomb, PkmnMove.Thunderbolt),
	Gengar(PkmnType.GHOST, PkmnType.POISON, 648, 128, 123, 200, 139, 178, PkmnMove.ShadowBall, PkmnMove.FocusBlast, PkmnMove.Psychic, PkmnMove.Thunderbolt),
	Onix(PkmnType.ROCK, PkmnType.GROUND, 548, 106, 233, 90, 106, 134, PkmnMove.StoneEdge, PkmnMove.Earthquake, PkmnMove.FlashCannon, PkmnMove.IronTail),
	Exeggutor(PkmnType.GRASS, PkmnType.PSYCHIC, 788, 161, 150, 194, 128, 117, PkmnMove.LeafStorm, PkmnMove.GigaDrain, PkmnMove.Psyshock, PkmnMove.AncientPower),
	Cubone(PkmnType.GROUND, PkmnType.NULL, 608, 112, 161, 101, 112, 95, PkmnMove.Earthquake, PkmnMove.ThunderPunch, PkmnMove.FirePunch, PkmnMove.DoubleEdge),
	Hitmonchan(PkmnType.FIGHTING, PkmnType.NULL, 608, 172, 144, 95, 178, 140, PkmnMove.CloseCombat, PkmnMove.ThunderPunch, PkmnMove.IcePunch, PkmnMove.DrainPunch),
	//Rhyhorn(PkmnType.GROUND, PkmnType.ROCK, 728, 150, 161, 90, 90, 84, PkmnMove.Earthquake, PkmnMove.StoneEdge, PkmnMove.Megahorn, PkmnMove.Superpower),
	Rhydon(PkmnType.GROUND, PkmnType.ROCK, 828, 200, 189, 106, 106, 101, PkmnMove.Megahorn, PkmnMove.Earthquake, PkmnMove.RockSlide, PkmnMove.HammerArm),
	Kangaskhan(PkmnType.NORMAL, PkmnType.NULL, 828, 161, 145, 101, 145, 156, PkmnMove.DrainPunch, PkmnMove.Earthquake, PkmnMove.Return, PkmnMove.SuckerPunch),
	Scyther(PkmnType.BUG, PkmnType.FLYING, 688, 178, 145, 117, 145, 145, PkmnMove.BugBite, PkmnMove.AerialAce, PkmnMove.BrickBreak, PkmnMove.NightSlash),
	Jynx(PkmnType.ICE, PkmnType.PSYCHIC, 668, 112, 95, 183, 161, 161, PkmnMove.IceBeam, PkmnMove.Psyshock, PkmnMove.FocusBlast, PkmnMove.EnergyBall),
	Electabuzz(PkmnType.ELECTRIC, PkmnType.NULL, 668, 148, 119, 161, 150, 172, PkmnMove.Thunderbolt, PkmnMove.HiddenPower.setType(PkmnType.ICE), PkmnMove.FocusBlast, PkmnMove.Psychic),
	Magmar(PkmnType.FIRE, PkmnType.NULL, 668, 161, 119, 167, 150, 159, PkmnMove.Flamethrower, PkmnMove.FocusBlast, PkmnMove.ThunderPunch, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Pinsir(PkmnType.BUG, PkmnType.NULL, 668, 194, 167, 117, 134, 150, PkmnMove.XScissor, PkmnMove.StoneEdge, PkmnMove.Earthquake, PkmnMove.CloseCombat),
	Gyarados(PkmnType.WATER, PkmnType.FLYING, 788, 194, 144, 123, 167, 146, PkmnMove.Waterfall, PkmnMove.StoneEdge, PkmnMove.Earthquake, PkmnMove.Flamethrower),
	//Eevee(PkmnType.NORMAL, PkmnType.NULL, 628, 117, 112, 106, 128, 117, PkmnMove.QuickAttack, PkmnMove.Bite, PkmnMove.IronTail, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Vaporeon(PkmnType.WATER, PkmnType.NULL, 928, 128, 123, 178, 161, 128, PkmnMove.Scald, PkmnMove.IceBeam, PkmnMove.HydroPump, PkmnMove.ShadowBall),
	Jolteon(PkmnType.ELECTRIC, PkmnType.NULL, 668, 128, 123, 178, 161, 200, PkmnMove.Thunderbolt, PkmnMove.ShadowBall, PkmnMove.VoltSwitch, PkmnMove.HiddenPower.setType(PkmnType.ICE)),
	Flareon(PkmnType.FIRE, PkmnType.NULL, 668, 200, 123, 161, 178, 128, PkmnMove.Flamethrower, PkmnMove.ShadowBall, PkmnMove.Bite, PkmnMove.FireBlast),
	//Omanyte(PkmnType.ROCK, PkmnType.WATER, 548, 101, 167, 156, 117, 95, PkmnMove.Surf, PkmnMove.HydroPump, PkmnMove.AncientPower, PkmnMove.EarthPower),
	Omastar(PkmnType.ROCK, PkmnType.WATER, 688, 123, 194, 183, 134, 117, PkmnMove.Surf, PkmnMove.HydroPump, PkmnMove.IceBeam, PkmnMove.RockSlide),
	//Kabuto(PkmnType.ROCK, PkmnType.WATER, 528, 145, 156, 117, 106, 117, PkmnMove.Waterfall, PkmnMove.RockSlide, PkmnMove.AquaJet, PkmnMove.GigaDrain),
	Kabutops(PkmnType.ROCK, PkmnType.WATER, 648, 183, 172, 128, 134, 145, PkmnMove.Waterfall, PkmnMove.StoneEdge, PkmnMove.XScissor, PkmnMove.AquaJet),
	Aerodactyl(PkmnType.ROCK, PkmnType.FLYING, 728, 172, 128, 123, 139, 200, PkmnMove.StoneEdge, PkmnMove.AquaTail, PkmnMove.Earthquake, PkmnMove.Roost),
	Snorlax(PkmnType.NORMAL, PkmnType.NULL, 999, 178, 128, 128, 178, 90, PkmnMove.BodySlam, PkmnMove.Earthquake, PkmnMove.Crunch, PkmnMove.FirePunch),
	Articuno(PkmnType.ICE, PkmnType.FLYING, 768, 150, 167, 161, 194, 150, PkmnMove.Roost, PkmnMove.IceBeam, PkmnMove.Blizzard, PkmnMove.AerialAce),
	Zapdos(PkmnType.ELECTRIC, PkmnType.FLYING, 768, 156, 150, 194, 156, 167, PkmnMove.Thunderbolt, PkmnMove.HeatWave, PkmnMove.HiddenPower.setType(PkmnType.ICE), PkmnMove.Roost),
	Moltres(PkmnType.FIRE, PkmnType.FLYING, 768, 167, 156, 194, 150, 156, PkmnMove.FireBlast, PkmnMove.AirSlash, PkmnMove.AncientPower, PkmnMove.Roost),
	//Dratini(PkmnType.DRAGON, PkmnType.NULL, 572, 127, 106, 112, 112, 112, PkmnMove.Waterfall, PkmnMove.FireBlast, PkmnMove.DracoMeteor, PkmnMove.ExtremeSpeed),
	//Dragonair(PkmnType.DRAGON, PkmnType.NULL, 652, 149, 128, 134, 134, 134, PkmnMove.Thunder, PkmnMove.FireBlast, PkmnMove.AquaJet, PkmnMove.ExtremeSpeed),
	Dragonite(PkmnType.DRAGON, PkmnType.FLYING, 772, 203, 161, 167, 167, 145, PkmnMove.Superpower, PkmnMove.FirePunch, PkmnMove.DragonClaw, PkmnMove.Thunder),
	Mewtwo(PkmnType.PSYCHIC, PkmnType.NULL, 832, 178, 156, 226, 156, 200, PkmnMove.Psystrike, PkmnMove.AuraSphere, PkmnMove.IceBeam, PkmnMove.Thunder),
	Mew(PkmnType.PSYCHIC, PkmnType.NULL, 808, 167, 167, 167, 167, 167, PkmnMove.Blizzard, PkmnMove.Psyshock, PkmnMove.AuraSphere, PkmnMove.FireBlast);

	//Instance variables
	//type1 A Pokemon's primary type
	//type2 A Pokemon's secondary type (can be null)
	PkmnType type1, type2;
	//hp A Pokemon's health
	int hp;
	//atk A Pokemon's attack, def A Pokemon's defense, spa A Pokemon's special attack,
	//spd A Pokemon's special defense, spe A Pokemon's speed
	double atk, def, spa, spd, spe;
	//all of the moves available to the pokemon
	PkmnMove[] moveset = new PkmnMove[4];
	//the sprite to be used to visually represent pokemon 
	Icon animatedSprite;
	//a collection of images to be used to visually represent pokemon 
	Vector<Image> animationFrame = new Vector<Image>();

	/**
	 * Constructor for PkmnPokemon, instantiates instance vaiables
	 * 
	 * @param type1 A Pokemon's primary type
	 * @param type2 A Pokemon's secondary type (can be null)
	 * @param hp A Pokemon's health
	 * @param atk A Pokemon's attack
	 * @param def A Pokemon's defense
	 * @param spa A Pokemon's special attack
	 * @param spd A Pokemon's special defense
	 * @param spe A Pokemon's speed
	 * @param move0 A Pokemon's first attack
	 * @param move1 A Pokemon's second attack
	 * @param move2 A Pokemon's third attack
	 * @param move3 A Pokemon's fourth attack
	 */
	private PkmnPokemon(PkmnType type1, PkmnType type2, int hp, double atk, double def, double spa, double spd, double spe, PkmnMove move0, PkmnMove move1, PkmnMove move2, PkmnMove move3)
	{
		this.type1 = type1;
		this.type2 = type2;
		this.hp = hp;
		this.atk = atk;
		this.def = def;
		this.spa = spa;
		this.spd = spd;
		this.spe = spe;
		moveset[0] = move0;
		moveset[1] = move1;
		moveset[2] = move2;
		moveset[3] = move3;
		loadAnimationFrames();
	}

	/**
	 * Loads all of the individual frames of a Pokemon animation into
	 * a Vector of images.
	 */
	public void loadAnimationFrames()
	{
		String path = "images/split_images/" + this.toString() + "/frame";

		//Counting the frames
		int counter = 0;

		//Used for checking if the next image exists.
		boolean nextImage = true;

		//While the next image exists
		while (nextImage)
		{
			try
			{
				//Add the image to the vector and increment the counter.
				if (counter < 10)
					animationFrame.add(ImageIO.read(new File(path + "0" + counter + ".png")));
				else
					animationFrame.add(ImageIO.read(new File(path + counter + ".png")));

				counter++;
			} 
			//If there's an exception, it's most likely because it's iterated through all the frames
			// so stop the loop when that happens.
			catch (IOException e)
			{
				nextImage = false;
			}
		}
	}

	/**
	 * Get the Vector of animation frames of a Pokemon
	 * 
	 * @return animationFrame
	 */
	public Vector<Image> getAnimationFrames() { return animationFrame; }

	/**
	 * Get the Pokemon's animated image (called a sprite)
	 * 
	 * @return sprite
	 */
	public Icon getAnimatedSprite() { return animatedSprite; }

	/**
	 * Get the Pokemon's HP stat.
	 * 
	 * @return hp
	 */
	public int getHP() { return hp; }

	/**
	 * Get the Pokemon's moveset
	 * 
	 * @return moveset
	 */
	public PkmnMove[] getMoveSet() {return moveset; }

	/**
	 * Get the Pokemon's primary type.
	 * 
	 * @return type1
	 */
	public PkmnType getType1() { return type1; }

	/**
	 * Get the Pokemon's secondary type.
	 * 
	 * @return type2
	 */
	public PkmnType getType2() { return type2; }
	/**
	 * Get the Pokemon's attack stat.
	 * 
	 * @return atk
	 */
	public double getAtk() { return atk; }

	/**
	 * Get the Pokemon's defense stat.
	 * 
	 * @return def
	 */
	public double getDef() { return def; }

}
