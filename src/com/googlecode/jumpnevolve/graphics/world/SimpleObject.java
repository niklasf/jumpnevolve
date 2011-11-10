package com.googlecode.jumpnevolve.graphics.world;

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
}
