package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Erik Wagner
 *
 */
public class Collision {

	private HashMap<Vector, Boolean> overlaps = new HashMap<Vector, Boolean>();
	private ArrayList<Vector> blocking = new ArrayList<Vector>();
	private float[] restorings = new float[4];
	private boolean[] collidingSides = new boolean[4];
	private final boolean thisMoveable;

	/**
	 * Erzeugt ein neues Kollisions-Objekt
	 */
	public Collision(boolean thisMoveable) {
		this.thisMoveable = thisMoveable;
	}

	public Collision(Vector overlap, boolean thisMoveable, boolean otherMoveable) {
		this.thisMoveable = thisMoveable;
		this.addOverlap(overlap, otherMoveable);
	}

	private Collision(HashMap<Vector, Boolean> overlaps, boolean thisMoveable) {
		this.thisMoveable = thisMoveable;
		for (Vector overlap : overlaps.keySet()) {
			this.addOverlap(overlap, overlaps.get(overlap));
		}
	}

	public void addOverlap(Vector overlap, boolean otherMoveable) {
		Vector restoring = this.toRestoring(overlap, otherMoveable);
		this.overlaps.put(overlap, otherMoveable);
		this.addRestoring(restoring);
	}

	private Vector toRestoring(Vector overlap, boolean otherMoveable) {
		if (otherMoveable == this.thisMoveable) {
			return overlap.div(-2.0f);
		} else if (otherMoveable) {
			return Vector.ZERO;
		} else {
			return overlap.neg();
		}
	}

	private void addRestoring(Vector restoring) {
		this.blocking.add(restoring);
		this.restorings[0] = Math.min(restoring.y, this.restorings[0]);
		this.restorings[1] = Math.max(restoring.x, this.restorings[1]);
		this.restorings[2] = Math.max(restoring.y, this.restorings[2]);
		this.restorings[3] = Math.min(restoring.x, this.restorings[3]);
		this.addBlockedSide(restoring.neg().toShapeDirection());
	}

	/**
	 * Fügt dieser Kollision eine weitere Kollision hinzu. Dabei wird der
	 * Beweglichkeits-Status der hinzuzufügenden Kollision ignoriert und nur der
	 * Beweglichkeits-Status dieser Kollision berücksichtigt
	 *
	 * @param toAdd
	 *            Die hinzuzufügende Kollision
	 */
	public void addCollision(Collision toAdd) {
		for (Vector overlap : toAdd.overlaps.keySet()) {
			this.addOverlap(overlap, toAdd.overlaps.get(overlap));
		}
	}

	public Vector getRestoring() {
		return new Vector(restorings[1] + restorings[3], restorings[0]
				+ restorings[2]);
	}

	private void addBlockedSide(byte direction) {
		switch (direction) {
		case Shape.UP:
			this.collidingSides[0] = true;
			break;
		case Shape.UP_RIGHT:
			this.collidingSides[0] = true;
			this.collidingSides[1] = true;
			break;
		case Shape.RIGHT:
			this.collidingSides[1] = true;
			break;
		case Shape.DOWN_RIGHT:
			this.collidingSides[1] = true;
			this.collidingSides[2] = true;
			break;
		case Shape.DOWN:
			this.collidingSides[2] = true;
			break;
		case Shape.DOWN_LEFT:
			this.collidingSides[2] = true;
			this.collidingSides[3] = true;
			break;
		case Shape.LEFT:
			this.collidingSides[3] = true;
			break;
		case Shape.UP_LEFT:
			this.collidingSides[3] = true;
			this.collidingSides[0] = true;
			break;
		default:
			break;
		}
	}

	/**
	 * @param direction
	 *            Die Richtung, von diesem Objekt ausgehend
	 * @return Ob in dieser Richtung eine Kollision vorliegt
	 */
	public boolean isBlocked(byte direction) {
		switch (direction) {
		case Shape.UP:
			return this.collidingSides[0];
		case Shape.RIGHT:
			return this.collidingSides[1];
		case Shape.DOWN:
			return this.collidingSides[2];
		case Shape.LEFT:
			return this.collidingSides[3];
		default:
			System.out.println("Fehler bei Kollision, falsche Richtung: "
					+ direction);
			return false;
		}
	}

	/**
	 * Korrigiert die Position eines Shapes nach dieser Kollision
	 *
	 * @param toCorrect
	 *            Das Shape, dessen Position korrigiert werden soll
	 * @return Das korrigierte Shape
	 */
	public Shape correctPosition(Shape toCorrect) {
		Vector restore = this.getRestoring();
		if (!Float.isNaN(restore.x) && !Float.isNaN(restore.y)) {
			return toCorrect.modifyCenter(toCorrect.getCenter().add(restore));
		} else {
			return toCorrect;
		}
	}

	/**
	 * Korrigiert die Richtung (und Länge) eines Vektors gemäß dieser Kollision
	 *
	 * @param toCorrect
	 *            Der Vektor, der korrigiert werden soll
	 * @return Der korrigierte Vektor
	 */
	public Vector correctVector(Vector toCorrect) {
		Vector vec = toCorrect;
		for (int i = 0; i < this.blocking.size(); i++) {
			Vector blocked = this.blocking.get(i);
			if (toCorrect.ang(blocked) > Math.PI / 2.0) {
				blocked = blocked.rotateQuarterClockwise();
				vec = Vector.min(blocked.getDirection().mul(
						toCorrect.abs()
								* (float) Math.cos(toCorrect.ang(blocked))),
						vec);
			}
		}
		return vec;
	}

	/**
	 * Invertiert diese Kollision, d.h. die Overlaps werden umgedreht
	 *
	 * Sollte nur bei einfachen Kollisionen verwendet werden
	 *
	 * @param otherMoveable
	 *            Der Beweglichkeit-Status des Objekts, dem die invertierte
	 *            Kollision zugeordnet wird
	 * @return Die invertierte Kollision
	 */
	public Collision invert(boolean otherMoveable) {
		HashMap<Vector, Boolean> newOverlaps = new HashMap<Vector, Boolean>();
		for (Vector overlap : this.overlaps.keySet()) {
			newOverlaps.put(overlap.neg(), thisMoveable);
		}
		return new Collision(newOverlaps, otherMoveable);
	}

	/**
	 * Gibt diese Kollision in der Konsole aus
	 */
	public void print() {
		System.out.println("Blockierungen:");
		for (Vector vec : this.blocking) {
			System.out.println(vec.toString());
		}
		System.out
				.println("Rückstellvektor: " + this.getRestoring().toString());
		for (boolean b : collidingSides) {
			System.out.println("Blocked: " + b);
		}
	}
}
