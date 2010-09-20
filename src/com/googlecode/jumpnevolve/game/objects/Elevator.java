package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * 
 * Beschreibung: Eine Plattform, die sich rauf und runter bewegt
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 1; vertikal in einem bestimmten Bereich --> 50 Pixel pro
 * Sekunde
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: keine
 * 
 * Besonderheiten: keine
 * 
 * @author Erik Wagner
 * 
 */
public class Elevator extends ObjectTemplate {

	private static final long serialVersionUID = 4385912397697222758L;

	private final float upEnd, downEnd;

	public Elevator(World world, Vector position, Vector dimension,
			float downEnd, float upEnd) {
		super(world, new Rectangle(position, dimension), 1.0f, true, false,
				false, false);
		this.upEnd = upEnd;
		this.downEnd = downEnd;
		this.setVelocity(Vector.UP.mul(50.0f));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getPosition().x <= this.upEnd + 10 && this.getPosition().x >= this.upEnd - 10 ) {
			this.setVelocity(Vector.DOWN.mul(50.0f));
		}
		if (this.getPosition().x <= this.downEnd + 10 && this.getPosition().x >= this.downEnd - 10) {
			this.setVelocity(Vector.UP.mul(50.0f));
		}
	}

	// TODO: draw-Methode einfügen
}
