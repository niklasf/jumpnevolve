package com.googlecode.jumpnevolve.math;

import java.util.ArrayList;

import org.newdawn.slick.util.Log;

/**
 * @author Erik Wagner
 * 
 */
public class NextCollision {

	private ArrayList<CollisionResult> results = new ArrayList<CollisionResult>();
	private ArrayList<Vector> isIntersects = new ArrayList<Vector>(),
			willIntersects = new ArrayList<Vector>();
	private float[] restoringsIs = new float[4], restoringsWill = new float[4];
	private final boolean thisMoveable;
	private boolean[] collidingSides = new boolean[4];
	private boolean isIntersecting = false, willIntersect = false;

	public NextCollision(boolean thisMoveable) {
		this.thisMoveable = thisMoveable;
	}

	private NextCollision(ArrayList<CollisionResult> results,
			boolean thisMoveable) {
		this.thisMoveable = thisMoveable;
		for (CollisionResult collisionResult : results) {
			this.addCollisionResult(collisionResult);
		}
	}

	public void addCollisionResult(CollisionResult result) {
		this.results.add(result);
		if (result.isIntersecting()) {
			Vector restore = this.toRestoring(result.getIsOverlap(),
					result.isOtherMoveable());
			this.addIsRestoring(restore);
			this.isIntersecting = true;
			this.isIntersects.add(restore);
		}
		if (result.willIntersect()) {
			Vector restore = this.toRestoring(result.getWillOverlap(),
					result.isOtherMoveable());
			this.addWillRestoring(restore);
			this.willIntersect = true;
			this.willIntersects.add(restore);
		}
	}

	private void addWillRestoring(Vector restoring) {
		this.restoringsWill[0] = Math.min(restoring.y, this.restoringsWill[0]);
		this.restoringsWill[1] = Math.max(restoring.x, this.restoringsWill[1]);
		this.restoringsWill[2] = Math.max(restoring.y, this.restoringsWill[2]);
		this.restoringsWill[3] = Math.min(restoring.x, this.restoringsWill[3]);
		this.addBlockedSide(restoring.neg().toShapeDirection());
	}

	private void addIsRestoring(Vector restoring) {
		this.restoringsIs[0] = Math.min(restoring.y, this.restoringsIs[0]);
		this.restoringsIs[1] = Math.max(restoring.x, this.restoringsIs[1]);
		this.restoringsIs[2] = Math.max(restoring.y, this.restoringsIs[2]);
		this.restoringsIs[3] = Math.min(restoring.x, this.restoringsIs[3]);
		this.addBlockedSide(restoring.neg().toShapeDirection());
	}

	private Vector toRestoring(Vector overlap, boolean otherMoveable) {
		if (otherMoveable == this.thisMoveable) {
			return overlap.div(2.0f);
		} else if (otherMoveable) {
			return Vector.ZERO;
		} else {
			return overlap;
		}
	}

	public Vector getIsRestoring() {
		return new Vector(restoringsIs[1] + restoringsIs[3], restoringsIs[0]
				+ restoringsIs[2]);
	}

	public Vector getWillRestoring() {
		return new Vector(restoringsWill[1] + restoringsWill[3],
				restoringsWill[0] + restoringsWill[2]);
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
		case Shape.DOWN_LEFT:
			return this.collidingSides[2] || this.collidingSides[3];
		case Shape.DOWN_RIGHT:
			return this.collidingSides[2] || this.collidingSides[1];
		case Shape.UP_LEFT:
			return this.collidingSides[0] || this.collidingSides[3];
		case Shape.UP_RIGHT:
			return this.collidingSides[0] || this.collidingSides[1];
		default:
			Log.error("Fehler bei Kollision, falsche Richtung: " + direction);
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
	public NextShape correctPosition(NextShape toCorrect) {
		Vector restore = Vector.ZERO;
		if (this.isIntersecting) {
			restore = this.getIsRestoring();
			if (!Float.isNaN(restore.x) && !Float.isNaN(restore.y)) {
				return toCorrect.modifyCenter(toCorrect.getCenter()
						.add(restore));
			}
		}
		return toCorrect;
	}

	/**
	 * Korrigiert die Richtung (und Länge) eines Vektors gemäß dieser Kollision
	 * 
	 * @param toCorrect
	 *            Der Vektor, der korrigiert werden soll
	 * @return Der korrigierte Vektor
	 */
	public Vector correctVelocity(Vector toCorrect) {
		Vector vec = toCorrect;
		if (this.isIntersecting) {
			for (int i = 0; i < this.isIntersects.size(); i++) {
				Vector blocked = this.isIntersects.get(i);
				float ang = toCorrect.ang(blocked);
				if (ang > Math.PI / 2.0) {
					blocked = blocked.rotateQuarterClockwise();
					ang = toCorrect.ang(blocked);
					vec = Vector.min(
							blocked.getDirection().mul(
									toCorrect.abs() * (float) Math.cos(ang)),
							vec);
				}
			}
		}
		return vec;
	}

	/**
	 * Korrigiert die Richtung (und Länge) eines Kraft-Vektors gemäß dieser
	 * Kollision
	 * 
	 * @param toCorrect
	 *            Der Vektor, der korrigiert werden soll
	 * @return Der korrigierte Vektor
	 */
	public Vector correctForce(Vector toCorrect) {
		Vector vec = toCorrect;
		for (CollisionResult result : this.results) {
			Vector blocked;
			if (result.isIntersecting()) {
				blocked = result.getIsOverlap();
			} else if (result.willIntersect()) {
				blocked = result.getWillOverlap();
			} else {
				blocked = toCorrect;
			}
			float ang = toCorrect.ang(blocked);
			if (ang > Math.PI / 2.0) {
				blocked = blocked.rotateQuarterClockwise();
				ang = toCorrect.ang(blocked);
				vec = Vector.min(
						blocked.getDirection().mul(
								toCorrect.abs() * (float) Math.cos(ang)), vec);
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
	public NextCollision invert(boolean otherMoveable) {
		ArrayList<CollisionResult> newResults = new ArrayList<CollisionResult>();
		for (CollisionResult collisionResult : this.results) {
			newResults.add(collisionResult.invert());
		}
		return new NextCollision(newResults, otherMoveable);
	}

	/**
	 * Gibt diese Kollision in der Konsole aus
	 */
	public void print() {
		System.out.println("Results:");
		for (CollisionResult result : this.results) {
			result.print();
		}
	}

	/**
	 * Gibt diese Kollision in der Konsole aus
	 */
	public void print2() {
		System.out.println("IsIntersects:");
		for (Vector is : this.isIntersects) {
			System.out.println("" + is);
		}
		System.out.println("WillIntersects:");
		for (Vector will : this.willIntersects) {
			System.out.println("" + will);
		}
	}
}
