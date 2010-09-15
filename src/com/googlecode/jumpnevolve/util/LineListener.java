package com.googlecode.jumpnevolve.util;

/**
 * Erhält die Ergebnisse von einem {@link LineParser} und kann sie verarbeiten.
 * 
 * @author Niklas Fiekas
 */
public interface LineListener {

	/**
	 * Wird für jede Zeile einzeln in korrekter Reihenfolge aufgerufen.
	 * 
	 * @param line
	 *            Die Zeile, wie sie eingelesen wurde.
	 * @param parts
	 *            Die durch den regulären Ausdruck ausgewählten Teilstrings.
	 * @return {@code true}, wenn das Parsen mit der nächsten Zeile fortgesetzt
	 *         werden soll.
	 */
	public boolean lineParsed(String line, String[] parts);

	/**
	 * Wird aufgerufen, wenn eine Zeile ignoriert wurde, weil sie nur Whitespace
	 * enthielt oder leer war.
	 * 
	 * @return {@code true}, wenn das Parsen mit der nächsten Zeile fortgesetzt
	 *         werden soll.
	 */
	public boolean lineEmpty();

	/**
	 * Wird aufgerufen, wenn eine Zeile nicht zum vorgesehenen regulären
	 * Ausdruck passt.
	 * 
	 * @param line
	 *            Die Zeile, wie sie eingelesen wurde.
	 * @return {@code true}, wenn das Parsen mit der nächsten Zeile fortgesetzt
	 *         werden soll.
	 */
	public boolean lineFailed(String line);
}
