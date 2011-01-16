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
import com.googlecode.jumpnevolve.math.Kollision;
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
	private static final float MAXIMUM_VELOCITY_ONE_WAY = 300;

	public static final float GRAVITY = 98.1f;

	private Shape shape;

	private Shape oldShape;

	private float mass = 0;

	private World world;

	private Vector velocity = Vector.ZERO;

	private Vector oldVelocity = Vector.ZERO;

	private boolean blockable;

	private boolean alive = true;

	// Attribute pro Runde

	private Vector force;

	private HashSet<AbstractObject> allreadyDone = new HashSet<AbstractObject>();

	private float oldStep;

	/**
	 * Kollision, die die Kollisionen von diesem Objekt wiederspiegelt
	 */

	private Kollision collision = new Kollision();

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
	 */
	public AbstractObject(World world, Shape shape, float mass,
			boolean blockable) {
		this(world, shape);
		this.mass = mass;
		this.blockable = blockable;
	}

	/**
	 * Erzeugt ein neues, bewegliches Objekt mit einer Startgeschwindigkeit
	 * 
	 * @see #AbstractObject(World, Shape, float, boolean)
	 * @param velocity
	 *            Die Start-Geschwindigkeit des Objekts
	 */
	public AbstractObject(World world, Shape shape, float mass,
			boolean blockable, Vector velocity) {
		this(world, shape, mass, blockable);
		this.velocity = velocity;
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
		if (this instanceof Moving) {
			Vector direction = ((Moving) this).getMovingDirection();
			float vel = 0.0f;
			float move = ((Moving) this).getMovingSpeed();
			if (direction.equals(0, 0)) {
				move = 0.0f;
				direction = this.getVelocity().neg();
				vel = -1.0f;
			} else if (this.getVelocity().equals(0, 0) == false) {
				vel = (float) (Math.cos(this.getVelocity().ang(direction)) * this
						.getVelocity().abs());
			}
			float mul = 1.0f;
			if (direction.x * direction.y != 0) {
				mul = 0.707f;
			}
			this.applyForce(direction.mul(mul * (move - vel) * 1.5f
					* this.getMass()));
		}
		if (this instanceof Jumping) {
			if (this.isWayBlocked(Shape.DOWN)) {
				this.velocity = this.velocity.modifyY((float) -Math.sqrt(2.0
						* ((Jumping) this).getJumpingHeight() * GRAVITY));
				// FIXME: Statt 4.0 muss hier laut Formel eigentlich 2.0 stehen,
				// dann springt das Objekt aber nur ca. halb so hoch, wie es
				// soll --> Wo liegt der Fehler?
			}
		}
		if (this instanceof GravityActing) {
			this.applyGravity();
		}
		this.collision = new Kollision();
	}

	@Override
	public void poll(Input input, float secounds) {
		/*
		 * Crashes nur für bewegliche Objekte berechnen, da sich unbewegte
		 * Objekte von selbst nie verändern, sondern nur, wenn überhaupt, durch
		 * andere (bewegbare) Objekte verändert werden können
		 */
		if (this.isMoveable()) {
			for (LinkedList<AbstractObject> neighboursSub : this.world
					.getNeighbours(this)) {
				// Alle Nachbarn durchgehen
				for (AbstractObject other : neighboursSub) {
					// Nicht mit sich selbst testen
					if (other == this)
						continue;
					// Keinen doppelten Test durchführen
					if (this.alreadyDone(other))
						continue;
					// Um doppelte Tests zu vermeiden, bei beiden Objekten als
					// getan kennzeichnen
					addDone(other);
					other.addDone(this);

					// Kollisionen mit dem Nachbarn prüfen
					if (this.shape.doesCollide(other.getShape())) {
						onCrash(other);
						other.onCrash(this);
					}
				}
			}

			this.oldStep = secounds;
		}
	}

	/**
	 * Schließt eine Simulationsrunde ab, indem die Kraft auf das Objekt
	 * angewendet wird und es bewegt wird.
	 * 
	 * Dabei wird darauf geachtet, nicht in geblockte Seiten zu laufen.
	 */
	public void endRound() {
		this.oldVelocity = this.velocity;
		if (this.mass != 0.0f) { // Beweglich
			this.shape = this.collision.correctPosition(this.shape);
			this.force = this.collision.correctVector(this.force);
			this.velocity = this.collision.correctVector(this.velocity);

			// Neue Geschwindigkeit bestimmen
			Vector acceleration = this.force.div(this.mass);
			Vector deltaVelocity = acceleration.mul(this.oldStep);
			this.velocity = this.velocity.add(deltaVelocity);

			// Begrenze die Geschwindigkeit
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

			// Neues Shape bestimmen
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

	private void applyGravity() {
		this.applyForce(Vector.DOWN.mul(GRAVITY * this.mass));
	}

	/**
	 * Setzt die Richtung zum "Blocker" als blockiert Wird in
	 * {@link #endRound()} beim Setzen der neuen Position ausgewertet
	 * 
	 * @param blocker
	 *            Das blockende Objekt
	 */
	public void blockWay(AbstractObject blocker) {
		this.collision.addKollision(this.getShape().getCollision(
				blocker.getShape(), blocker.isMoveable(), this.isMoveable()));
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
	 * @return Die Geschwindigkeit der letzten Runde vor dem Auswerten von Kraft
	 *         und Kollision.
	 */
	public final Vector getOldVelocity() {
		return this.oldVelocity;
	}

	/**
	 * @return Die Kraft die zur Zeit auf das Objekt wirkt.
	 */
	public final Vector getForce() {
		return this.force;
	}

	/**
	 * Gibt die Nummer der Subarea zurück, in der dieses Objekt <b>beginnt</b>
	 * 
	 * @param subareaWidth
	 *            Die Breite einer Subarea
	 * @param maxSubarea
	 *            Die Nummer der letzten Subarea
	 * @return Der zurückgegebene Wert {@code return} genügt immer folgenden
	 *         Bedingungen:</p> {@code return} >= 0 und {@code return} <=
	 *         {@code maxSubarea}
	 */
	public final int getStartSubarea(int subareaWidth, int maxSubarea) {
		int subarea = (int) (this.getHorizontalStart() / subareaWidth);
		if (subarea < 0) {
			subarea = 0;
		}
		if (subarea > maxSubarea) {
			subarea = maxSubarea;
		}
		return subarea;
	}

	/**
	 * Gibt die Nummer der Subarea zurück, in der dieses Objekt <b>endet</b>
	 * 
	 * @param subareaWidth
	 *            Die Breite einer Subarea
	 * @param maxSubarea
	 *            Die Nummer der letzten Subarea
	 * @return Der zurückgegebene Wert {@code return} genügt immer folgenden
	 *         Bedingungen:</p> {@code return} >= 0 und {@code return} <=
	 *         {@code maxSubarea}
	 */
	public final int getEndSubarea(int subareaWidth, int maxSubarea) {
		int subarea = (int) (this.getHorizontalEnd() / subareaWidth);
		if (subarea < 0) {
			subarea = 0;
		}
		if (subarea > maxSubarea) {
			subarea = maxSubarea;
		}
		return subarea;
	}

	/**
	 * Gibt die Nummer der Subarea zurück, in der dieses Objekt letzte Runde
	 * <b>begann</b>
	 * 
	 * @param subareaWidth
	 *            Die Breite einer Subarea
	 * @param maxSubarea
	 *            Die Nummer der letzten Subarea
	 * @return Der zurückgegebene Wert {@code return} genügt immer folgenden
	 *         Bedingungen:</p> {@code return} >= 0 und {@code return} <=
	 *         {@code maxSubarea}
	 */
	public final int getOldStartSubarea(int subareaWidth, int maxSubarea) {
		int subarea = (int) (this.getOldHorizontalStart() / subareaWidth);
		if (subarea < 0) {
			subarea = 0;
		}
		if (subarea > maxSubarea) {
			subarea = maxSubarea;
		}
		return subarea;
	}

	/**
	 * Gibt die Nummer der Subarea zurück, in der dieses Objekt letzte Runde
	 * <b>endete</b>
	 * 
	 * @param subareaWidth
	 *            Die Breite einer Subarea
	 * @param maxSubarea
	 *            Die Nummer der letzten Subarea
	 * @return Der zurückgegebene Wert {@code return} genügt immer folgenden
	 *         Bedingungen:</p> {@code return} >= 0 und {@code return} <=
	 *         {@code maxSubarea}
	 */
	public final int getOldEndSubarea(int subareaWidth, int maxSubarea) {
		int subarea = (int) (this.getOldHorizontalEnd() / subareaWidth);
		if (subarea < 0) {
			subarea = 0;
		}
		if (subarea > maxSubarea) {
			subarea = maxSubarea;
		}
		return subarea;
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
	 * @return Die aktuelle Kollision
	 */
	public final Kollision getCollision() {
		return this.collision;
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
	public boolean isBlockable() {
		return this.blockable;
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

	/**
	 * Ändert die Gestalt des Objekts, nicht aber seine Position
	 * 
	 * @param newShape
	 *            Das Shape, dessen Form übernommen wird
	 */
	public final void setShape(Shape newShape) {
		this.shape = newShape.modifyCenter(this.shape.getCenter());
		System.out.println("Shape gesetzt" + this.shape);
	}

	public final void setCollision(Kollision newCollision) {
		this.collision = newCollision;
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
	@Deprecated
	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	// Standartimplementierung für das Zeichnen

	@Override
	public void draw(Graphics g) {
		GraphicUtils.draw(g, this.shape);
		GraphicUtils.drawString(g, this.getPosition(), this.toString());
	}

	// Callbacks

	/**
	 * Wird aufgerufen wenn eine Kollision vorliegt.
	 * 
	 * @param other
	 *            Der Kollisionspartner
	 */
	public void onCrash(AbstractObject other) {
		// Treffen auf ein Living-Object, wenn dieses ein Damageable ist
		if (this instanceof Damageable && other instanceof Living) {
			// Schaden zufügen, wenn das Damageable dem Living Schaden zufügen
			// will
			Kollision col = new Kollision();
			col.addKollision(this.getShape().getCollision(other.getShape(),
					true, true));
			if (((Damageable) this).wantDamaging((Living) other)
					&& ((Damageable) this).canDamage(col)) {
				((Living) other).damage((Damageable) this);
			}
		}
		// Treffen auf ein Activable, wenn dieses ein Activating ist
		if (this instanceof Activating && other instanceof Activable) {
			if (((Activable) other).isActivated()) {
				// Wenn das Activable aktiviert ist, prüfen, ob es deaktivert
				// werden kann und soll
				if (((Activable) other).isDeactivableBy((Activating) this)
						&& ((Activating) this)
								.wantDeactivate((Activable) other)) {
					((Activable) other).deactivate((Activating) this);
				}
			} else {
				// Wenn das Activable deaktiviert ist, prüfen, ob es aktivert
				// werden kann und soll
				if (((Activable) other).isActivableBy((Activating) this)
						&& ((Activating) this).wantActivate((Activable) other)) {
					((Activable) other).activate((Activating) this);
				}
			}
		}

		// if (this instanceof Soldier)
		// System.out.println("Crash!" + other.getShape());
		// Spezielle Methoden aufrufen
		// ACHTUNG: Aktualisieren, wenn neue Objekte eingefügt werden
		if (other.blockable) {
			onBlockableCrash(other);
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

	public void onGeneralCrash(AbstractObject other) {
	}

	@Override
	public String toString() {
		// Paketnamen abtrennen
		String superString = super.toString();
		return superString.substring(superString.lastIndexOf('.') + 1);
	}
}
