package com.googlecode.jumpnevolve.graphics.gui;

/**
 * Ein Informable kann über die Aktionen eines InterfaceObject informiert
 * werden. Dazu wird {@link InterfaceObject} verwendet.
 * 
 * @author Erik Wagner
 * 
 */
public interface Informable {

	/**
	 * Wird aufgerufen, wenn ein Interface-Objekt eine Aktion auslöst
	 * 
	 * @param function
	 *            Ein Integerwert, der dem Wert entspricht, mit dem der Button
	 *            initialisiert wurde (eine Konstante aus
	 *            {@link InterfaceConstants})
	 * @param object
	 *            Das Objekt durch welches die Aktion ausgelöst wurden
	 */
	public void interfaceAction(int function, InterfaceObject object);
}
