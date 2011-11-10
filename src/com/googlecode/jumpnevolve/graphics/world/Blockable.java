package com.googlecode.jumpnevolve.graphics.world;

/**
 * @author Erik Wagner
 * 
 */
public interface Blockable extends Accompanying, SimpleObject {

	/**
	 * Gibt zurück, ob dieses Objekt ein anderes blockieren will
	 * 
	 * @param other
	 *            Das andere {@link Blockable}
	 * @return <code>true</code>, wenn das Objekt blockiert werden soll
	 */
	public boolean wantBlock(Blockable other);

	/**
	 * Gibt zurück, ob dieses Objekt von einem anderen blockiert werden kann
	 * 
	 * @param other
	 *            Das andere {@link Blockable}
	 * @return <code>true</code>, wenn dieses Objekt blockiert werden kann
	 */
	public boolean canBeBlockedBy(Blockable other);
}
