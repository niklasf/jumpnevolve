package com.googlecode.jumpnevolve.util;

/**
 * <p>
 * Standartimplementierung {@link LineListener}, die überschrieben werden kann
 * um nur eizelne Methoden vom Standartverhalten abweichen zu lassen.
 * </p>
 * 
 * <ul>
 * <li>Leere Zeilen werden ignoriert und das Parsen wird mit der nächsten Zeile
 * fortgesetzt.</li>
 * <li>Bei Fehler (wenn die gelesene Zeile nicht zum regulären Ausdruck passt),
 * wird eine {@link LineFailedException} geworfen.</li>
 * <li>Erfolgreich gelesene und geparste Zeilen werden nicht weiter verarbeitet.
 * Das Parsen wird fortgesetzt.</li>
 * </ul>
 * 
 * @author Niklas Fiekas
 */
public class LineAdapter implements LineListener {

	@Override
	public boolean lineEmpty() {
		return true;
	}

	@Override
	public boolean lineFailed(String line) {
		throw new LineFailedException(line);
	}

	@Override
	public boolean lineParsed(String line, String[] parts) {
		return true;
	}

}
