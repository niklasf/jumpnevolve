package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 * 
 */
public class Kollision {

	private ArrayList<Vector> blocking = new ArrayList<Vector>();
	private Vector restoring = Vector.ZERO;
	private boolean[] collidingSides = new boolean[4];

	/**
	 * Erzeugt ein neues Kollisions-Objekt
	 */
	public Kollision() {
	}

	/**
	 * Fügt dieser Kollision eine elementare Kollision hinzu
	 * 
	 * @param other
	 *            Die elementare Kollision
	 */
	public void addKollision(ElementalKollision other) {
		if (other.getRestoring().equals(0.0f, 0.0f) == false) {
			this.blocking.add(other.getRestoring());
			this.restoring = this.restoring.add(other.getRestoring());
			this.addBlockedSide(other.getRestoring().neg().toShapeDirection());
		}
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
		return toCorrect.modifyCenter(toCorrect.getCenter().add(restoring));
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
	 * Gibt diese Kollision in der Konsole aus
	 */
	public void print() {
		System.out.println("Blockierungen:");
		for (Vector vec : this.blocking) {
			System.out.println(vec.toString());
		}
		System.out.println("Rückstellvektor: " + restoring.toString());
		for (boolean b : collidingSides) {
			System.out.println("Blocked: " + b);
		}
	}
}
