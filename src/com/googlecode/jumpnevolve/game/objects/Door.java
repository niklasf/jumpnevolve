package com.googlecode.jumpnevolve.game.objects;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Input;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

/**
 * 
 * Beschreibung: Eine Tür, die geöffnet (aktiviert) werden kann
 * 
 * Spezifikationen: nicht blockbar, nicht schiebbar
 * 
 * Bewegungen: keine
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: Die Tür öffnet sich
 * 
 * Deaktivierung: Die Tür schließt sich
 * 
 * Besonderheiten: blockt, wenn die Tür geschlossen ist
 * 
 * @author Erik Wagner
 * 
 */
public class Door extends ObjectTemplate {

	private boolean opneningState = false;

	public Door(World world) {
		super(world, shape, 0.0f, true, false, true, false);
		// FIXME: Shape erstellen
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// TODO Auto-generated method stub

	}

	@Override
	public void activate(AbstractObject activator) {
		if (activator instanceof ActivatingObject) {
			this.open();
		}
	}

	@Override
	public void deactivate(AbstractObject deactivator) {
		if (deactivator instanceof ActivatingObject) {
			this.close();
		}
	}

	@Override
	public void onBlockableCrash(AbstractObject other) {
		if (opneningState == false) {
			other.blockWay(this);
		}
	}

	private void open() {
		this.opneningState = true;
	}

	private void close() {
		this.opneningState = false;
	}
}
