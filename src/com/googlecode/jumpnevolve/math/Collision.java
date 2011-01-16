package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

/**
 * @author Erik Wagner
 * 
 */
public class Collision {

	private ArrayList<Vector> blocking = new ArrayList<Vector>();
	private float[] restorings = new float[4];
	private boolean[] collidingSides = new boolean[4];

	/**
	 * Erzeugt ein neues Kollisions-Objekt
	 */
	public Collision() {
	}

	/**
	 * Fügt dieser Kollision eine elementare Kollision hinzu
	 * 
	 * @param toAdd
	 *            Die elementare Kollision
	 */
	public void addCollision(ElementalCollision toAdd) {
		if (toAdd.getRestoring().equals(0.0f, 0.0f) == false) {
			this.blocking.add(toAdd.getRestoring());
			this.addBlockedSide(toAdd.getRestoring().neg().toShapeDirection());
			this.restorings[0] = Math.min(toAdd.getRestoring().y,
					this.restorings[0]);
			this.restorings[1] = Math.max(toAdd.getRestoring().x,
					this.restorings[1]);
			this.restorings[2] = Math.max(toAdd.getRestoring().y,
					this.restorings[2]);
			this.restorings[3] = Math.min(toAdd.getRestoring().x,
					this.restorings[3]);
		}
	}

	public void addCollision(Collision toAdd) {
		this.blocking.addAll(toAdd.blocking);
		this.restorings[0] = Math.min(toAdd.restorings[0], this.restorings[0]);
		this.restorings[1] = Math.max(toAdd.restorings[1], this.restorings[1]);
		this.restorings[2] = Math.max(toAdd.restorings[2], this.restorings[2]);
		this.restorings[3] = Math.min(toAdd.restorings[3], this.restorings[3]);
		this.collidingSides[0] = this.collidingSides[0]
				|| toAdd.collidingSides[0];
		this.collidingSides[1] = this.collidingSides[1]
				|| toAdd.collidingSides[1];
		this.collidingSides[2] = this.collidingSides[2]
				|| toAdd.collidingSides[2];
		this.collidingSides[3] = this.collidingSides[3]
				|| toAdd.collidingSides[3];
	}

	private Vector getRestoring() {
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
		return toCorrect.modifyCenter(toCorrect.getCenter().add(
				this.getRestoring()));
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
		System.out
				.println("Rückstellvektor: " + this.getRestoring().toString());
		for (boolean b : collidingSides) {
			System.out.println("Blocked: " + b);
		}
	}
}
