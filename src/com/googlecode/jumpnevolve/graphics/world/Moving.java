package com.googlecode.jumpnevolve.graphics.world;

import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 *         TODO: boolean canMove() hinzufügen (beispielsweise nur bewegen, wenn
 *         das Objekt auf dem Boden steht)
 * 
 */
public interface Moving extends SimpleObject {

	public static final float MOVE_SOLDIER = 50.0f;
	public static final float MOVE_SLIDING_PLATTFORM = 50.0f;
	public static final float MOVE_ELEVATOR = 50.0f;
	public static final float MOVE_SLIMEWORM = 20.0f;

	/**
	 * Gibt die Geschwindigkeit zurück, mit der sich das Objekt in x-Richtung
	 * bewegen möchte
	 * 
	 * @return >0.0f
	 */
	public float getMovingSpeed();

	/**
	 * Gibt die Richtung zurück, in die sich das Objekt bewegt
	 * 
	 * @return Eine der Konstanten aus {@link Shape} ({@link Shape#NULL}, wenn
	 *         zur Zeit keine Bewegung stattfindet)
	 */
	public Vector getMovingDirection();
}