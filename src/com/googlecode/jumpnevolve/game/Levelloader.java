package com.googlecode.jumpnevolve.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipFile;

import org.newdawn.slick.util.DefaultLogSystem;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.game.objects.ActivatingObject;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.JarHandler;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * Zum Laden von Leveln.
 * 
 * @author Erik Wagner
 * 
 */
public class Levelloader {

	public final String source;

	private Level level = null;

	public Levelloader(String source) {
		this.source = source;
	}

	/**
	 * Lädt das Level, das diesem Levelloader zugeordnet ist
	 */
	private void loadLevel() {
		InputStream levelFile = null;

		try {
			// Datei als InputStream vorbereiten
			if (JarHandler.existJar()
					&& !this.source
							.startsWith(Parameter.PROGRAMM_DIRECTORY_LEVELS)) {
				// Aus Jar-Archiv laden
				levelFile = this.getClass().getResourceAsStream(this.source);
			} else {
				if (this.source.contains("!")
						&& this.source
								.startsWith(Parameter.PROGRAMM_DIRECTORY_CAMPAIGNS)) {
					// Aus Zip-Archiv einer Kampagne laden
					ZipFile zip = new ZipFile(this.source.substring(0,
							this.source.indexOf("!")));
					levelFile = zip.getInputStream(zip.getEntry(this.source
							.substring(this.source.indexOf("!") + 1)));
				} else {
					// Nach dem direkten Pfad laden
					levelFile = new FileInputStream(this.source);
				}
			}

			// Einordnen, was für eine Datei geladen wird
			if (this.source.toLowerCase().endsWith(".txt")) {
				Log.info("Lade .txt-Level aus " + this.source);

				// BufferedReader zum Laden der Datei erstellen
				BufferedReader levelFileReader = new BufferedReader(
						new InputStreamReader(levelFile));

				// Level als .txt laden
				this.loadTxtLevel(levelFileReader);

			} else if (this.source.toLowerCase().endsWith(".lvl")) {
				Log.info("Lade .lvl-Level aus " + this.source);

				// BufferedReader zum Laden der Datei erstellen
				BufferedReader levelFileReader = new BufferedReader(
						new InputStreamReader(levelFile));

				// Level als .lvl laden
				this.loadLvlLevel(levelFileReader);

			} else if (this.source.toLowerCase().endsWith(".dat")) {
				Log.info("Lade Speicherstand aus " + this.source);

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
				// Fehler beim Erkennen der Dateiart
				throw new IOException("Nicht-verarbeitbare Dateiendung: "
						+ this.source);
			}
		} catch (IOException e) {
			// Allgemeinen Fehler ausgeben
			Log.error("Fehler beim Laden des Levels:\n" + e.getMessage());
			e.printStackTrace(DefaultLogSystem.out);
		} finally {
			if (levelFile != null) {
				try {
					levelFile.close();
				} catch (IOException e) {
					// Fehler beim Schließen des Streams ausgeben
					Log.error(e);
					e.printStackTrace(DefaultLogSystem.out);
				}
			}
		}
	}

