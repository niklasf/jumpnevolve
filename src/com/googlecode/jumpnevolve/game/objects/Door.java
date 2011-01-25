package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
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
public class Door extends ObjectTemplate implements Activable, Blockable {

	private static final long serialVersionUID = -1980816280681808337L;

	private boolean openingState = false;

	public Door(World world, Vector position, Vector dimension) {
		super(world, ShapeFactory.createRectangle(position, dimension), 0.0f);
	}

	public Door(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// nichts tun

	}

	@Override
	public void activate(Activating activator) {
		if (activator.getCompany() == COMPANY_OBJECT) {
			this.open();
		}
	}

	@Override
	public void deactivate(Activating deactivator) {
		if (deactivator.getCompany() == COMPANY_OBJECT) {
			this.close();
		}
	}

	private void open() {
		this.openingState = true;
	}

	private void close() {
		this.openingState = false;
	}

	public void draw(Graphics g) {
		if (openingState == false) {
			GraphicUtils.texture(g, getShape(), ResourceManager.getInstance()
					.getImage("textures/wood.png"), false);
		}
	}

	@Override
	public boolean isActivableBy(Activating activator) {
		return activator.getCompany() == COMPANY_OBJECT;
	}

	@Override
	public boolean isActivated() {
		return openingState;
	}

	@Override
	public boolean isDeactivableBy(Activating deactivator) {
		return deactivator.getCompany() == COMPANY_OBJECT;
	}

	@Override
	public boolean canBeBlockedBy(Blockable other) {
		return false;
	}

	@Override
	public boolean wantBlock(Blockable other) {
		return !openingState;
	}
}
