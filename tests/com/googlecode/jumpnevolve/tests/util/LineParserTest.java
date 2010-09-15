package com.googlecode.jumpnevolve.tests.util;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import com.googlecode.jumpnevolve.util.LineAdapter;
import com.googlecode.jumpnevolve.util.LineFailedException;
import com.googlecode.jumpnevolve.util.LineParser;

public class LineParserTest {

	private int i = 0;

	@Test
	public void testLineParser() throws IOException {
		// Beispielreader erzeugen
		// Die Zeilen könnten auch aus einer Datei stammen.
		String test = "123,abc\r\n" +
				"983, def\n" +
				"   \t   \r\n" // Leere	Zeile, die ignoriert werden sollte
				+ "786   ,zdhfjs\n";
		StringReader reader = new StringReader(test);

		// Die geparsten Werte prüfen
		new LineParser("([0-9]+)\\s*,\\s*([a-z]+)", new LineAdapter() {
			public boolean lineParsed(String line, String[] parts) {
				i++;

				assertThat(parts.length, is(2));

				if (i == 1) {
					assertThat(line, is(equalTo("123,abc")));
					assertThat(parts[0], is(equalTo("123")));
					assertThat(parts[1], is(equalTo("abc")));
				} else if (i == 2) {
					assertThat(line, is(equalTo("983, def")));
					assertThat(parts[0], is(equalTo("983")));
					assertThat(parts[1], is(equalTo("def")));
				} else if (i == 3) {
					assertThat(line, is(equalTo("786   ,zdhfjs")));
					assertThat(parts[0], is(equalTo("786")));
					assertThat(parts[1], is(equalTo("zdhfjs")));
				}

				return true;
			}
		}).parse(reader);
		assertThat(i, is(3));

		// Es wird ein Fehler erwartet, da die Syntax nicht mit der Eingabe
		// übereinstimmt.
		try {
			LineParser parser = new LineParser("([0-9]+)");
			parser.parse(new StringReader(test), new LineAdapter());
			throw new AssertionError("The test should not reach this point.");
		} catch (LineFailedException expected) {
		}
	}
}
