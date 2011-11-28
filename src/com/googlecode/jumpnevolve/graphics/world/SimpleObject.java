package com.googlecode.jumpnevolve.graphics.world;

import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Interface für einfache Objekte, in der allgemeine Funktionen gesammelt
 * werden
 * <p>
 * So können Funktionen aus {@link AbstractObject} in anderen Interfaces genutzt
 * werden
 * 
 * @see AbstractObject implementiert SimpleObject bereits
 * 
 * @author Erik Wagner
 * 
 */
public interface SimpleObject {

	/**
	 * Gibt zurück, ob dieses Objekt bewegt werden kann
	 * 
	 * @return <code>true</code>, wenn dieses Objekt bewegbar ist
	 */
	public boolean isMoveable();

	/**
	 * Gibt die Masse des Objekts zurück
	 * 
	 * @return 0, wenn das Objekt keine Masse hat
	 */
	public float getMass();

	/**
	 * @return Die Position des Objekts
	 */
	public Vector getPosition();

	/**
	 * @return Der Impuls des Objekts
	 */
	public Vector getImpulse();
}