	/**
	 * Lädt ein Level anhand der Syntax eines ".lvl"-Levels
	 * 
	 * @param levelFileReader
	 *            Der BufferedReader der entsprechenden Datei
	 * @throws IOException
	 *             Wenn ein Fehler in der Datei vorliegt
	 */
	private void loadLvlLevel(BufferedReader levelFileReader)
			throws IOException {
		// Notizen für das neue Levelformat

		// Erste Zeile: Maße des Levels
		// Zweite Zeile: Zoom, Timer, Hintergrund
		// Dritte Zeile: Player
		// Folgende Zeilen: zusätzliche Informationen für Level/Player
		// Aufbau: "Setting"_ArtDerInformation_EigentlicheInformation
		// ODER: Objekte des Levels
		// Aufbau: "Object"_ObjektZeileWieInTxt

		// Im Prozedere entscheiden nach Object oder Information

		// TODO: Lade-Prozedur für neues Levelformat erstellen
		// throw new
		// IOException(".lvl-Dateien können noch nicht geladen werden");
		// Erste Zeile: Maße des Levels (Breite, Höhe, Subareabreite

		String firstLine = levelFileReader.readLine();
		String[] firstLineSplit = firstLine.split("_");
		if (firstLineSplit[0].equals("Leveldimensionen") == false
				|| firstLineSplit.length != 4) {
			throw new IOException(
					"Erste Zeile enthält nicht die Leveldimensionen");
		}

		// Level erstellen
		this.level = new Level(this, Integer.parseInt(firstLineSplit[1]),
				Integer.parseInt(firstLineSplit[2]),
				Integer.parseInt(firstLineSplit[3]));

		// Zweite Zeile: Leveleinstellungen (Zoom, Zeit, Hintergrund)
		String secondLine = levelFileReader.readLine();
		String[] secondLineSplit = secondLine.split("_");
		if (secondLineSplit[0].equals("Leveleinstellungen") == false
				|| secondLineSplit.length != 4) {
			throw new IOException(
					"Zweite Zeile enthält nicht die Leveleinstellungen");
		}

		// Zoomeinstellungen
		String[] zoom = secondLineSplit[1].split(",");
		if (zoom.length == 1) {
			try {
				this.level.setZoom(this.toFloat(zoom[0]));
			} catch (Exception e) {
				throw new IOException(
						"Fehler beim Laden des Zooms (1 Zoom-Argument)", e);
			}
		} else if (zoom.length == 2) {
			try {
				this.level
						.setZoom(this.toFloat(zoom[0]), this.toFloat(zoom[1]));
			} catch (Exception e) {
				throw new IOException(
						"Fehler beim Laden des Zooms (2 Zoom-Argumente)", e);
			}
		} else {
			throw new IOException("Die Zoomangaben sind unzulässig");
		}

		// Hintergrund setzen
		this.level.setBackground(secondLineSplit[3]);

		// Timer einstellen
		try {
			this.level.setTime(Float.parseFloat(secondLineSplit[2]));
		} catch (Exception e) {
			throw new IOException("Fehler beim Einstellen des Timers", e);
		}

		// Dritte Zeile: Spielereinstellungen (Startfigur, verfügbare
		// Spielfiguren, Position)
		String thirdLine = levelFileReader.readLine();
		String[] thirdLineSplit = thirdLine.split("_");
		if (thirdLineSplit[0].equals("Player") == false
				|| thirdLineSplit.length != 4) {
			throw new IOException(
					"Dritte Zeile enthält nicht die Playerangaben");
		}

		// Spieler erstellen
		try {
			this.level.addPlayer(this.toVector(thirdLineSplit[3]),
					thirdLineSplit[2], thirdLineSplit[1]);
		} catch (Exception e) {
			throw new IOException("Fehler beim Erstellen des Player-Objekts", e);
		}

		// HashMaps für Objekte zum Zwischenspeichern erstellen
		HashMap<String, Activable> activableObjects = new HashMap<String, Activable>();
		ArrayList<ActivatingObject> activatingObjects = new ArrayList<ActivatingObject>();
		ArrayList<String[]> argumtensForActivating = new ArrayList<String[]>();
		ArrayList<AbstractObject> otherObjects = new ArrayList<AbstractObject>();

		// Objekte und Informationen laden
		// Aufbau der Objekt-Zeilen:
		// "Object"_Klassenname_Koordinate_NameDesObjekts_NamenDerZuAktivierendenObjekte_Argumente

		String current = levelFileReader.readLine();

		while (current != null) {
			if (current.startsWith("Object")) { // Objekt laden
				try {
					String[] currentSplit = current.split("_");
					String name = currentSplit[3];
					String activates = currentSplit[4];
					AbstractObject newObject = null;
					newObject = GameObjects.loadObject(
							current.substring(current.indexOf("_") + 1),
							this.level);

					if (newObject != null) {
						boolean alreadyPutted = false;
						if (newObject instanceof ActivatingObject) {
							argumtensForActivating.add(activates.split(","));
							activatingObjects.add((ActivatingObject) newObject);
							alreadyPutted = true;
						}
						if (newObject instanceof Activable) {
							activableObjects.put(name.toLowerCase(),
									(Activable) newObject);
							alreadyPutted = true;
						}
						if (alreadyPutted == false) {
							otherObjects.add(newObject);
						}
					}
				} catch (Exception e) {
					throw new IOException("Fehler beim Laden eines Objekts: "
							+ current + " Fehlermeldung: " + e);
				}
			} else if (current.startsWith("Setting")) { // Setting verarbeiten
				try {
					String[] currentSplit = current.split("_");
					String nameOfAttribute = currentSplit[1];
					String content = currentSplit[2];

					if (nameOfAttribute.equals("LevelName")) {
						// TODO: Name des Levels setzen
					} else if (nameOfAttribute.equals("IntroText")) {
						// TODO: Introtext erstellen und setzen
					}
				} catch (Exception e) {
					throw new IOException("Fehler beim Laden eines Settings: "
							+ current + " Fehlermeldung: " + e);
				}
			} else {
				throw new IOException(
						"Fehlerhafte Zeile in der Leveldatei (konnte keine Art (Object/Setting) zugewiesen werden: "
								+ current);
			}

			// Nächste Zeile laden
			current = levelFileReader.readLine();
		}

		// Zuweisen der zu aktivierenden Objekte
		for (int i = 0; i < activatingObjects.size(); i++) {
			ActivatingObject activating = activatingObjects.get(i);
			String[] arguments = argumtensForActivating.get(i);
			for (int j = 0; j < argumtensForActivating.get(i).length; j++) {
				Activable activable = activableObjects.get(arguments[j]
						.toLowerCase());
				if (activable != null) {
					activating.addActivable(activable);
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
	}

	/**
	 * Lädt ein Level anhand der Syntax eines ".txt"-Levels
	 * 
	 * @param levelFileReader
	 *            Der BufferedReader der entsprechenden Datei
	 * @throws IOException
	 *             Wenn ein Fehler in der Datei vorliegt
	 */
	private void loadTxtLevel(BufferedReader levelFileReader)
			throws IOException {

		// Erste Zeile: Maße des Levels (Breite, Höhe, Subareabreite
		String firstLine = levelFileReader.readLine();
		String[] firstLineSplit = firstLine.split("_");
		if (firstLineSplit[0].equals("Leveldimensionen") == false
				|| firstLineSplit.length != 4) {
			throw new IOException(
					"Erste Zeile enthält nicht die Leveldimensionen");
		}

		// Level erstellen
		this.level = new Level(this, Integer.parseInt(firstLineSplit[1]),
				Integer.parseInt(firstLineSplit[2]),
				Integer.parseInt(firstLineSplit[3]));

		// Zweite Zeile: Leveleinstellungen (Zoom, Zeit, Hintergrund)
		String secondLine = levelFileReader.readLine();
		String[] secondLineSplit = secondLine.split("_");
		if (secondLineSplit[0].equals("Leveleinstellungen") == false
				|| secondLineSplit.length != 4) {
			throw new IOException(
					"Zweite Zeile enthält nicht die Leveleinstellungen");
		}

		// Zoomeinstellungen
		String[] zoom = secondLineSplit[1].split(",");
		if (zoom.length == 1) {
			try {
				this.level.setZoom(this.toFloat(zoom[0]));
			} catch (Exception e) {
				throw new IOException(
						"Fehler beim Laden des Zooms (1 Zoom-Argument)", e);
			}
		} else if (zoom.length == 2) {
			try {
				this.level
						.setZoom(this.toFloat(zoom[0]), this.toFloat(zoom[1]));
			} catch (Exception e) {
				throw new IOException(
						"Fehler beim Laden des Zooms (2 Zoom-Argumente)", e);
			}
		} else {
			throw new IOException("Die Zoomangaben sind unzulässig");
		}

		// Hintergrund setzen
		this.level.setBackground(secondLineSplit[3]);

		// Timer einstellen
		try {
			this.level.setTime(Float.parseFloat(secondLineSplit[2]));
		} catch (Exception e) {
			throw new IOException("Fehler beim Einstellen des Timers", e);
		}

		// Dritte Zeile: Spielereinstellungen (Startfigur, verfügbare
		// Spielfiguren, Position)
		String thirdLine = levelFileReader.readLine();
		String[] thirdLineSplit = thirdLine.split("_");
		if (thirdLineSplit[0].equals("Player") == false
				|| thirdLineSplit.length != 4) {
			throw new IOException(
					"Dritte Zeile enthält nicht die Playerangaben");
		}

		// Spieler erstellen
		try {
			this.level.addPlayer(this.toVector(thirdLineSplit[3]),
					thirdLineSplit[2], thirdLineSplit[1]);
		} catch (Exception e) {
			throw new IOException("Fehler beim Erstellen des Player-Objekts", e);
		}

		// HashMaps für Objekte zum Zwischenspeichern erstellen
		HashMap<String, Activable> activableObjects = new HashMap<String, Activable>();
		ArrayList<ActivatingObject> activatingObjects = new ArrayList<ActivatingObject>();
		ArrayList<String[]> argumtensForActivating = new ArrayList<String[]>();
		ArrayList<AbstractObject> otherObjects = new ArrayList<AbstractObject>();

		// Objekte laden
		// Aufbau der Zeilen:
		// Klassenname_Koordinate_NameDesObjekts_NamenDerZuAktivierendenObjekte_Argumente

		String current = levelFileReader.readLine();

		while (current != null) {
			try {
				// Objekt laden
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
						activableObjects.put(name.toLowerCase(),
								(Activable) newObject);
						alreadyPutted = true;
					}
					if (alreadyPutted == false) {
						otherObjects.add(newObject);
					}
				}
			} catch (Exception e) {
				throw new IOException("Fehler beim Laden des Objekts: "
						+ current + " Fehlermeldung: " + e);
			}

			// Nächste Zeile lesen
			current = levelFileReader.readLine();
		}

		// Zuweisen der zu aktivierenden Objekte
		for (int i = 0; i < activatingObjects.size(); i++) {
			ActivatingObject activating = activatingObjects.get(i);
			String[] arguments = argumtensForActivating.get(i);
			for (int j = 0; j < argumtensForActivating.get(i).length; j++) {
				Activable activable = activableObjects.get(arguments[j]
						.toLowerCase());
				if (activable != null) {
					activating.addActivable(activable);
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
	}

	/**
	 * @return Das Level, dass dieser Levelloader laden soll
	 */
	public Level getLevel() {
		if (this.level == null) {
			this.loadLevel();
		}
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