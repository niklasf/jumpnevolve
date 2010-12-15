package com.googlecode.jumpnevolve.graphics;

public interface AbstractEngine {

	/**
	 * Fügt einen Zustand hinzu, sodass er später aufgerufen werden kann. Wird
	 * ein existierender Zustand hinzugefügt, wird keine Aktion ausgeführt.
	 * 
	 * @param state
	 *            Neuer Zustand der Grafikengine.
	 */
	public abstract void addState(AbstractState state);

	/**
	 * Fragt ab, ob ein Zustand bereits hinzugefügt wurde.
	 * 
	 * @param state
	 *            Der zu prüfende Zustand
	 * @return {@code true}, wenn der Zustand hinzugefügt wurde.
	 */
	public abstract boolean containsState(AbstractState state);

	/**
	 * Bereitet den neuen Zustand vor, beendet den alten und startet den neuen.
	 * Der alte Zustand wird nicht neu initialisiert, wenn neuer und alter
	 * Zustand die selben sind.
	 * 
	 * @param state
	 *            Der neue Zustand
	 */
	public abstract void switchState(AbstractState state);

	/**
	 * @return Der aktuelle Zustand.
	 */
	public abstract AbstractState getCurrentState();

	public abstract void start();

	public abstract int getScreenHeight();
	
	public abstract int getWidth();
	
	public abstract int getHeight();

	public abstract int getScreenWidth();

}