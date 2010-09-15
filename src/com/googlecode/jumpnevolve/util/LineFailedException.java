package com.googlecode.jumpnevolve.util;

/**
 * Kann geworfen werden, wenn ein Fehler beim Parsen einer Zeile auftritt.
 * 
 * @author Niklas Fiekas
 */
public class LineFailedException extends RuntimeException {

	private static final long serialVersionUID = 8945755199789337059L;

	/**
	 * Erzeugt eine Instanz dieses Ausnahmetyps.
	 * 
	 * @param line
	 *            Der Inhalt der Zeile, die den Fehler verursacht.
	 */
	public LineFailedException(String line) {
		super(String.format("Line failed: %s", line));
	}

}
