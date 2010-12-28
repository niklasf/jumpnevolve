/**
 * 
 */
package com.googlecode.jumpnevolve.editor;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Erik Wagner
 * 
 */
public class Arguments extends JPanel {

	private JTextField[] contents;
	private char[] hyphen;
	private boolean alreadyInitialize = false;

	/**
	 * Erzeugt ein Arguments-Objekt ohne Inhalt
	 */
	public Arguments() {
		super();
	}

	/**
	 * Initialisiert das Arguments-Objekt
	 * 
	 * @param contents
	 *            Inhalt der Argumente, pro contens-String wird ein Textfeld
	 *            erzeugt
	 * @param hyphen
	 *            Trennungszeichen zwischen den Argumenten für die spätere
	 *            Speicherung in einer Leveldatei (z.B. "|" oder ",")
	 */
	public void initArguments(String[] contents, char[] hyphen,
			String[] initContents) {
		if (alreadyInitialize == false) {
			if (controlArgs(contents, hyphen, initContents)) {
				this.setLayout(new GridLayout(contents.length, 2));
				this.hyphen = hyphen;
				this.contents = new JTextField[contents.length];
				for (int i = 0; i < contents.length; i++) {
					this.add(new JLabel(contents[i] + ":"));
					this.contents[i] = new JTextField();
					this.add(this.contents[i]);
					this.contents[i].setText(initContents[i]);
				}
			} else {
				System.out
						.println("Fehler bei der Erstellung des Argumentsobjekts");
				this.hyphen = null;
				this.contents = null;
			}
		} else {
			System.out.println("Objekt wurde bereits initialisiert.");
		}
		alreadyInitialize = true;
	}

	private boolean controlArgs(String[] con, char[] hyph, String[] initCon) {
		return hyph.length == con.length - 1 && con.length == initCon.length;
	}

	public String getArguments() {
		if (alreadyInitialize) {
			String re = "";
			for (int i = 0; i < contents.length - 1; i++) {
				re = re + this.contents[i].getText().trim() + this.hyphen[i];
			}
			re = re + this.contents[this.contents.length - 1].getText().trim();
			return re;
		} else {
			return "none";
		}
	}
}
