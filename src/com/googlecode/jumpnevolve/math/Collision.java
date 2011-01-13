package com.googlecode.jumpnevolve.math;

public class Collision {

	private boolean[] collidingSides = new boolean[4];

	private float[] collidingPositions = new float[4];

	/**
	 * Eine leere Kollision
	 */
	public Collision() {
	}

	/**
	 * Erzeugt eine Kollision nach der Blockungsrichtung, es können auch zwei
	 * Seiten geblockt werden
	 * 
	 * @param direction
	 *            Die Kollisionsrichtung, Konstanten aus Shape
	 * @param blockedPosition1
	 *            Die vertikale geblockte Position
	 * @param blockedPosition2
	 *            Die horizontale geblockte Position
	 */
	public Collision(byte direction, float blockedPosition1,
			float blockedPosition2) {
		// TODO Kollision initialisieren
		switch (direction) {
		case Shape.UP:
			this.collidingSides[0] = true;
			this.collidingPositions[0] = blockedPosition1;
			break;
		case Shape.UP_RIGHT:
			this.collidingSides[0] = true;
			this.collidingPositions[0] = blockedPosition1;
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition2;
			break;
		case Shape.RIGHT:
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition2;
			break;
		case Shape.DOWN_RIGHT:
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition2;
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			break;
		case Shape.DOWN:
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			break;
		case Shape.DOWN_LEFT:
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			this.collidingSides[3] = true;
			this.collidingPositions[3] = blockedPosition2;
			break;
		case Shape.LEFT:
			this.collidingSides[3] = true;
			this.collidingPositions[3] = blockedPosition2;
			break;
		case Shape.UP_LEFT:
			this.collidingSides[3] = true;
			this.collidingPositions[3] = blockedPosition2;
			this.collidingSides[0] = true;
			this.collidingPositions[0] = blockedPosition1;
			break;
		default:
			break;
		}
	}

	/**
	 * Fügt zwei Kollisionen zusammen, sodass diese Kollision, andere
	 * Kollisionsrichtung der anderen übernimmt
	 * 
	 * Außerdem werden nähere Objekte als geblockte Positionen übernommen
	 * 
	 * @param other
	 *            Die andere Kollision
	 */
	public void addCollision(Collision other) {
		if (other.isBlocked(Shape.UP)) {
			if (this.isBlocked(Shape.UP)) {
				if (other.getBlockingPosition(Shape.UP) > this
						.getBlockingPosition(Shape.UP)) {
					this.collidingPositions[0] = other
							.getBlockingPosition(Shape.UP);
				}
			} else {
				this.collidingPositions[0] = other
						.getBlockingPosition(Shape.UP);
			}
			this.collidingSides[0] = true;
		}
		if (other.isBlocked(Shape.RIGHT)) {
			if (this.isBlocked(Shape.RIGHT)) {
				if (other.getBlockingPosition(Shape.RIGHT) < this
						.getBlockingPosition(Shape.RIGHT)) {
					this.collidingPositions[1] = other
							.getBlockingPosition(Shape.RIGHT);
				}
			} else {
				this.collidingPositions[1] = other
						.getBlockingPosition(Shape.RIGHT);
			}
			this.collidingSides[1] = true;
		}
		if (other.isBlocked(Shape.DOWN)) {
			if (this.isBlocked(Shape.DOWN)) {
				if (other.getBlockingPosition(Shape.DOWN) < this
						.getBlockingPosition(Shape.DOWN)) {
					this.collidingPositions[2] = other
							.getBlockingPosition(Shape.DOWN);
				}
			} else {
				this.collidingPositions[2] = other
						.getBlockingPosition(Shape.DOWN);
			}
			this.collidingSides[2] = true;
		}
		if (other.isBlocked(Shape.LEFT)) {
			if (this.isBlocked(Shape.LEFT)) {
				if (other.getBlockingPosition(Shape.LEFT) > this
						.getBlockingPosition(Shape.LEFT)) {
					this.collidingPositions[3] = other
							.getBlockingPosition(Shape.LEFT);
				}
			} else {
				this.collidingPositions[3] = other
						.getBlockingPosition(Shape.LEFT);
			}
			this.collidingSides[3] = true;
		}
	}

