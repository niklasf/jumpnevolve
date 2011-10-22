package com.googlecode.jumpnevolve.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.game.objects.ActivatingObject;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader {

	public final String source;

	private Level level;

	public Levelloader(String source) {
		this.source = source;
	}

	public void loadLevel() {
		FileInputStream levelFile = null;

		try {
			levelFile = new FileInputStream(this.source);

			if (this.source.toLowerCase().endsWith(".txt")) {
				// TODO: ".txt" soll in ".lvl" geändert werden

				// Neues Level aus Textdatei erstellen
				BufferedReader levelFileReader = new BufferedReader(
						new InputStreamReader(levelFile));

				// Level durch Kopfzeile erstellen --> Größenordnungen
				String firstLine = levelFileReader.readLine();
				String[] firstLineSplit = firstLine.split("_");
				if (firstLineSplit[0].equals("Leveldimensionen") == false
						|| firstLineSplit.length != 4) {
					throw new IOException(
							"Erste Zeile enthält nicht die Leveldimensionen");
				}
				this.level = new Level(this,
						Integer.parseInt(firstLineSplit[1]),
						Integer.parseInt(firstLineSplit[2]),
						Integer.parseInt(firstLineSplit[3]));
				// Einstellungen vornehmen
				String secondLine = levelFileReader.readLine();
				String[] secondLineSplit = secondLine.split("_");
				if (secondLineSplit[0].equals("Leveleinstellungen") == false) {
					throw new IOException(
							"Zweite Zeile enthält nicht die Leveleinstellungen");
				}
				// Zoomeinstellungen
				String[] zoom = secondLineSplit[1].split(",");
				if (zoom.length == 1) {
					try {
						this.level.setZoom(this.toFloat(zoom[0]));
					} catch (IOException e) {
						throw new IOException(
								"Fehler beim Laden des Zooms (1 Zoom-Argument)",
								e);
					}
				} else if (zoom.length == 2) {
					try {
						this.level.setZoom(this.toFloat(zoom[0]),
								this.toFloat(zoom[1]));
					} catch (IOException e) {
						throw new IOException(
								"Fehler beim Laden des Zooms (2 Zoom-Argumente)",
								e);
					}
				} else {
					throw new IOException("Die Zoomangaben sind unzulässig");
				}
				// Hintergrund setzen
				this.level
						.setBackground(secondLineSplit[secondLineSplit.length - 1]);

				// FIXME: Einstellungen für das Level vornehmen
				// TODO: Timer für das Level hinzufügen

				// Beispiele: verfügbare Charaktere, Timer etc.
				String thirdLine = levelFileReader.readLine();
				String[] thirdLineSplit = thirdLine.split("_");
				if (thirdLineSplit[0].equals("Player") == false) {
					throw new IOException(
							"Dritte Zeile enthält nicht die Playerangaben");
				}
				try {
					this.level.addPlayer(this.toVector(thirdLineSplit[3]),
							thirdLineSplit[2], thirdLineSplit[1],
							thirdLineSplit[4].split(","));
				} catch (IOException e) {
					throw new IOException(
							"Fehler beim Erstellen des Player-Objekts", e);
				}

				// HashMaps für Objekte zum Zwischenspeichern erstellen
				HashMap<String, Activable> activableObjects = new HashMap<String, Activable>();
				ArrayList<ActivatingObject> activatingObjects = new ArrayList<ActivatingObject>();
				ArrayList<String[]> argumtensForActivating = new ArrayList<String[]>();
				ArrayList<AbstractObject> otherObjects = new ArrayList<AbstractObject>();

				String current = levelFileReader.readLine();
				// Neue Struktur:
				// Klassenname_Koordinate_NameDesObjekts_NamenDerZuAktivierendenObjekte_Argumente
				// "none" beachten, als Platzhalter, wenn bestimmte Dinge nicht
				// von Nöten sind
				// mehrere Argumente werden durch "," getrennt
				while (current != null) { // Pseudo-Methode ersetzen
					String[] currentSplit = current.split("_");
					String name = currentSplit[2];
					String activates = currentSplit[3];
					AbstractObject newObject = null;
					newObject = GameObjects.loadObject(current, this.level);

					if (newObject != null) {
						boolean alreadyPutted = false;
						if (newObject instanceof ActivatingObject) {
							argumtensForActivating.add(activates.split(","));
							activatingObjects.add((ActivatingObject) newObject);
							alreadyPutted = true;
						}
						if (newObject instanceof Activable) {
							activableObjects.put(name, (Activable) newObject);
							alreadyPutted = true;
						}
						if (alreadyPutted == false) {
							otherObjects.add(newObject);
						}
					}

					current = levelFileReader.readLine();
				}
				// Zuweisen der zu aktivierenden Objekte
				for (int i = 0; i < activatingObjects.size(); i++) {
					for (int j = 0; j < argumtensForActivating.get(i).length; j++) {
						if (activableObjects
								.get(argumtensForActivating.get(i)[j]) != null) {
							activatingObjects.get(i).addActivable(
									activableObjects.get(argumtensForActivating
											.get(i)[j]));
						}
					}
				}
				// Einfügen der Objekte in das Level
				for (AbstractObject object : activatingObjects) {
					this.level.add(object);
				}
				for (AbstractObject object : otherObjects) {
					this.level.add(object);
				}
				for (Object object : activableObjects.values()) {
					this.level.add(object);
				}

			} else if (this.source.toLowerCase().endsWith(".dat")) {

				// Speicherung laden --> Level-Objekt
				ObjectInputStream objectLevelFile = new ObjectInputStream(
						levelFile);
				// Objekt lesen, in Level konvertieren und abspeichern
				try {
					Object object = objectLevelFile.readObject();
					if (object instanceof Level) {
						this.level = (Level) object;
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				// Fehler beim Laden des Levels
				throw new IOException("Nicht-verarbeitbare Dateiendung");
			}
		} catch (IOException e) {
			System.out.println("Fehler beim Laden des Levels:\n"
					+ e.getMessage());
			e.printStackTrace();
		} finally {
			if (levelFile != null) {
				try {
					levelFile.close();
				} catch (IOException e) {
					Log.error(e);
				}
			}
		}
	}

	public Level getLevel() {
		return this.level;
	}

	public static Level asyncLoadLevel(String source) {
		Levelloader loader = new Levelloader(source);
		loader.loadLevel();
		return loader.getLevel();
	}

	/*
	 * toVector() und toFloat() werden benutzt, um die Fehlerquellen genauer
	 * eingrenzen zu können
	 */

	private Vector toVector(String koordinate) throws IOException {
		try {
			return Vector.parseVector(koordinate);
		} catch (NumberFormatException e) {
			throw new IOException("Inkorrekter Vektor: " + koordinate);
		}
	}

	private float toFloat(String argument) throws IOException {
		try {
			return Float.parseFloat(argument);
		} catch (NumberFormatException e) {
			throw new IOException("Inkorrektes Float-Argument: " + argument);
		}
	}
}