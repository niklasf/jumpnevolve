package com.googlecode.jumpnevolve.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader {

	private String source;

	private Level level;

	public Levelloader(String source) {
		this.source = source;
	}

	public void run() {
		InputStream levelFile;
		try {
			String[] sourceSplit = this.source.split(".");
			if (sourceSplit.length == 2) {
				if (sourceSplit[1].equals("txt")) {
					// neues Level aus Textdatei erstellen
					levelFile = new FileInputStream(source);
					BufferedReader levelFileReader = new BufferedReader(
							new InputStreamReader(levelFile));
					// Level durch Kopfzeile erstellen --> Größenordnungen
					String firstLine = levelFileReader.readLine();
					String[] firstLineSplit = firstLine.split("_");
					if (firstLineSplit[0].equals("Leveldimensionen") == false) {
						// FIXME: Fehlermeldung ausgeben und abbrechen
					}
					this.level = new Level(Integer.parseInt(firstLineSplit[1]),
							Integer.parseInt(firstLineSplit[2]), Integer
									.parseInt(firstLineSplit[3]));
					// Einstellungen vornehmen
					String secondLine = levelFileReader.readLine();
					String[] secondLineSplit = secondLine.split("_");
					if (firstLineSplit[0].equals("Leveleinstellungen") == false) {
						// FIXME: Fehlermeldung ausgeben und abbrechen
					}
					// FIXME: Einstellungen für das Level vornehmen
					// Beispiele: verfügbare Charaktere, Timer etc.

					// HashMaps für Objekte zum Zwischenspeichern erstellen
					HashMap<String, AbstractObject> activableObjects = new HashMap<String, AbstractObject>();
					ArrayList<AbstractObject> activatingObjects = new ArrayList<AbstractObject>();
					ArrayList<String> argumtensForActivating = new ArrayList<String>();
					ArrayList<AbstractObject> otherObjects = new ArrayList<AbstractObject>();

					String current = levelFileReader.readLine();
					while (current != null) { // Pseudo-Methode ersetzen
						String[] currentSplit = current.split("_");
						String[] currentArguments = currentSplit[2].split(",");
						current = levelFileReader.readLine();
						if (currentSplit[0].equals("WalkingSoldier")) {
							otherObjects.add(new WalkingSoldier(this.level,
									this.toVector(currentSplit[1])));
						} else if (currentSplit[0].equals("JumpingSoldier")) {
							otherObjects.add(new JumpingSoldier(this.level,
									this.toVector(currentSplit[1])));
						}
						// TODO: Weitere Klassen einfügen
					}
				} else if (sourceSplit[1].equals("bin")) {
					// Speicherung laden --> Level-Objekt
					levelFile = new ObjectInputStream(new FileInputStream(
							source));
					// Objekt lesen, in Level konvertieren und abspeichern
					try {
						Object object = ((ObjectInputStream) levelFile)
								.readObject();
						if (object instanceof Level) {
							this.level = (Level) object;
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				// Ausgabe: Fehler beim Laden des Levels
				// FIXME: Möglich durch throw new IOException()?
			}
		} catch (IOException e) {
			// Ausgabe: Fehler beim Laden des Levels
		} finally {
			levelFile.close();
		}
	}

	public Level getLevel() {
		return this.level;
	}

	public static Level asyncLoadLevel(String source) {
		Levelloader loader = new Levelloader(source);
		loader.run();
		return loader.getLevel();
	}

	private Vector toVector(String koordinate) {
		String[] koordinates = koordinate.split("|");
		// FIXME: Falscher String (nicht in der Form "50|50") abfangen und
		// Fehler melden
		return new Vector(Float.parseFloat(koordinates[0]), Float
				.parseFloat(koordinates[1]));
	}
}