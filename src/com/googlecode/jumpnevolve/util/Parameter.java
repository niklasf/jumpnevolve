package com.googlecode.jumpnevolve.util;

/**
 * Eine Klasse, in der Konstanten für alle anderen Klassen aus Jumpnevolve
 * gesammelt werden, damit sie an einer zentralen Stelle eingesehen und geändert
 * werden können. Gemeint sind nur Konstanten, die einstellbare Werte angeben,
 * wie Größen und Abstände, <b>nicht</b> solche die z.B. nur verschiedene Modi
 * im Code sichtbar kennzeichnen.
 * <p>
 * Die Konstanten sind folgendermaßen zu benennen:
 * <p>
 * LetzterTeilDesPaketNamens_GekürzterKlassenName_NameDerKonstante
 * <p>
 * Beispiel: GUI_BUTTON_DIMENSION
 * <p>
 * Hier wurde Button statt InterfaceButton als gekürzter Klassenname verwendet
 * <p>
 * Zum besseren Verständnis sollten die Konstanten mit einem kurzen Hinweis über
 * ihre Funktion beschrieben werden.
 * 
 * @author Erik Wagner
 * 
 */
public class Parameter {

	/**
	 * Die gewünschte FPS-Rate, die von der Engine angestrebt werden soll
	 */
	public static final int GAME_FPS_TARGET = 100;

	/**
	 * Die minimale FPS-Rate, unter der Frames als Lagg gewertet werden.
	 * <p>
	 * Im Falle eines Lags: Das Level wird für den jeweiligen Frame nicht
	 * weiterberechnet
	 * <p>
	 * Der Wert sollte <strong>deutlich</strong> unterhalb von "GAME_FPS_TARGET"
	 * liegen
	 */
	public static final int GAME_FPS_MINIMUM = 10;

	/**
	 * Die Schwerkraft, die auf Objekte wirkt
	 */
	public static final float GAME_ABSTRACTOBJECT_GRAVITY = 98.1f;

	/**
	 * Die maximale Geschwindigkeit, die ein Objekt erreichen kann
	 * <p>
	 * Wird verwendet um zu verhindern, dass Kollisionen nicht erkannt werden
	 */
	public static final float GAME_ABSTRACTOBJECT_MAXVELOCITY = 300.0f;

	/**
	 * Die Zeit, die nach dem Verlieren des Levels vergeht, bis das Level
	 * neugeladen wird.
	 */
	public static final float GAME_LEVEL_RELOADDELAY = 10.0f;

	/**
	 * Die Größe der Positions-Marker im Editor
	 */
	public static final float EDITOR_POSITIONMARKER_DIMENSION = 5.0f;

	/**
	 * Der Radius der Kreise, die das Rechteck bezeichnen
	 */
	public static final float EDITOR_RECTANGLEDIMENSION_RADIUS = 5.0f;

	/**
	 * Das Level, das beim Starten des Editors geladen wird
	 */
	public static final String EDITOR_EDITOR_DEFAULTLEVEL = "default.txt";

	/**
	 * Die Zeitspanne des Delays, innerhalb dessen keine neuen Objekte im Editor
	 * nach dem Hinzufügen eines Objekts hinzugefügt werden können, in Sekunden
	 */
	public static final float EDITOR_EDITOR_DELAY = 1.0f;

	/**
	 * Die Größe eines Buttons für das Interface
	 */
	public static final float GUI_BUTTON_DIMENSION = 50.0f;

	/**
	 * Die normale Schrittgröße beim Hoch-/Runterzählen der aktuellen Zahl für
	 * die NumberSelection
	 */
	public static final int GUI_NUMBERSELECTION_DEFAULTSTEP = 1;

	/**
	 * Die Größe einer Checkbox für das Interface
	 */
	public static final float GUI_CHECKBOX_SIZE = 20.0f;

	/**
	 * Das Eingabedelay für Textfelder
	 */
	public static final float GUI_TEXTFIELD_DELAY = 0.15f;

	/**
	 * Das Interval für den Nebel-Partikeleffekt
	 */
	public static final int EFFECTS_FOG_INTERVAL = 50;

	/**
	 * Das Interval für die Spray-Partikel
	 */
	public static final int EFFECTS_SPRAY_INTERVAL = 30;

	/**
	 * Die Größe der unbeschriebene Ränder rechts und links am Infoschild
	 */
	public static final float OBJECTS_INFOSIGN_SIDEDIST = 5.0f;

	/**
	 * Der Abstand, der zwischen den Zeilen eines Infoschilds eingefügt wird
	 */
	public static final float OBJECTS_INFOSIGN_LINEDIST = 2.0f;

	/**
	 * Der Zeit nach der der FallingGround zurückgesetzt wird
	 */
	public static final float OBJECTS_FALLINGGROUND_FALLINGTIME = 10.0f;

	/**
	 * Die maximale Distanz, die ein FallingGround fällt
	 */
	public static final float OBJECTS_FALLINGGROUND_FALLINGDIST = 300.0f;
}
