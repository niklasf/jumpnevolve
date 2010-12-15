/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.graphics.world;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * <p>
 * Ein abstraktes Objekt in einer physikalisch simulierten Welt mit Figur und
 * Masse.
 * </p>
 * 
 * <p>
 * Callbacks können überschrieben werden, haben aber eine
 * Standartimplementierung die in den meisten Fällen auch aufgerufen werden
 * sollte.
 * </p>
 * 
 * @author Erik Wagner
 */
public abstract class AbstractObject implements Pollable, Drawable,
		Serializable {

	private static final long serialVersionUID = -3990787994625166974L;

	/**
	 * Konstante für die maximale Geschwindigkeit in einer Richtung (x bzw. y)
	 */
	private static final float MAXIMUM_VELOCITY_ONE_WAY = 150;

	private Shape shape;

	private Shape oldShape;

	private float mass = 0;

	private World world;

	private Vector velocity = Vector.ZERO;

	private boolean blockable;

	private boolean pushable;

	private boolean living;

	private boolean activable;

	private boolean alive;

	private boolean killable;

	// Attribute pro Runde

	private Vector force;

	private HashSet<AbstractObject> allreadyDone = new HashSet<AbstractObject>();

	private float oldStep;

	/**
	 * Kollision, die die Kollisionen von diesem Objekt wiederspiegelt
	 */

	private Collision collision = new Collision();

	// Methode für die spezifischen Einstellungen pro Runde

	protected abstract void specialSettingsPerRound(Input input);

	// Konstruktoren

	/**
	 * Erzeugt ein neues, bewegliches Objekt.
	 * 
	 * @param world
	 *            Die Welt, zu der das Objekt gehört.
	 * @param shape
	 *            Die Form mit Angaben zur Position des Objekts.
	 * @param mass
	 *            Die Masse des Objekts.
	 * @param blockable
	 *            Ob das Objekt in seiner Bewegung blockierbar ist
	 * @param pushable
	 *            Ob das Objekt durch andere Objekte geschoben werden kann
	 * @param living
	 *            Ob das Objekt lebt
	 * @param activable
	 *            Ob das Objekt ein anderes bestimmtes Objekt aktivieren kann.
	 */
	public AbstractObject(World world, Shape shape, float mass,
			boolean blockable, boolean pushable, boolean living,
			boolean activable, boolean killable) {
		this(world, shape);
		this.mass = mass;
		this.blockable = blockable;
		this.pushable = pushable;
		this.living = living;
		this.activable = activable;
		this.killable = killable;
		if (this.living) {
			this.alive = true;
		}
	}

	/**
	 * Erzeugt ein neues, nicht bewegliches Objekt.
	 * 
	 * @param world
	 *            Die Welt, zu der das Objekt gehört.
	 * @param shape
	 *            Die Form mit Angaben zur Position des Objekts.
	 */
	public AbstractObject(World world, Shape shape) {
		this.world = world;
		this.shape = this.oldShape = shape;
	}

	// Simulationsablauf

	/**
	 * Bereitet eine Simulationsrunde vor, indem die Werte der letzten Runde
	 * aufgeräumt werden.
	 * 
	 * Auch werden Objekt spezifische Einstellungen vorgenommen.
	 * 
	 * @param input
	 *            Das Input-Objekt für die aktuelle Runde
	 */
	public final void startRound(Input input) {
		this.allreadyDone.clear();
		this.addDone(this);
		this.force = Vector.ZERO;
		this.specialSettingsPerRound(input);
		this.collision.clear();
	}

	@Override
	public void poll(Input input, float secounds) {
		for (LinkedList<AbstractObject> neighboursSub : this.world
				.getNeighbours(this)) {
			for (AbstractObject other : neighboursSub) {
				// Nicht mit sich selbst testen
				if (other == this)
					continue;

				// Doppelte Tests vermeiden
				addDone(other);
				other.addDone(this);

				// Kollisionen mit jedem nachbarn prüfen
				if (this.shape.doesCollide(other.getShape())) {
					onCrash(other);
					other.onCrash(this);
				}
			}
		}

		this.oldStep = secounds;
	}

	/**
	 * Schließt eine Simulationsrunde ab, indem die Kraft auf das Objekt
	 * angewendet wird und es bewegt wird.
	 * 
	 * Dabei wird darauf geachtet, nicht in geblockt Seiten zu laufen.
	 */
	public void endRound() {
		// if (this.getPosition().y > 100) {
		// System.out.println("Mist....");
		// }
		if (this.mass != 0.0f) { // Beweglich
			if (this.isWayBlocked(Shape.UP)) {
				if (this.getForce().y < 0) {
					this.force = this.force.modifyY(0);
				}
				if (this.getVelocity().y < 0) {
					this.velocity = this.velocity.modifyY(0);
				}
				if (this.isWayBlocked(Shape.DOWN) == false) {
					this.shape = this.shape
							.modifyCenter(this
									.getPosition()
									.modifyY(
											this.collision
													.getBlockingPosition(Shape.UP)
													+ this.shape
															.getDistanceToSide(Shape.UP)));
				}
			}
			if (this.isWayBlocked(Shape.DOWN)) {
				if (this.getForce().y > 0) {
					this.force = this.force.modifyY(0);
				}
				if (this.getVelocity().y > 0) {
					this.velocity = this.velocity.modifyY(0);
				}
				if (this.isWayBlocked(Shape.UP) == false) {
					this.shape = this.shape
							.modifyCenter(this.shape
									.getCenter()
									.modifyY(
											this.collision
													.getBlockingPosition(Shape.DOWN)
													- this.shape
															.getDistanceToSide(Shape.DOWN)));
				}
			}
			if (this.isWayBlocked(Shape.RIGHT)) {
				if (this.getForce().x > 0) {
					this.force = this.force.modifyX(0);
				}
				if (this.getVelocity().x > 0) {
					this.velocity = this.velocity.modifyX(0);
				}
				if (this.isWayBlocked(Shape.LEFT) == false) {
					this.shape = this.shape
							.modifyCenter(this
									.getPosition()
									.modifyX(
											this.collision
													.getBlockingPosition(Shape.RIGHT)
													- this.shape
															.getDistanceToSide(Shape.RIGHT)));
				}
			}
			if (this.isWayBlocked(Shape.LEFT)) {
				if (this.getForce().x < 0) {
					this.force = this.force.modifyX(0);
				}
				if (this.getVelocity().x < 0) {
					this.velocity = this.velocity.modifyX(0);
				}
				if (this.isWayBlocked(Shape.RIGHT) == false) {
					this.shape = this.shape
							.modifyCenter(this
									.getPosition()
									.modifyX(
											this.collision
													.getBlockingPosition(Shape.LEFT)
													+ this.shape
															.getDistanceToSide(Shape.LEFT)));
				}
			}

			// Neue Geschwindigkeit bestimmen
			Vector acceleration = this.force.div(this.mass);
			Vector deltaVelocity = acceleration.mul(this.oldStep);
			this.velocity = this.velocity.add(deltaVelocity);

			// Begrenze Geschwindigkeit
			if (this.velocity.x > AbstractObject.MAXIMUM_VELOCITY_ONE_WAY) {
				this.velocity = this.velocity
						.modifyX(AbstractObject.MAXIMUM_VELOCITY_ONE_WAY);
			} else if (this.velocity.x < -AbstractObject.MAXIMUM_VELOCITY_ONE_WAY) {
				this.velocity = this.velocity
						.modifyX(-AbstractObject.MAXIMUM_VELOCITY_ONE_WAY);
			}
			if (this.velocity.y > AbstractObject.MAXIMUM_VELOCITY_ONE_WAY) {
				this.velocity = this.velocity
						.modifyY(AbstractObject.MAXIMUM_VELOCITY_ONE_WAY);
			} else if (this.velocity.y < -AbstractObject.MAXIMUM_VELOCITY_ONE_WAY) {
				this.velocity = this.velocity
						.modifyY(-AbstractObject.MAXIMUM_VELOCITY_ONE_WAY);
			}

			// Entsprechend die neue Position berechnen
			Vector newPos = this.shape.getCenter().add(
					this.velocity.mul(this.oldStep));

			// Neue Form bestimmen
			Shape newShape = this.shape.modifyCenter(newPos);
			this.oldShape = this.shape;
			this.shape = newShape;

			// Welt informieren
			// TODO: Automatisch alle n Runden machen
			this.world.changedPosition(this);
		}
	}

	/**
	 * @param other
	 *            Ein Objekt
	 * @return {@code true}, wenn es in diesem Schritt schon behandelt wurde.
	 */
	public final boolean alreadyDone(AbstractObject other) {
		return this.allreadyDone.contains(other);
	}

	/**
	 * @param other
	 *            Objekt, das in diesem Schritt schon behandelt wurde.
	 */
	private void addDone(AbstractObject other) {
		this.allreadyDone.add(other);
	}

	// Interaktionen

	/**
	 * Fügt eine Kraft hinzu, die auf das Objekt wirkt.
	 * 
	 * @param force
	 *            Die Kraft
	 */
	public final void applyForce(Vector force) {
		this.force = this.force.add(force);
	}

	/**
	 * Aktiviert das Objekt
	 * 
	 * @param activator
	 *            Das Objekt, durch welches dieses aktiviert wurde
	 */
	public void activate(AbstractObject activator) {
	}

	/**
	 * Deaktiviert das Objekt
	 * 
	 * @param deactivator
	 *            Das Objekt, durch welches dieses deaktiviert wurde
	 */
	public void deactivate(AbstractObject deactivator) {
	}

	/**
	 * Tötet dieses Objekt, wenn der killer töten kann.
	 * 
	 * In diese Methode gehören Immunitäten des Objekts gegenüber bestimmten
	 * Killern.
	 * 
	 * @param killer
	 *            Das Objekt, das dieses tötet
	 */
	public void kill(AbstractObject killer) {
		if (killer.isKillable()) {
			this.setAlive(false);
		}
	}

	/**
	 * Setzt die Richtung zum "Blocker" als blockiert Wird in
	 * {@link #endRound()} beim Setzen der neuen Position ausgewertet
	 * 
	 * @param blocker
	 *            Das blockende Objekt
	 */
	public void blockWay(AbstractObject blocker) {
		this.collision.addCollision(this.getShape().getCollision(
				blocker.getShape(), blocker.isMoveable(), this.isMoveable()));
	}

	// FIXME: Bitte korrigieren, da hab ich ein Denkfehler gemacht...
	/**
	 * Überträgt Energie in Form eines Geschwindigkeitsvektors auf das Objekt
	 * 
	 * @param energy
	 *            Die übertragene Energie
	 */
	public void giveEnergy(Vector energy) {
	}

	// Attribute holen und setzen

	/**
	 * @return Die Masse des Objekts.
	 */
	public final float getMass() {
		return this.mass;
	}

	/**
	 * @return Aktuelle Geschwindigkeit.
	 */
	public final Vector getVelocity() {
		return this.velocity;
	}

	/**
	 * @return Die Kraft die zur Zeit auf das Objekt wirkt.
	 */
	public final Vector getForce() {
		return this.force;
	}

	/**
	 * @return Die x-Koordinate des linken Endes des Objekts
	 */
	public final float getHorizontalStart() {
		return this.shape.getLeftEnd();
	}

	/**
	 * @return Die x-Koordinate des rechten Endes des Objekts
	 */
	public final float getHorizontalEnd() {
		return this.shape.getRightEnd();
	}

	/**
	 * @return Die x-Koordinate des linken Endes des Objekts vor einer
	 *         Berechnungsrunde
	 */
	public final float getOldHorizontalStart() {
		return this.oldShape.getLeftEnd();
	}

	/**
	 * @return Die x-Koordinate des rechten Endes des Objekts vor einer
	 *         Berechnungsrunde
	 */
	public final float getOldHorizontalEnd() {
		return this.oldShape.getRightEnd();
	}

	/**
	 * @return Die mathematische Figur, die das Objekt beschreibt
	 */
	public final Shape getShape() {
		return this.shape;
	}

	/**
	 * @return Die mathematische Figur, die das Objekt (vor einer
	 *         Berechnungsrunde) beschreibt
	 */
	public final Shape getOldShape() {
		return this.oldShape;
	}

	/**
	 * @return Die aktuelle Position (also der Mittelpunkt) des Objekts.
	 */
	public final Vector getPosition() {
		return this.shape.getCenter();
	}

	/**
	 * @return Die Position des Objekts vor einer Berechnungsrunde.
	 */
	public final Vector getOldPosition() {
		return this.oldShape.getCenter();
	}

	/**
	 * @return Die Welt, in der dieses Objekt exsistiert
	 */
	public final World getWorld() {
		return this.world;
	}

	/**
	 * @return {@code true}, wenn das Objekt beweglich ist.
	 */
	public final boolean isMoveable() {
		return this.mass != 0.0f;
	}

	/**
	 * @return {@code true}, wenn das Objekt blockbar ist.
	 */
	public final boolean isBlockable() {
		return this.blockable;
	}

	/**
	 * @return {@code true}, wenn das Objekt schiebbar ist.
	 */
	public final boolean isPushable() {
		return this.pushable;
	}

	/**
	 * @return {@code true}, wenn das Objekt lebendig ist.
	 */
	public final boolean isLiving() {
		return this.living;
	}

	/**
	 * @return {@code true}, wenn das Objekt aktivierbar ist. Achtung: Nicht
	 *         unbedingt alle Objekte können das Objekt aktivieren
	 */
	public final boolean isActivable() {
		return this.activable;
	}

	/**
	 * @return {@code true}, wenn das Objekt töten kann.
	 */
	public final boolean isKillable() {
		return this.killable;
	}

	/**
	 * @param direction
	 *            Richtung, welche abgefragt wird; bezieht sich auf die
	 *            Richtungs-Konstanten von {@link Shape}
	 * @return {@code true}, wenn der Weg in Richtung von Direction blockiert
	 *         ist
	 */
	public final boolean isWayBlocked(byte direction) {
		return this.collision.isBlocked(direction);
	}

	protected final void setAlive(boolean alive) {
		this.alive = alive;
	}

	public final void setPosition(Vector newPosition) {
		this.shape = this.shape.modifyCenter(newPosition);
	}

	public final boolean isAlive() {
		return this.alive;
	}

	/**
	 * Setzt die aktuelle Geschwindigkeit auf eine gradlienige neue.
	 * 
	 * @param velocity
	 *            Die neue Geschwindigkeit
	 */
	public void setVelocity(Vector velocity) {
		this.force = Vector.ZERO;
		this.velocity = velocity;
	}

	// Standartimplementierung für das Zeichnen

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.shape);
		GraphicUtils.string(g, this.getPosition(), this.toString());
	}

	// Callbacks

	/**
	 * Wird aufgerufen wenn eine Kollision vorliegt.
	 * 
	 * @param other
	 *            Der Kollisionspartner
	 */
	public void onCrash(AbstractObject other) {
		// if (this instanceof Soldier)
		// System.out.println("Crash!" + other.getShape());
		// Spezielle Methoden aufrufen
		// ACHTUNG: Aktualisieren, wenn neue Objekte eingefügt werden
		if (this.blockable) {
			onBlockableCrash(other);
		}
		if (this.pushable) {
			onPushableCrash(other);
		}
		if (this.living) {
			onLivingCrash(other);
		}
		if (this.activable) {
			onActivableCrash(other);
		}
		onGeneralCrash(other);
	}

	/**
	 * Wird aufgerufen, wenn das Objekt auf ein blockbares Objekt trifft.
	 * 
	 * Grundsätzlich wird das andere Objekt (other) geblockt, wenn auch dieses
	 * Objekt (this) blockbar ist.
	 * 
	 * @param other
	 *            Das andere (blockbare) Objekt
	 */
	public void onBlockableCrash(AbstractObject other) {
		if (this.blockable) {
			other.blockWay(this);
		}
	}

	/**
	 * Wird aufgerufen, wenn dieses Objekt auf ein schiebbares Objekt trifft
	 * 
	 * Dem anderen Objekt wird die Energie dieses Objekts übertragen.
	 * 
	 * @param other
	 *            Das andere (schiebbare) Objekt
	 */
	public void onPushableCrash(AbstractObject other) {
		other.giveEnergy(this.getVelocity().getDirection().mul(
				this.getVelocity().abs() * this.getVelocity().abs()
						* (0.5f * this.getMass())));
	}

	/**
	 * Wird aufgerufen, wenn das Objekt auf ein lebendes Objekt trifft.
	 * 
	 * Das andere Objekt wird getötet, wenn dieses Objekt tötungsfähig ist
	 * (Immunitäten beachten).
	 * 
	 * Hierein gehören Eigenschaft des Objekts, die bewirken, dass ein
	 * tötungsfähiges Objekt (this) ein anderes Objekt tötet (other).
	 * 
	 * @param other
	 *            Das andere (lebende) Objekt
	 */
	public void onLivingCrash(AbstractObject other) {
		if (this.isKillable()) {
			other.kill(this);
		}
	}

	/**
	 * Wird aufgerufen wenn dieses Objekt auf ein aktivierbares Objekt trifft.
	 * 
	 * Das andere Objekt wird aktiviert.
	 * 
	 * @param other
	 *            Das andere (aktivierbare) Objekt
	 */
	public void onActivableCrash(AbstractObject other) {
		other.activate(this);
	}

	public void onGeneralCrash(AbstractObject other) {
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
