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
	 *            Die geblockte Position; werden zwei Seiten geblockt, so ist
	 *            dies die Position der vertikalen Blockung
	 * @param blockedPosition2
	 *            Die geblockte Position, wenn zwei Seiten geblockt werden, dies
	 *            ist die Position der horizontalen Blockung
	 */
	public Collision(byte direction, float blockedPosition1,
			float blockedPosition2) {
		// TODO Kollision initialisieren
		switch (direction) {
		case Shape.OBEN:
			this.collidingSides[0] = true;
			this.collidingPositions[0] = blockedPosition1;
			break;
		case Shape.OBEN_RECHTS:
			this.collidingSides[0] = true;
			this.collidingPositions[0] = blockedPosition1;
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition2;
			break;
		case Shape.RECHTS:
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition1;
			break;
		case Shape.UNTEN_RECHTS:
			this.collidingSides[1] = true;
			this.collidingPositions[1] = blockedPosition2;
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			break;
		case Shape.UNTEN:
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			break;
		case Shape.UNTEN_LINKS:
			this.collidingSides[2] = true;
			this.collidingPositions[2] = blockedPosition1;
			this.collidingSides[3] = true;
			this.collidingPositions[3] = blockedPosition2;
			break;
		case Shape.LINKS:
			this.collidingSides[3] = true;
			this.collidingPositions[3] = blockedPosition1;
			break;
		case Shape.OBEN_LINKS:
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
	 * Erzeugt eine Kollision, bei der nur eine Seite geblockt wird
	 * 
	 * FIXME: ACHTUNG: Wird eine Konstante für die Blockrichtung gewählt, die 2
	 * geblockte Seiten erwartet, wird die zweite Blockung mit 0.0f übernommen
	 * 
	 * @param direction
	 *            Die Kollisionsrichtung, Konstanten aus Shape
	 * @param blockedPosition
	 *            Die geblockte Position
	 */
	public Collision(byte direction, float blockedPosition) {
		this(direction, blockedPosition, 0.0f);
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
		if (other.isBlocked(Shape.OBEN)) {
			if (this.isBlocked(Shape.OBEN)) {
				if (other.getBlockingPosition(Shape.OBEN) > this
						.getBlockingPosition(Shape.OBEN)) {
					this.collidingPositions[0] = other
							.getBlockingPosition(Shape.OBEN);
				}
			} else {
				this.collidingPositions[0] = other
						.getBlockingPosition(Shape.OBEN);
			}
			this.collidingSides[0] = true;
		}
		if (other.isBlocked(Shape.RECHTS)) {
			if (this.isBlocked(Shape.RECHTS)) {
				if (other.getBlockingPosition(Shape.RECHTS) < this
						.getBlockingPosition(Shape.RECHTS)) {
					this.collidingPositions[1] = other
							.getBlockingPosition(Shape.RECHTS);
				}
			} else {
				this.collidingPositions[1] = other
						.getBlockingPosition(Shape.RECHTS);
			}
			this.collidingSides[1] = true;
		}
		if (other.isBlocked(Shape.UNTEN)) {
			if (this.isBlocked(Shape.UNTEN)) {
				if (other.getBlockingPosition(Shape.UNTEN) < this
						.getBlockingPosition(Shape.UNTEN)) {
					this.collidingPositions[2] = other
							.getBlockingPosition(Shape.UNTEN);
				}
			} else {
				this.collidingPositions[2] = other
						.getBlockingPosition(Shape.UNTEN);
			}
			this.collidingSides[2] = true;
		}
		if (other.isBlocked(Shape.LINKS)) {
			if (this.isBlocked(Shape.LINKS)) {
				if (other.getBlockingPosition(Shape.LINKS) > this
						.getBlockingPosition(Shape.LINKS)) {
					this.collidingPositions[3] = other
							.getBlockingPosition(Shape.LINKS);
				}
			} else {
				this.collidingPositions[3] = other
						.getBlockingPosition(Shape.LINKS);
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
		case Shape.OBEN:
			return this.collidingSides[0];
		case Shape.RECHTS:
			return this.collidingSides[1];
		case Shape.UNTEN:
			return this.collidingSides[2];
		case Shape.LINKS:
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
		case Shape.OBEN:
			return this.collidingPositions[0];
		case Shape.RECHTS:
			return this.collidingPositions[1];
		case Shape.UNTEN:
			return this.collidingPositions[2];
		case Shape.LINKS:
			return this.collidingPositions[3];
		default:
			System.out.println("Fehler bei Kollision, falsche Richtung: "
					+ direction);
			return 0.0f;
		}
	}
}
