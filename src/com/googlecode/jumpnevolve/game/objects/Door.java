package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

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

	private static final long serialVersionUID = -1980816280681808337L;

	private boolean opneningState = false;

	public Door(World world, Vector position, Vector dimension) {
		super(world, new Rectangle(position, dimension), 0.0f, true, false,
				true, false);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// nichts tun

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

	@Override
	public String toDataLine() {
		// FIXME: Bitte überprüfee, geht das so mit den Vektoren
		return new String("Door_" + this.getPosition() + "_" + this.getName()
				+ "_none_" + this.getShape().getDimensions() + "\n");
	}

	// TODO: draw-Methode einfügen
}
