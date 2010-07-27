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

import java.util.HashMap;

import com.googlecode.jumpnevolve.game.*;
import com.googlecode.jumpnevolve.graphics.Pollable;
import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author niklas TODO: Java-Doc kontrollieren und vervollständigen
 */
public abstract class AbstractObject implements Pollable, Drawable {
	/*
	 * Methoden die definieren, wie die Attribute des Objekts, das das aktuelle
	 * Objekt gecrasht hat verändert werden z.B.: schneller, langsamer,
	 * zusätzliche Kraft, Tod
	 */
	protected abstract void crashedByPlayer(Figure player);

	protected abstract void crashedByObjekt(VorlageObjekte objekt);

	protected abstract void crashedByEnemy(VorlageGegner gegner);

	protected abstract void crashedByGround(VorlageLandschaft ground);

	/*
	 * Gibt den Status des Objekts zurück: Bei Gegnern und Figur den
	 * Alive-Status, bei Objekten, ob aktiviert oder nicht, bei Landschaft immer
	 * true
	 * 
	 * soll dazu benutzt werden, ob das Objekt berechnet werden muss oder nicht
	 * (ob es als Hndernis fungieren könnte)
	 */
	public abstract boolean getState();

	Vector position;
	Vector oldPosition;
	Vector force;
	Vector dimension;
	final byte type;
	public static final byte TYPE_BALL = 0;
	public static final byte TYPE_BOX = 1;
	private static int Id = 0;
	public final int id;

	/*
	 * HashMap um abzuspeichern, welche Objekte schon behnadelt wurden, dadurch
	 * brauchen 2 Objekte nur einmal auf einen Crash zuprüfen; Der boolean-Wert
	 * speichert den Zustand der Kollision (Ja oder Nein)
	 */
	private HashMap<Integer, Boolean> alreadyDone;

	protected AbstractObject(byte type, Vector position, Vector dimension,
			Vector force) {
		this.type = type;
		this.position = position;
		this.oldPosition = position;
		this.dimension = dimension;
		this.force = force;
		this.id = Id++;
	}

	protected AbstractObject(byte type, Vector position, Vector dimension) {
		this.type = type;
		this.position = position;
		this.oldPosition = position;
		this.dimension = dimension;
		this.force = Vector.ZERO;
		this.id = Id++;
	}

	/**
	 * Gibt zurück, ob die Kollision der Objekte schon berechnet wurde
	 * 
	 * @param other
	 *            Das Objekt, bei dem geprüft werden soll, ob es mit diesem
	 *            kollidiert
	 * @return Ob es schon berechnet wurde
	 */
	public boolean alreadyDone(AbstractObject other) {
		return this.alreadyDone.containsKey(other.getID());
	}

	private void addDone(AbstractObject other, boolean state) {
		this.alreadyDone.put(other.getID(), state);
	}

	/**
	 * Leert die "Schon berechnet"-HashMap, damit die Kollisionen neu berechnet
	 * werden können
	 */
	public void newCalculationRound() {
		this.alreadyDone.clear();
	}

	/**
	 * @return ID des AbstractObjects
	 */
	public final int getID() {
		return this.id;
	}

	public final float getMass() {
		return 0;
	}

	/**
	 * @return Aktuelle Geschwindigkeit anhand der alten Position
	 */
	public final Vector getVelocity() {
		return this.position.sub(this.oldPosition);
	}

	public final Vector getForce() {
		return this.force;
	}

	public byte getType() {
		return this.type;
	}

	public final Vector getDimension() {
		return this.dimension;
	}

	public void applyForce(Vector force) {
		this.force = this.force.add(force);
	}

	public void finalizeStep(boolean undo) {

	}

	public final Vector getPosition() {
		return this.position;
	}

	public boolean isStatic() {
		return false;
	}

	/**
	 * Setzt die aktuelle Geschwindigkeit auf eine geradlinige neue
	 * Geschwindigkeit
	 * 
	 * @param velocity
	 *            neue Geschwindigkeit
	 */
	public void setVelocity(Vector velocity) {
		this.force = Vector.ZERO;
		this.oldPosition = this.position.sub(velocity);
	}

	protected void setNewPosition(float seconds) {
		/* Berechnung der neuen Position durch den Verlet-Algorithmus */
		Vector acceleration = ((this.getForce()).div(this.getMass()))
				.mul(seconds * seconds);
		Vector newPosition = (((this.position).add(this.position))
				.sub(this.oldPosition)).add(acceleration);
		this.oldPosition = this.position;
		this.position = newPosition;
	}

	/**
	 * Prüft, ob die beiden Objekte kollidieren. Sollte dies der Fall sein,
	 * werden die entsprechenden Crash-Methoden aufgerufen
	 * 
	 * @param other
	 *            Das Objekt, bei dem geprüft werden soll, ob es mit diesem
	 *            kollidiert
	 * @return Kollisionsstatus (Kollidieren die Objekte oder nicht)
	 */
	public final boolean doesCollide(AbstractObject other) {
		if (this.alreadyDone(other)) {
			/*
			 * Gibt den Kollisionstatus dirket zurück, wenn die Objekte schon
			 * berechnet wurden
			 */
			return this.alreadyDone.get(other.getID());
		}
		Shape thisShape;
		Shape otherShape;
		switch (this.type) {
		case TYPE_BALL:
			thisShape = new Circle(this.getPosition(), this.getDimension());
			break;
		case TYPE_BOX:
			thisShape = new Rectangle(this.getPosition(), this.getDimension());
			break;
		default:
			return false;
		}
		switch (other.type) {
		case TYPE_BALL:
			otherShape = new Circle(this.getPosition(), this.getDimension());
			break;
		case TYPE_BOX:
			otherShape = new Rectangle(this.getPosition(), this.getDimension());
			break;
		default:
			return false;
		}
		if (thisShape.doesCollide(otherShape)) {
			/* Gegenseitiger Aufruf der Crash-Methoden */
			this.crashedByObject(other);
			other.crashedByObject(this);
			this.addDone(other, true);
			other.addDone(this, true);
			return true;
		} else {
			this.addDone(other, false);
			other.addDone(this, false);
			return false;
		}
	}

	private void crashedByObject(AbstractObject other) {
		if (other instanceof VorlageGegner) {
			this.crashedByEnemy((VorlageGegner) other);
		} else if (other instanceof VorlageObjekte) {
			this.crashedByObjekt((VorlageObjekte) other);
		} else if (other instanceof VorlageLandschaft) {
			this.crashedByGround((VorlageLandschaft) other);
		} else if (other instanceof Figure) {
			this.crashedByPlayer((Figure) other);
		} else {
		}
	}
}
