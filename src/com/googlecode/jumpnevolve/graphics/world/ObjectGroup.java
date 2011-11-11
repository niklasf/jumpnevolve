package com.googlecode.jumpnevolve.graphics.world;

/**
 * Ein Interface für Objekte, die aus mehreren Objekten bestehen.
 * <p>
 * Die zusätzlichen Objekte werden durch {@link getObjects()} zurückgegeben und
 * in die World automatisch eingebunden.
 * 
 * @author Erik Wagner
 * 
 */
public interface ObjectGroup {

	/**
	 * Gibt die zusätzlichen Objekte dieses Objekts zurück
	 * 
	 * @return Ein Array der Objekte, <strong>ohne</strong> dieses Objekt selbst
	 */
	public AbstractObject[] getObjects();
}
