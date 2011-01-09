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
import com.googlecode.jumpnevolve.game.objects.Button;
import com.googlecode.jumpnevolve.game.objects.Door;
import com.googlecode.jumpnevolve.game.objects.Elevator;
import com.googlecode.jumpnevolve.game.objects.GreenSlimeWorm;
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.SlidingPlattform;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
import com.googlecode.jumpnevolve.game.objects.Soldier;
import com.googlecode.jumpnevolve.game.objects.WalkingSoldier;
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

	private String source;

	private Level level;

	public Levelloader(String source) {
		this.source = source;
	}

	public void loadLevel() {
		FileInputStream levelFile = null;

		try {
			levelFile = new FileInputStream(this.source);

			if (this.source.toLowerCase().endsWith(".txt")) {
				// Neues Level aus Textdatei erstellen
				BufferedReader levelFileReader = new BufferedReader(
						new InputStreamReader(levelFile));

				// Level durch Kopfzeile erstellen --> Größenordnungen
				String firstLine = levelFileReader.readLine();
				String[] firstLineSplit = firstLine.split("_");
				if (firstLineSplit[0].equals("Leveldimensionen") == false) {
					// FIXME: Fehlermeldung ausgeben und abbrechen
				}
				this.level = new Level(this, Integer
						.parseInt(firstLineSplit[1]), Integer
						.parseInt(firstLineSplit[2]), Integer
						.parseInt(firstLineSplit[3]));
				// Einstellungen vornehmen
				String secondLine = levelFileReader.readLine();
				String[] secondLineSplit = secondLine.split("_");
				if (secondLineSplit[0].equals("Leveleinstellungen") == false) {
					// FIXME: Fehlermeldung ausgeben und abbrechen
				}
				// Zoomeinstellungen
				String[] zoom = secondLineSplit[1].split(",");
				if (zoom.length == 1) {
					this.level.setZoom(this.toFloat(zoom[0]));
				} else if (zoom.length == 2) {
					this.level.setZoom(this.toFloat(zoom[0]), this
							.toFloat(zoom[1]));
				} else {
					// FIXME: Fehlermeldung
				}
				// Hintergrund setzen
				this.level
						.setBackground(secondLineSplit[secondLineSplit.length - 1]);
				// FIXME: Einstellungen für das Level vornehmen
				// Beispiele: verfügbare Charaktere, Timer etc.
				String thirdLine = levelFileReader.readLine();
				String[] thirdLineSplit = thirdLine.split("_");
				if (thirdLineSplit[0].equals("Player") == false) {
					// FIXME: Fehlermeldung ausgeben und abbrechen
				}
				this.level.addPlayer(this.toVector(thirdLineSplit[3]),
						thirdLineSplit[2], thirdLineSplit[1], thirdLineSplit[4]
								.split(","));

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
					String className = currentSplit[0];
					Vector position = this.toVector(currentSplit[1]);
					String name = currentSplit[2];
					String activates = currentSplit[3];
					String arguments = currentSplit[4];
					AbstractObject newObject = null;

					if (className.equals("WalkingSoldier")) {
						newObject = new WalkingSoldier(this.level, position);
					} else if (className.equals("JumpingSoldier")) {
						newObject = new JumpingSoldier(this.level, position);
					} else if (className.equals("Soldier")) {
						newObject = new Soldier(this.level, position);
					} else if (className.equals("KillingMachine")) {
						newObject = new KillingMachine(this.level, position);
					} else if (className.equals("Button")) {
						newObject = new Button(this.level, position, this
								.toFloat(arguments));
						argumtensForActivating.add(activates.split(","));
					} else if (className.equals("Door")) {
						newObject = new Door(this.level, position, this
								.toVector(arguments));
					} else if (className.equals("Ground")) {
						newObject = new Ground(this.level, position, this
								.toVector(arguments));
					} else if (className.equals("Elevator")) {
						String[] curArgus = arguments.split(",");
						newObject = new Elevator(this.level, position, this
								.toVector(curArgus[0]), this
								.toFloat(curArgus[1]), this
								.toFloat(curArgus[2]));
					} else if (className.equals("GreenSlimeWorm")) {
						newObject = new GreenSlimeWorm(this.level, position);
					} else if (className.equals("SlidingPlattform")) {
						String[] curArgus = arguments.split(",");
						newObject = new SlidingPlattform(this.level, position,
								this.toVector(curArgus[0]), this
										.toFloat(curArgus[1]), this
										.toFloat(curArgus[2]));
					}
					if (newObject != null) {
						boolean alreadyPutted = false;
						if (newObject instanceof ActivatingObject) {
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
					// TODO: Weitere Klassen einfügen
					// Aktivierenden Objekten, die zu aktivierenden Objekt
					// übergeben
					current = levelFileReader.readLine();
				}
				for (int i = 0; i < activatingObjects.size(); i++) {
					for (int j = 0; j < argumtensForActivating.get(i).length; j++) {
						activatingObjects.get(i).addActivable(
								activableObjects.get(argumtensForActivating
										.get(i)[j]));
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// Ausgabe: Fehler beim Laden des Levels
				// FIXME: Möglich durch throw new IOException()?
				// FIXME: try catch finally throw etwas überarbeiten.
			}
		} catch (IOException e) {
			// Ausgabe: Fehler beim Laden des Levels
		} catch (DataInputException e) {

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

	private Vector toVector(String koordinate) throws DataInputException {
		// TODO: Vector.parseVector könnte auch direkt verwendet werden.
		try {
			return Vector.parseVector(koordinate);
		} catch (NumberFormatException e) {
			throw new DataInputException(koordinate);
		}
	}

	private float toFloat(String argument) throws DataInputException {
		// TODO: Eigentlich könnte man auch direkt NumberFormatException
		// werfen bzw. nicht extra abfangen
		// bzw. Float.parseFloat direkt im Code verwenden.
		try {
			return Float.parseFloat(argument);
		} catch (NumberFormatException e) {
			throw new DataInputException(argument);
		}
	}
}

// TODO: Wenn diese Klasse weiter nützlich ist, sollte sie eine
// eigene Klassendatei bekommen.
class DataInputException extends Exception {

	private static final long serialVersionUID = 8875770146455595015L;

	private final String wrongInput;

	public DataInputException(String wrongInput) {
		this.wrongInput = wrongInput;
	}

	public String getWrongInput() {
		return this.wrongInput;
	}
}