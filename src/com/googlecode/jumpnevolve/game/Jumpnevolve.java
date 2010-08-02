/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.game;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import org.newdawn.slick.Image;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.Engine;

/**
 * @author Niklas Fiekas
 */
public class Jumpnevolve {
	
	public static Image wood;

	/**
	 * Startet das Spiel.
	 * 
	 * @param args
	 *            Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		try {
			// TODO: Logging einrichten
			
			// Grafikengine holen und initialisieren
			// Wenn es Probleme mit der Auflösung gibt, wird hier möglicherweise
			// eine Exception geworfen.
			Engine engine = Engine.getInstance();

			// TODO: Ressourcen laden

			// TODO: Hauptmenü laden

			// Demolevel laden und darstellen
			engine.switchState(new DemoLevel(new LevelWorldFactory()).getSimulatedWorld());

			// Die Engine starten.
			// Das Programm wird automatisch aufgeräumt und beendet,
			// wenn der Benutzer das Fenster schließt.
			engine.start();
		} catch (Throwable e) {
			// Versuchen alle Fehler abzufangen und sie in den Log zu schreiben
			try {
				Log.error(e);
			} catch (Throwable ex) {
				System.err.println("Error in log implementation.");
			}

			// Eine Meldung anzeigen, damit das Programm nicht kommentarlos
			// schließt
			// TODO: Mehr Informationen zu verschiedenen Fehler und die
			// Möglichkeit einen Bug Report zu senden
			StringWriter messageWriter = new StringWriter();
			PrintWriter message = new PrintWriter(messageWriter);
			message.println("Es ist ein unerwarteter Fehler aufgetreten.");
			message.println("Das Programm muss geschlossen werden.");
			message.println();
			e.printStackTrace(message);
			JOptionPane.showMessageDialog(null, messageWriter.toString());
		}
	}
}
