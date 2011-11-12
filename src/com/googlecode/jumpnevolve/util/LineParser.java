package com.googlecode.jumpnevolve.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hilft, eine Datei zeilenweise zu lesen und anhand eines regulären Ausdrucks
 * zu zerlegen.
 * 
 * 
 * @author Niklas Fiekas
 */
public class LineParser {

	private final Pattern pattern;

	private LineListener lineListener;

	private boolean shouldIgnore(String line) {
		return line.isEmpty() || line.matches("\\s*");
	}

	/**
	 * Erzeugt einen neuen Zeilenparser.
	 * 
	 * @param regex
	 *            Ausdruck, nach dem die einzelnen Zeilen geparst werden.
	 */
	public LineParser(String regex) {
		this(regex, null);
	}

	/**
	 * Erzeugt einen neuen Zeilenparser.
	 * 
	 * @param regex
	 *            Ausdruck, nach dem die einzelnen Zeilen geparst werden.
	 * @param lineListener
	 *            Standart {@link LineListener}.
	 */
	public LineParser(String regex, LineListener lineListener) {
		this.pattern = Pattern.compile(regex);
		this.lineListener = lineListener;
	}

	/**
	 * Parst die Eingaben von einem Reader. Die Ergebnisse werden an den
	 * Standart {@link LineListener} gesendet.
	 * 
	 * @param reader
	 *            Reader.
	 * @throws IOException
	 *             Wenn ein Fehler beim Lesen auftritt.
	 */
	public void parse(Reader reader) throws IOException {
		parse(reader, lineListener);
	}

	/**
	 * Parst die Eingaben von einem Reader.
	 * 
	 * @param reader
	 *            Reader.
	 * @param listener
	 *            Die Ergebnisse werden an diesen {@link LineListener} gesendet.
	 * @throws IOException
	 *             Wenn ein Fehler beim Lesen auftritt.
	 */
	public void parse(Reader reader, LineListener listener) throws IOException {
		// BufferedReader erzeugen
		BufferedReader buffer;
		if (reader instanceof BufferedReader) {
			buffer = (BufferedReader) reader;
		} else {
			buffer = new BufferedReader(reader);
		}

		// Jede Zeile parsen
		String line;
		boolean next = true;
		while (next && (line = buffer.readLine()) != null) {
			if (!shouldIgnore(line)) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					String[] matches = new String[matcher.groupCount()];
					for (int i = 0; i < matches.length; i++) {
						matches[i] = matcher.group(i + 1);
					}
					next = listener.lineParsed(line, matches);
				} else {
					next = listener.lineFailed(line);
				}
			} else {
				next = listener.lineEmpty();
			}
		}
	}
}
