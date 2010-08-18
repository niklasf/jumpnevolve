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

import java.util.HashSet;
import java.util.LinkedList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.Figure;
import com.googlecode.jumpnevolve.game.VorlageGegner;
import com.googlecode.jumpnevolve.game.VorlageLandschaft;
import com.googlecode.jumpnevolve.game.VorlageObjekte;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * <p>
 * Ein abstraktes Objekt in einer physikalisch simulierten Welt mit Figur
 * und Masse.
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
public class AbstractObject implements Pollable, Drawable {

	// Attribute

	private Shape shape;

	private Shape oldShape;

	private float mass = 0;

	private World world;
	
	private Vector velocity = Vector.ZERO;

	
	// Attribute pro Runde

	private Vector force;

	private HashSet<AbstractObject> allreadyDone = new HashSet<AbstractObject>();

	private float oldStep;


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
	 */
	public AbstractObject(World world, Shape shape, float mass) {
		this(world, shape);
		this.mass = mass;
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
	 */
	public final void startRound() {
		this.allreadyDone.clear();
		this.addDone(this);
		this.force = Vector.ZERO;
	}

	@Override
	public void poll(Input input, float secounds) {
		for (LinkedList<AbstractObject> neighboursSub : this.world
				.getNeighbours(this)) {
			for (AbstractObject other : neighboursSub) {
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
	 */
	public void endRound() {
		if (this.mass != 0.0f) { // Beweglich
			// Bewegungsgleichung lösen:
			// Nicht mit dem Verlet-Algorithmus, da this.oldStep nicht unbedingt
			// konstant ist.
			Vector acceleration = this.force.div(this.mass);
			Vector deltaVelocity = acceleration.mul(this.oldStep * this.oldStep * 0.5f);
			this.velocity = this.velocity.add(deltaVelocity);
			Vector newPos = this.shape.getCenter().add(this.velocity.mul(this.oldStep));
			
			// FIXME: Bewegung in geblockte Richtungen verhindern
			
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
	public void applyForce(Vector force) {
		this.force = this.force.add(force);
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
	 * @return {@code true}, wenn das Objekt beweglich ist.
	 */
	public boolean isMoveable() {
		return this.mass != 0.0f;
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
	}
	

	// Callbacks

	/**
	 * Wird aufgerufen wenn eine Kollision vorliegt.
	 * 
	 * @param other
	 *            Der Kollisionspartner
	 */
	public void onCrash(AbstractObject other) {
		// FIXME: Seite blocken
		
		// Spezielle Methoden aufrufen
		// ACHTUNG: Aktualisieren, wenn neue Objekte eingefügt werden
		if(other instanceof Figure) {
			onPlayerCrash((Figure) other);
		} else if(other instanceof VorlageObjekte) {
			onObjectCrash((VorlageObjekte) other);
		} else if(other instanceof VorlageGegner) {
			onEnemyCrash((VorlageGegner) other);
		} else if(other instanceof VorlageLandschaft) {
			onGroundCrash((VorlageLandschaft) other);
		} else {
			onMiscCrash(other);
		}
	}
	
	public void onPlayerCrash(Figure other) { }
	
	public void onObjectCrash(VorlageObjekte other) { }
	
	public void onEnemyCrash(VorlageGegner other) { }
	
	public void onGroundCrash(VorlageLandschaft other) { }
	
	public void onMiscCrash(AbstractObject other) {	}
	
}
