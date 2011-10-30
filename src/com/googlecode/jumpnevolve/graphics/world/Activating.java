package com.googlecode.jumpnevolve.graphics.world;

/**
 * Ein Interface für Objekte, die {@link Activable} aktivieren/deaktivieren
 * können
 *
 * @author Erik Wagner
 *
 */
public interface Activating extends Accompanying {

	/**
	 * Wird durch ein Activable aufgerufen, wenn es durch dieses Objekt
	 * aktiviert wurde
	 *
	 * @param object
	 *            Das Activable, das aktiviert wurde
	 */
	public void hasActivated(Activable object);

	/**
	 * Wird durch ein Activable aufgerufen, wenn es durch dieses Objekt
	 * deaktiviert wurde
	 *
	 * @param object
	 *            Das Activable, das deaktiviert wurde
	 */
	public void hasDeactivated(Activable object);

	/**
	 * Gibt zurück, ob dieses Objekt ein {@link Activable} aktivieren will
	 *
	 * @param object
	 *            Das {@link Activable}-Objekt
	 * @return <code>true</code>, wenn dieses Objekt das {@link Activable}
	 *         aktivieren will
	 */
	public boolean wantActivate(Activable object);

	/**
	 * Gibt zurück, ob dieses Objekt ein {@link Activable} deaktivieren will
	 *
	 * @param object
	 *            Das {@link Activable}-Objekt
	 * @return <code>true</code>, wenn dieses Objekt das {@link Activable}
	 *         deaktivieren will
	 */
	public boolean wantDeactivate(Activable object);
}
