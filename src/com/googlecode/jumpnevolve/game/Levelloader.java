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
import com.googlecode.jumpnevolve.game.objects.Ground;
import com.googlecode.jumpnevolve.game.objects.JumpingSoldier;
import com.googlecode.jumpnevolve.game.objects.KillingMachine;
import com.googlecode.jumpnevolve.game.objects.RollingBall;
import com.googlecode.jumpnevolve.game.objects.Soldier;
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
				this.level = new Level(Integer.parseInt(firstLineSplit[1]),
						Integer.parseInt(firstLineSplit[2]), Integer
								.parseInt(firstLineSplit[3]));
				// Einstellungen vornehmen
				//String secondLine = levelFileReader.readLine();
				//String[] secondLineSplit = secondLine.split("_");
				if (firstLineSplit[0].equals("Leveleinstellungen") == false) {
					// FIXME: Fehlermeldung ausgeben und abbrechen
				}
				// FIXME: Einstellungen für das Level vornehmen
				// Beispiele: verfügbare Charaktere, Timer etc.

				// HashMaps für Objekte zum Zwischenspeichern erstellen
				HashMap<String, AbstractObject> activableObjects = new HashMap<String, AbstractObject>();
				ArrayList<ActivatingObject> activatingObjects = new ArrayList<ActivatingObject>();
				ArrayList<String[]> argumtensForActivating = new ArrayList<String[]>();
				ArrayList<AbstractObject> otherObjects = new ArrayList<AbstractObject>();

				String current = levelFileReader.readLine();
				while (current != null) { // Pseudo-Methode ersetzen
					String[] currentSplit = current.split("_");
					String[] currentArguments = currentSplit[2].split(",");
					current = levelFileReader.readLine();
					if (currentSplit[0].equals("WalkingSoldier")) {
						otherObjects.add(new WalkingSoldier(this.level, this
								.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("JumpingSoldier")) {
						otherObjects.add(new JumpingSoldier(this.level, this
								.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("Soldier")) {
						otherObjects.add(new Soldier(this.level, this
								.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("KillingMachine")) {
						otherObjects.add(new KillingMachine(this.level, this
								.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("Button")) {
						activatingObjects.add(new Button(this.level, this
								.toVector(currentSplit[1]), this
								.toFloat(currentArguments[0])));
						String[] argument = new String[currentArguments.length - 1];
						for (int i = 1; i < currentArguments.length; i++) {
							argument[i - 1] = currentArguments[i];
						}
						argumtensForActivating.add(argument);
					} else if (currentSplit[0].equals("Door")) {
						activableObjects.put(currentArguments[0], new Door(
								this.level, this.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("Ground")) {
						otherObjects.add(new Ground(this.level, this
								.toVector(currentSplit[1]), this
								.toVector(currentArguments[0])));
					} else if (currentSplit[0].equals("RollingBall")) {
						otherObjects.add(new RollingBall(this.level, this
								.toVector(currentSplit[1])));
					} else if (currentSplit[0].equals("Elevator")) {
						otherObjects.add(new Elevator(this.level, this
								.toVector(currentSplit[1]), this
								.toVector(currentArguments[0]), this
								.toFloat(currentArguments[1]), this
								.toFloat(currentArguments[2])));
					}
					// TODO: Weitere Klassen einfügen
					// Aktivierenden Objekten, die zu aktivierenden Objekt
					// übergeben
					for (int i = 0; i < activatingObjects.size(); i++) {
						for (int j = 0; j < argumtensForActivating.get(i).length; j++) {
							activatingObjects.get(i).addObjectsToActivate(
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
					for (AbstractObject object : activableObjects.values()) {
						this.level.add(object);
					}
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