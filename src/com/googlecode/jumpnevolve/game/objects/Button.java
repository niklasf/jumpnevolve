package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Ein Knopf der durch den Spieler bei Berührung aktiviert wird
 * 
 * Spezifikationen: nicht blockbar, nicht schiebbar
 * 
 * Bewegungen: keine
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: Aktiviert eine beliebige Anzahl von Objekten für eine gewisse
 * Zeit oder wahlweise auch für immer
 * 
 * Besonderheiten: keine
 * 
 * @author Erik Wagner
 * 
 */
public class Button extends ActivatingObject implements Activable {

	private static final long serialVersionUID = 7682200558867723370L;

	private Timer remainingTime = new Timer();
	private final float activatingTime;

	public Button(World world, Vector position, float activatingTime) {
		super(world, ShapeFactory.createRectangle(position, 60, 60), 0.0f);
		this.remainingTime.setTime(activatingTime);
		this.activatingTime = activatingTime;
	}

	public Button(World world, Vector position) {
		this(world, position, 0);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.remainingTime.didFinish()) {
			// Alle aktivierten Objekte wieder deaktivieren, wenn die Zeit
			// abgelaufen ist
			for (Activable object : this.getObjectsToActivate()) {
				object.deactivate(this);
			}
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		// Zeit weiterlaufen lassen
		this.remainingTime.poll(input, secounds);
		super.poll(input, secounds);
	}

	@Override
	public void activate(Activating activator) {
		// Alle zu aktivierenden Objekte aktivieren
		for (Activable object : this.getObjectsToActivate()) {
			object.activate(this);
		}
		// Timer zurücksetzen und starten, wenn Zeit vorgegeben
		if (this.activatingTime > 0) {
			this.remainingTime.start(this.activatingTime);
		}

	}

	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}

	@Override
	public void deactivate(Activating deactivator) {
		for (Activable object : this.getObjectsToDeactivate()) {
			object.deactivate(this);
		}
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		// Ein Button kann nur durch den Spieler aktiviert werden
		return activator.getCompany() == COMPANY_PLAYER;
	}

	@Override
	public boolean isActivated() {
		return this.remainingTime.isRunning();
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		// Ein Button kann nicht deaktiviert werden
		return false;
	}

	@Override
	public void hasActivated(Activable object) {
		// Nichts tun
	}

	@Override
	public void hasDeactivated(Activable object) {
		// Nichts tun
	}

	@Override
	public boolean wantActivate(Activable object) {
		return this.getObjectsToActivate().contains(object)
				&& this.isActivated() == false;
	}

	@Override
	public boolean wantDeactivate(Activable object) {
		return this.getObjectsToDeactivate().contains(object)
				&& this.isActivated();
	}
}
