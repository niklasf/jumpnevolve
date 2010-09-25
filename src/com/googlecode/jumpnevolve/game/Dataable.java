package com.googlecode.jumpnevolve.game;

/**
 * Ein Inerface für alle Objekte, die in Level-Dateien geschrieben werden können
 * 
 * @author Erik Wagner
 * 
 */
public interface Dataable {

	/**
	 * Formt das Objekt in eine entsprechende Zeile für eine Level-Datei um.
	 * 
	 * Die Form entspricht:
	 * Klassenname_Koordinate_NameDesObjekts_NamenDerZuAktivierendenObjekte_Argumente
	 * 
	 * Hierbei ist zu beachten, dass getName() der zu aktivierenden Objekte die
	 * entsprechenden Namen zurückgibt
	 * 
	 * @return Die Zeile für die Level-Datei, endet immer mit einem
	 *         Zeilenumbruch ("\n")
	 */
	public String toDataLine();

	/**
	 * @return Der Name des Objekts für Zuordnungen
	 */
	public String getName();
}
