package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.FigureTemplate;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
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
public class Button extends ActivatingObject {

	private static final long serialVersionUID = 7682200558867723370L;

	private Timer remainingTime = new Timer();
	private final float activatingTime;

	public Button(World world, Vector position, float activatingTime) {
		super(world, new Rectangle(position, new Vector(30, 30)), 0.0f, false,
				false, true, false);
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
			for (AbstractObject object : this.getObjectsToActivate()) {
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
	public void activate(AbstractObject activator) {
		if (activator instanceof FigureTemplate) {
			// Alle zu aktivierenden Objekte aktivieren
			for (AbstractObject object : this.getObjectsToActivate()) {
				object.activate(this);
			}
			// Timer zurücksetzen und starten, wenn Zeit vorgegeben
			if (this.activatingTime > 0) {
				this.remainingTime.start(this.activatingTime);
			}
		}
	}

	// TODO: draw-Methode einfügen
}
