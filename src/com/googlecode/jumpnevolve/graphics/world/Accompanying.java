/**
 * 
 */
package com.googlecode.jumpnevolve.graphics.world;

/**
 * Ein Interface für Objekte, die einer "Gruppe" zugehörig ist
 * 
 * @author Erik Wagner
 * 
 */
public interface Accompanying {

	public static int COMPANY_UNDEFINED = 0;
	public static int COMPANY_OBJECT = 1;
	public static int COMPANY_ENEMY = 2;
	public static int COMPANY_PLAYER = 3;
	public static int COMPANY_GROUND = 4;

	/**
	 * Die "Gruppe" des Objekts
	 * 
	 * @return Eine der COMPANY-Konstanten aus {@link Accompanying}
	 */
	public int getCompany();
}
