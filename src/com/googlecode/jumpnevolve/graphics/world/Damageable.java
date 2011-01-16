package com.googlecode.jumpnevolve.graphics.world;

import com.googlecode.jumpnevolve.math.Kollision;

/**
 * 
 * @author Erik Wagner
 * 
 */
public interface Damageable extends Accompanying {

	public static int DAMAGE_NORMAL = 0;
	public static int DAMAGE_FIRE = 1;
	public static int DAMAGE_WATER = 2;
	public static int DAMAGE_STAB = 3;
	public static int DAMAGE_SWORD = 4;
	public static int DAMAGE_MAGIC = 5;

	/**
	 * @return Der Schaden, den dieses Objekt verursacht
	 */
	public int getDamage();

	/**
	 * @return Die Art des Schadens, den dieses Objekt verursacht
	 */
	public int getKindOfDamage();

	/**
	 * Gibt zurück, ob dieses Objekt einem anderen Objekt Schaden zufügen will
	 * 
	 * @param object
	 *            Das andere Objekt
	 * @return <code>true</code>, wenn dieses Objekt dem anderen Objekt Schaden
	 *         zufügen will
	 */
	public boolean wantDamaging(Living object);

	/**
	 * Gibt zurück, ob dieses Objekt einem anderen Objekt Schaden zufügen kann
	 * 
	 * @param col
	 *            Die Kollision der beiden Objekte, ausgehend von diesem Objekt
	 * @return <code>true</code>, wenn dieses Objekt dem anderen Objekt Schaden
	 *         zufügen kann
	 */
	public boolean canDamage(Kollision col);
}
