package pkmn;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * The PkmnType enumeration.
 * Contains all of the possible types a Pokemon can have.
 * 
 * @author Pokemon Purple Team
 * @version 5 November 2011
 *
 */
public enum PkmnType 
{
	NORMAL(0), FIRE(1), WATER(2), ELECTRIC(3),
	GRASS(4), ICE(5), FIGHTING(6), POISON(7),
	GROUND(8), FLYING(9), PSYCHIC(10), BUG(11),
	ROCK(12), GHOST(13), DRAGON(14), DARK(15),
	STEEL(16), VARIABLE(17), NULL(18);
	
	//Instance variables
	private int typeNum;
	Image typeImage;
	
	/**
	 * Constructor for PkmnType
	 * 
	 * @param typeNum
	 */
	private PkmnType(int typeNum)
	{
		this.typeNum = typeNum;
		typeImage = loadImage(toString());
	}
	
	/**
	 * Uploads the image of a Type based on the file location given.
	 * 
	 * @param url The file location
	 * @return The Image uploaded
	 */
	public Image loadImage(String url)
	{
		Image img = null;
		String path = "images/nonCharImages/type/";
		//Images for VARIABLE and NULL do not exist.
			try {
				img = ImageIO.read(new File(path + url + ".png"));
			} 
			catch (IOException e) {
				//So give them a default image.
				try {
					img = ImageIO.read(new File(path + "UNKNOWN.png"));
				} catch (IOException e1) {}
			}
		
		return img;
	}
	
	
	/**
	 * Get the number corresponding to the type.
	 * 
	 * @return typeNum
	 */
	public int getNum() { return typeNum; }
	
	/**
	 * Get the image representing the type.
	 * 
	 * @return typeImage
	 */
	public Image getImage() {return typeImage; }
	
}