	/**
	 * Löscht alle Eintragungen der Kollision
	 */
	public void clear() {
		this.collidingSides = new boolean[4];
		this.collidingPositions = new float[4];
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
	 * @param direction
	 *            Die Richtung, von diesem Objekt ausgehend
	 * @return Wo in dieser Richtung eine Kollision vorliegt, die Position, auf
	 *         die das Objekt zurückgesetzt werden muss (Seite des Objekts)
	 */
	public float getBlockingPosition(byte direction) {
		switch (direction) {
		case Shape.UP:
			return this.collidingPositions[0];
		case Shape.RIGHT:
			return this.collidingPositions[1];
		case Shape.DOWN:
			return this.collidingPositions[2];
		case Shape.LEFT:
			return this.collidingPositions[3];
		default:
			System.out.println("Fehler bei Kollision, falsche Richtung: "
					+ direction);
			return 0.0f;
		}
	}

	/**
	 * Gibt eine invertierte Kollision zurück (RECHTS/LINKS bzw. OBEN/UNTEN
	 * vertauscht)
	 * 
	 * @return Die invertierte Kollision
	 */
	public Collision getInvertedCollision() {
		Collision inverted = new Collision();
		if (this.isBlocked(Shape.UP)) {
			inverted.addCollision(new Collision(Shape.DOWN, this
					.getBlockingPosition(Shape.UP), 0));
		}
		if (this.isBlocked(Shape.DOWN)) {
			inverted.addCollision(new Collision(Shape.UP, this
					.getBlockingPosition(Shape.DOWN), 0));
		}
		if (this.isBlocked(Shape.RIGHT)) {
			inverted.addCollision(new Collision(Shape.LEFT, 0, this
					.getBlockingPosition(Shape.RIGHT)));
		}
		if (this.isBlocked(Shape.LEFT)) {
			inverted.addCollision(new Collision(Shape.RIGHT, 0, this
					.getBlockingPosition(Shape.LEFT)));
		}
		return inverted;
	}

	public Vector correctVector(Vector vec) {
		if (this.isBlocked(Shape.UP)) {
			if (vec.y < 0) {
				vec = vec.modifyY(0);
			}
		}
		if (this.isBlocked(Shape.DOWN)) {
			if (vec.y > 0) {
				vec = vec.modifyY(0);
			}
		}
		if (this.isBlocked(Shape.RIGHT)) {
			if (vec.x > 0) {
				vec = vec.modifyX(0);
			}
		}
		if (this.isBlocked(Shape.LEFT)) {
			if (vec.x < 0) {
				vec = vec.modifyX(0);
			}
		}
		return vec;
	}

	public Shape correctPosition(Shape shape) {
		if (shape instanceof Rectangle) {
			return this.correctRectanglePosition(shape);
		} else if (shape instanceof Circle) {
			return this.correctCirclePosition((Circle) shape);
		} else {
			return this.correctCirclePosition(shape.getBestCircle());
		}
	}

	private Shape correctRectanglePosition(Shape shape) {
		if (this.isBlocked(Shape.UP)) {
			if (this.isBlocked(Shape.DOWN) == false) {
				shape = shape.modifyCenter(shape.getCenter().modifyY(
						this.getBlockingPosition(Shape.UP)
								+ shape.getDistanceToSide(Shape.UP)));
			}
		}
		if (this.isBlocked(Shape.DOWN)) {
			if (this.isBlocked(Shape.UP) == false) {
				shape = shape.modifyCenter(shape.getCenter().modifyY(
						this.getBlockingPosition(Shape.DOWN)
								- shape.getDistanceToSide(Shape.DOWN)));
			}
		}
		if (this.isBlocked(Shape.RIGHT)) {
			if (this.isBlocked(Shape.LEFT) == false) {
				shape = shape.modifyCenter(shape.getCenter().modifyX(
						this.getBlockingPosition(Shape.RIGHT)
								- shape.getDistanceToSide(Shape.RIGHT)));
			}
		}
		if (this.isBlocked(Shape.LEFT)) {
			if (this.isBlocked(Shape.RIGHT) == false) {
				shape = shape.modifyCenter(shape.getCenter().modifyX(
						this.getBlockingPosition(Shape.LEFT)
								+ shape.getDistanceToSide(Shape.LEFT)));
			}
		}
		return shape;
	}

	private Shape correctCirclePosition(Circle shape) {
		Vector correct = shape.getCenter();
		if (this.isBlocked(Shape.UP)) {
			if (this.isBlocked(Shape.DOWN) == false) {
				correct = correct.modifyY(this.getBlockingPosition(Shape.UP));
			}
		}
		if (this.isBlocked(Shape.DOWN)) {
			if (this.isBlocked(Shape.UP) == false) {
				correct = correct.modifyY(this.getBlockingPosition(Shape.DOWN));
			}
		}
		if (this.isBlocked(Shape.RIGHT)) {
			if (this.isBlocked(Shape.LEFT) == false) {
				correct = correct
						.modifyX(this.getBlockingPosition(Shape.RIGHT));
			}
		}
		if (this.isBlocked(Shape.LEFT)) {
			if (this.isBlocked(Shape.RIGHT) == false) {
				correct = correct.modifyX(this.getBlockingPosition(Shape.LEFT));
			}
		}
		if (correct.equals(shape.getCenter())) {
			return shape;
		} else {
			Vector dir = correct.sub(shape.getCenter());
			Vector diff = dir.mul(shape.radius / dir.abs() - 1);
			Vector newPos = shape.getCenter().sub(diff);
			return shape.modifyCenter(newPos);
		}
	}

	/**
	 * Gibt die Werte der Kollision in der Konsole aus
	 */
	public void print() {
		System.out.println("Name der Kollision: "
				+ this.toString().substring(
						this.toString().lastIndexOf(".") + 1));
		System.out.println("Oben: " + this.collidingSides[0]
				+ " an der Stelle " + this.collidingPositions[0]);
		System.out.println("Rechts: " + this.collidingSides[1]
				+ " an der Stelle " + this.collidingPositions[1]);
		System.out.println("Unten: " + this.collidingSides[2]
				+ " an der Stelle " + this.collidingPositions[2]);
		System.out.println("Links: " + this.collidingSides[3]
				+ " an der Stelle " + this.collidingPositions[3]);
	}
}
