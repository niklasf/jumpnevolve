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
import java.util.LinkedList;

import org.newdawn.slick.Input;

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
	 * 
	 * Möglichkeiten, Attribute zu ändern:
	 * 
	 * //sich selbst aktivieren / deaktivieren //Tod des Gegners oder von sich
	 * selbst //Änderung der Geschwindigkeit //Setzen der Geschwindigkeit (auch
	 * das Setzen nur des x-/y-Anteils z.B. auf 0) //Addieren einer Kraft
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

	/*
	 * Setzt die Standard-Werte für jede Runde (z.B. Schwerkraft)
	 */
	public abstract void setStandardsPerRound();

	private Vector position;
	private Vector oldPosition;
	private Vector velocity;
	private Vector force;
	private Vector dimension;
	private Shape thisShape;
	private Shape thisOldShape;
	public final byte type;
	public static final byte TYPE_BALL = 0;
	public static final byte TYPE_BOX = 1;
	private static int Id = 0;
	public final int id;
	private float currentSecounds;

	/*
	 * HashMap um abzuspeichern, welche Objekte schon behnadelt wurden, dadurch
	 * brauchen 2 Objekte nur einmal auf einen Crash zuprüfen; Der boolean-Wert
	 * speichert den Zustand der Kollision (Ja oder Nein)
	 */
	private HashMap<Integer, Boolean> alreadyDone;

	/*
	 * Die World-Instanz in der dieses Objekt gespeichert wurde
	 */
	public final World worldOfThis;

	protected AbstractObject(byte type, Vector position, Vector dimension,
			Vector force, World worldOfThis) {
		this.type = type;
		this.position = position;
		this.oldPosition = position;
		this.velocity = Vector.ZERO;
		this.dimension = dimension;
		this.force = force;
		this.id = Id++;
		this.worldOfThis = worldOfThis;
		this.setNewShape();
	}

	protected AbstractObject(byte type, Vector position, Vector dimension,
			World worldOfThis) {
		this.type = type;
		this.position = position;
		this.oldPosition = position;
		this.velocity = Vector.ZERO;
		this.dimension = dimension;
		this.force = Vector.ZERO;
		this.id = Id++;
		this.worldOfThis = worldOfThis;
		this.setNewShape();
	}

	/**
	 * Gibt zurück, ob die Kollision der Objekte schon berechnet wurde
	 * 
	 * @param other
	 *            Das Objekt, bei dem geprüft werden soll, ob es mit diesem
	 *            kollidiert
	 * @return Ob es schon berechnet wurde
	 */
	public final boolean alreadyDone(AbstractObject other) {
		return this.alreadyDone.containsKey(other.getID());
	}

	/**
	 * Fügt den Kollisions-Status mit dem anderen Objekt der HashMap hinzu
	 * 
	 * @param other
	 *            Das andere Objekt
	 * @param state
	 *            Der Kollisions-Status
	 */
	private void addDone(AbstractObject other, boolean state) {
		this.alreadyDone.put(other.getID(), state);
	}

	/**
	 * Muss vor jeder Runde neu aufgerufen werden
	 * 
	 * Nimmt Einstellungen vor, die vor einer neuen Berechnungsrunde erneuert
	 * werden müssen
	 */
	public void newCalculationRound() {
		this.alreadyDone.clear();
		this.addDone(this, true);
		this.setStandardsPerRound();
	}

	/**
	 * @return ID des AbstractObjects
	 */
	public final int getID() {
		return this.id;
	}

	/**
	 * @return Die Masse des Objekts
	 */
	public final float getMass() {
		return 1; // FIXME: Masse-Variable erstellen und zurückgeben
	}

	/**
	 * @return Aktuelle Geschwindigkeit anhand der alten Position
	 */
	public final Vector getVelocity() {
		return this.velocity;
	}

	/**
	 * @return Die Kraft die z.Z. auf das Objekt wirkt
	 */
	public final Vector getForce() {
		return this.force;
	}

	/**
	 * @return Der Typ der mathematischen Figur (vgl. Typ-Konstanten)
	 */
	public final byte getType() {
		return this.type;
	}

	/**
	 * @return "Dimension" des Objekts; bei einer BOX: Höhe und Breite; bei
	 *         einem BALL: Radius (entspricht dem Betrag des Vektors)
	 */
	public final Vector getDimension() {
		return this.dimension;
	}

	/**
	 * @return Die x-Koordinate des linken Endes des Objekts
	 */
	public final float getHorizontalStart() {
		return this.thisShape.getLeftEnd();
	}

	/**
	 * @return Die x-Koordinate des rechten Endes des Objekts
	 */
	public final float getHorizontalEnd() {
		return this.thisShape.getRightEnd();
	}

	/**
	 * @return Die x-Koordinate des linken Endes des Objekts vor einer
	 *         Berechnungsrunde
	 */
	public final float getOldHorizontalStart() {
		return this.thisOldShape.getLeftEnd();
	}

	/**
	 * @return Die x-Koordinate des rechten Endes des Objekts vor einer
	 *         Berechnungsrunde
	 */
	public final float getOldHorizontalEnd() {
		return this.thisOldShape.getRightEnd();
	}

	/**
	 * @return Die mathematische Figur, die das Objekt beschreibt
	 */
	public final Shape getShape() {
		return this.thisShape;
	}

	/**
	 * @return Die mathematische Figur, die das Objekt (vor einer
	 *         Berechnungsrunde) beschreibt
	 */
	public final Shape getOldShape() {
		return this.thisOldShape;
	}

	/**
	 * @return addiert eine Kraft zur Kraft, die aktuell auf das Objekt wirkt
	 */
	public void applyForce(Vector force) {
		this.force = this.force.add(force);
	}

	/**
	 * Führt Berechnungen / Einstellungen am Ende einer Berechnungsrunde aus
	 */
	public void finalizeStep(boolean undo) {
		this.setNewPosition(this.currentSecounds);
	}

	/**
	 * @return Die aktuelle Position des Objekts; ACHTUNG: Abhängig von der Form
	 *         des Objekts; BOX: obere, rechte Ecke; BALL: Mittelpunkt
	 */
	public final Vector getPosition() {
		return this.position;
	}

	/**
	 * @return Die aktuelle Position des Objekts vor einer Berechnungsrunde;
	 *         ACHTUNG: Abhängig von der Form des Objekts; BOX: obere, rechte
	 *         Ecke; BALL: Mittelpunkt
	 */
	public final Vector getOldPosition() {
		return this.oldPosition;
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
		this.velocity = velocity;
	}

	/**
	 * Berechnet und setzt die mathematische Figur des Objekts neu
	 */
	public final void setNewShape() {
		this.thisOldShape = this.thisShape;
		switch (this.type) {
		case TYPE_BALL:
			this.thisShape = new Circle(this.getPosition(), this.getDimension());
			break;
		case TYPE_BOX:
			this.thisShape = new Rectangle(this.getPosition(), this
					.getDimension());
			break;
		default:
			// FIXME: Fehlermeldung ausgeben
		}
	}

	/**
	 * Berechnet die neue Position des Objekts und setzt diese als aktuelle
	 * Position
	 * 
	 * @param seconds
	 *            Seit der letzten Berechnung vergangene Zeit
	 */
	private void setNewPosition(float secounds) {
		Vector acceleration = ((this.getForce()).div(this.getMass()))
				.mul(secounds * secounds);
		Vector velocity = this.velocity.mul(secounds);
		Vector newPosition = this.position.add(velocity).add(acceleration);
		this.oldPosition = this.position;
		this.position = newPosition;
		this.setNewShape();
		this.worldOfThis.changedPosition(this);
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
		Shape thisShape = this.getShape();
		Shape otherShape = other.getShape();
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

	/**
	 * Ruft je nach Objekt-Typ die entsprechende Crash-Methode auf
	 * 
	 * Wird bei einem Crash aufgerufen
	 * 
	 * @param other
	 *            Das andere Objekt
	 */
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

	@Override
	public void poll(Input input, float secounds) {
		this.currentSecounds = secounds;
		LinkedList<AbstractObject>[] neighbours = this.worldOfThis
				.getNeighbours(this);
		for (LinkedList<AbstractObject> neighboursSub : neighbours) {
			for (AbstractObject neighbour : neighboursSub) {
				this.doesCollide(neighbour);
			}
		}
	}
}
