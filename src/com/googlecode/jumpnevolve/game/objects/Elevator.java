package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Moving;
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
public class Elevator extends ObjectTemplate implements Moving {

	private static final long serialVersionUID = 4385912397697222758L;

	private final float upEnd, downEnd;
	private Vector curDirection = Vector.UP;

	public Elevator(World world, Vector position, Vector dimension,
			float downEnd, float upEnd) {
		super(world, new Rectangle(position, dimension), 2.0f, true);
		if (upEnd > downEnd) {
			this.upEnd = downEnd;
			this.downEnd = upEnd;
		} else {
			this.upEnd = upEnd;
			this.downEnd = downEnd;
		}
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getPosition().y <= this.upEnd) {
			this.curDirection = Vector.DOWN;
		}
		if (this.getPosition().y >= this.downEnd) {
			this.curDirection = Vector.UP;
		}
		if (this.getVelocity().y == 0) {
			this.curDirection = Vector.UP;
		}
	}

	public void blockWay(AbstractObject blocker) {
		if (blocker.isMoveable() == false) {
			super.blockWay(blocker);
		}
	}

	// TODO: draw-Methode einfügen
	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}

	@Override
	public Vector getMovingDirection() {
		return curDirection;
	}

	@Override
	public float getMovingSpeed() {
		return 50.0f;
	}
}
