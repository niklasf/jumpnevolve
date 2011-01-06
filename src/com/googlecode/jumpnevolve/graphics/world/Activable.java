package com.googlecode.jumpnevolve.graphics.world;

/**
 * Interface für ein aktivierbares Objekt
 * 
 * @author Erik Wagner
 * 
 */
public interface Activable extends Accompanying {

	/**
	 * Wird aufgerufen, wenn dieses Objekt aktiviert werden soll und kann. </br>
	 * Aktiviert dieses Objekt.
	 * 
	 * @param activator
	 *            Das aktivierende Objekt
	 */
	public void activate(Activating activator);

	/**
	 * Wird aufgerufen, wenn dieses Objekt deaktiviert werden soll und kann.
	 * </br> Deaktiviert dieses Objekt.
	 * 
	 * @param activator
	 *            Das deaktivierende Objekt
	 */
	public void deactivate(Activating deactivator);

	/**
	 * Prüft, ob ein Objekt dieses Objekt aktivieren kann.
	 * 
	 * @param activator
	 *            Das zu prüfende Objekt
	 * @return {@code true}, wenn dieses Objekt durch {@code activator}
	 *         aktiviert werden kann
	 */
	public boolean isActivableBy(Activating activator);

	/**
	 * Prüft, ob ein Objekt dieses Objekt deaktivieren kann
	 * 
	 * @param deactivator
	 *            Das zu prüfende Objekt
	 * @return {@code true}, wenn dieses Objekt durch {@code deactivator}
	 *         deaktiviert werden kann
	 */
	public boolean isDeactivableBy(Activating deactivator);

	/**
	 * @return <code>true</code>, wenn dieses Objekt aktiviert ist;</p>
	 *         <code>false</code>, wenn dieses Objek deaktiviert ist
	 */
	public boolean isActivated();
}
